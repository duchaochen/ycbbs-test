#通用Mappr
    码云地址：https://gitee.com/free/Mapper/wikis/pages?sort_id=208197
    github地址:https://github.com/abel533/Mapper
###mybatis的jdbc允许执行多条sql语句
    加上这个属性allowMultiQueries=true
    jdbc.url=jdbc:mysql://47.99.194.149:3306/yc_bbs?useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true

###1.配置的变化
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <!--扫描所有dao接口的实现，加入到ioc容器中 -->
        <property name="basePackage" value="com.mybatis.plus.mapper"/>
    </bean>
    
    将上面的全限定名的org修改为tk就好了
    <bean class="tk.mybatis.spring.mapper.MapperScannerConfigurer">
        <!--扫描所有dao接口的实现，加入到ioc容器中 -->
        <property name="basePackage" value="com.mybatis.plus.mapper"/>
    </bean>

###2.接口类变化
    创建一个EmployeeMapper接口并且继承Mapper<Employee>
    这里需要注意的是通用接口中的tk.mybatis.mapper.common.Mapper而不是org的
    public interface EmployeeMapper extends Mapper<Employee> {
    }
    以上继承之后就已经有很多公用的增删改查的方法了。
    
###3.创建一个service业务层来调用
    @Service
    public class EmployeeService {
        @Autowired
        private EmployeeMapper employeeMapper;
    
        /**
         * 获取单个数据信息
         * @param employeeQueryWhere
         * @return
         */
        public Employee getOne(Employee employeeQueryWhere) {
            return  employeeMapper.selectOne(employeeQueryWhere);
        }
    }

###4.错误信息
    1.  nested exception is com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException: Table 'yc_bbs.employee' doesn't exist
        以上问题表示表名和实体类的名称不一致，所以我们需要使用@table注解@Table(name = "tabple_emp")
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @Accessors(chain = true)
        @Table(name = "tabple_emp")
        public class Employee {
           private Integer empId;
           private String empName;
           private Double empSalary;
           private Integer empAge;
        }
    2.  还有一个@Column注解是实体的属性名和数据库的字段名不一致可以使用此注解
    3.  如果要根据主键查询需要在实体的主键字段上加上@Id这个注解，
        要不然mapper认为是组合主键，会将所有的字段都当做主键拿去查询
            @Id
            private Integer empId;
            
            employeeMapper.selectByPrimaryKey(empId);
        注意所有带有ByPrimaryKey的都需要使用@id注解的
        
        //获取自增的值
        @GeneratedValue(strategy = GenerationType.IDENTITY)
            
        如果字段是数据库中没有的我们需要使用@Transient注解来过滤，否则会报异常
        @Transient
        private Integer editEmpName;
        
        
###springmvc的拦截器、适配器（conversionService）、过滤器
    1.过滤器和springmvc没有关系
    2.拦截器是处理请求的各个阶段的
    3.适配器conversionService是处理参数绑定阶段
    
    
    
#shiro简单使用

     1. 创建一个自定义的CustomRealm类，来做认证和授权使用的，需要继承AuthorizingRealm类,重写以下2个方法
        1)doGetAuthenticationInfo认证发放，所有需要认证的都会经过次方法
        2)doGetAuthorizationInfo授权方法，所有用户要授权的方法都会在此方法中授权才可以访问，
        需要跟@RequiresPermissions注解来配合使用详情见程序中代码。
        
        
     2. 然后将此CustomRealm类挂在到spring中，applicationContext-shiro.xml配置
        <!-- 自定义 realm -->
        <bean id="userRealm" class="com.ycbbs.crud.realm.CustomRealm">
            <!--将认证需要的凭证匹配器注入到自定义realm中-->
            <property name="credentialsMatcher" ref="credentialsMatcher"/>
        </bean>
        <!-- 凭证匹配器，改类是为了和用户的密码加盐来进行匹配是否能认证成功的,使用md5的算法来加密1次判断 -->
         <bean id="credentialsMatcher"
               class="org.apache.shiro.authc.credential.HashedCredentialsMatcher">
             <property name="hashAlgorithmName" value="md5" />
             <property name="hashIterations" value="1" />
         </bean>
         
     3. 将自定义CustomRealm类注入到DefaultWebSecurityManager安全管理器的类中,applicationContext-shiro.xml配置
        <!-- 安全管理器 -->
        <bean id="securityManager" class="org.apache.shiro.web.mgt.DefaultWebSecurityManager">
            <property name="realm" ref="userRealm" />
        </bean>
        
     4. 开启shiro的注解支持（@RequiresPermissions("item:query")）这种形式
        注意:这段配置必须写在springmvc的配置文件中，也就是需要前端控制器来控制springmvc.xml配置
        <!-- 开启aop，对类代理 -->
        <aop:config proxy-target-class="true"/>
        <!-- 开启shiro注解支持 -->
        <bean class="org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor">
             <!-- 这里是将securityManager安全管理器注入进去,上面已经配置 -->
            <property name="securityManager" ref="securityManager" />
        </bean>
    
     5.最后需要配置shiro的过滤器,applicationContext-shiro.xml配置
        1)  spring中实例化过滤器
            <!-- Shiro 的Web过滤器 -->
            <bean id="shiroFilter" class="org.apache.shiro.spring.web.ShiroFilterFactoryBean">
                 <property name="securityManager" ref="securityManager" />
                 <!-- loginUrl认证提交地址，如果没有认证将会请求此地址进行认证，
                 请求此地址将由formAuthenticationFilter进行表单认证 -->
                 <property name="loginUrl" value="/login" />
                 <!--认证失败的跳转地址-->
                 <property name="unauthorizedUrl" value="/refuse" />
                 <!-- 过虑器链定义，从上向下顺序执行，一般将/**放在最下边 -->
                 <property name="filterChainDefinitions">
                     <value>
                         <!--退出,请求 /logout地址，shiro去清除session-->
                         /logout = logout
                         <!-- 所有匿名访问 -->
                         <!--/login = anon-->
                         <!--所有的都需要认真-->
                         /** = authc
                     </value>
                 </property>
            </bean>
            
        2)web.xml中配置shiro过滤器
            <!-- shiro过虑器，DelegatingFilterProxy通过代理模式将spring容器中的bean和filter关联起来 -->
            <filter>
                <filter-name>shiroFilter</filter-name>
                <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
                <!-- 设置true由servlet容器控制filter的生命周期 -->
                <init-param>
                    <param-name>targetFilterLifecycle</param-name>
                    <param-value>true</param-value>
                </init-param>
                <!-- 设置spring容器filter的bean id，如果不设置则找与filter-name一致的bean-->
                <init-param>
                    <param-name>targetBeanName</param-name>
                    <param-value>shiroFilter</param-value>
                </init-param>
            </filter>
            <filter-mapping>
                <filter-name>shiroFilter</filter-name>
                  <!-- 拦截所有 -->
                <url-pattern>/*</url-pattern>
            </filter-mapping>
     
     6.@RequiresPermissions使用
        1.在自定义的CustomRealm类中的授权方法中添加：simpleAuthorizationInfo.addStringPermission("item:update");
          表示用户有此权限那么在请求的方法上添加@RequiresPermissions("item:update")表示该方法是可以授权通过的。
          以下实例代码：
            @RequestMapping("/test03")
            @RequiresPermissions("item:update")
            public YcBbsResult test03() throws CustomException {
                Subject subject = SecurityUtils.getSubject();
                return YcBbsResult.build(200,"这个已授权:item:update",subject.getPrincipal());
            }
        2.用户中授权标记集合中是没有此方法，也就是表示此方法是授权不通过，直接会执行/refuse控制器中,这个在shiro过滤器中已经配置
        @RequestMapping("/test04")
        @RequiresPermissions("item:delete")
        public YcBbsResult test04() throws CustomException {
            Subject subject = SecurityUtils.getSubject();
            return YcBbsResult.build(200,"测试无授权",subject.getPrincipal());
        }
        
        注意：如果不加@RequiresPermissions注解的情况下，认证成功之后，将不会进行授权匹配的，
        也就是说不会触发CustomRealm类中的doGetAuthorizationInfo方法
        
     7.添加ehcache缓存
        1)添加shiro-ehcache.xml
        2)applicationContext-shiro.xml配置
            <!-- 加缓存管理器,直接注入到securityManager安全管理器即可 -->
            <bean id="cacheManager" class="org.apache.shiro.cache.ehcache.EhCacheManager">
                <!--创建一个缓存xml-->
                <property name="cacheManagerConfigFile" value="classpath:shiro-ehcache.xml"/>
            </bean>
        3）将cacheManager注入到安全管理器(securityManager)中
        4）清空缓存,需要定义在CustomRealm类中，在权限更新的时候调用