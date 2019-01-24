package com.ycbbs.crud.controller;

import com.ycbbs.crud.exception.CustomException;
import com.ycbbs.crud.pojo.YcBbsResult;
import com.ycbbs.crud.realm.CustomRealm;
import com.ycbbs.crud.service.PermissionInfoService;
import com.ycbbs.crud.service.UserInfoService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LoginController {

    @Autowired
    private UserInfoService userInfoService;
    /**
     * 这里是认证通过后
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/login")
    public YcBbsResult login(HttpServletRequest request) throws Exception {
        //如果登陆失败从request中获取认证异常信息，shiroLoginFailure就是shiro异常类的全限定名
        String exceptionClassName = (String) request.getAttribute("shiroLoginFailure");
        //根据shiro返回的异常类路径判断，抛出指定异常信息
        if(exceptionClassName!=null){
            if (UnknownAccountException.class.getName().equals(exceptionClassName)) {
                //最终会抛给异常处理器
                throw new CustomException("账号不存在");
            } else if (IncorrectCredentialsException.class.getName().equals(
                    exceptionClassName)) {
                throw new CustomException("用户名/密码错误");
            } else if("randomCodeError".equals(exceptionClassName)){
                throw new CustomException("验证码错误 ");
            }else {
                //最终在异常处理器生成未知错误
                throw new Exception();
            }
        }
        //此方法不处理登陆成功（认证成功），shiro认证成功会自动跳转到上一个请求路径
        //登陆失败还到login页面
        return YcBbsResult.build(200,"这里是已经认真通过或者第一次执行!!!");
    }
    /**
     * 认证失败调用
     * @return
     * @throws CustomException
     */
    @RequestMapping("/refuse")
    public YcBbsResult refuse() throws CustomException {
        return YcBbsResult.build(500,"认证不通过");
    }
    /**
     * 认证通过后取的数据
     * 登录认证成功之后怎么指定到某个请求
     * @return
     * @throws CustomException
     */
    @RequestMapping("/")
    public YcBbsResult home() throws CustomException {
        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();
        return YcBbsResult.build(200,"认证成功之后走的",principal);
    }

    @RequestMapping("/test01")
    @RequiresPermissions("item:create")
    public YcBbsResult test01() throws CustomException {
        return YcBbsResult.build(200,"这个已授权:item:create");
    }

    @RequestMapping("/test02")
    @RequiresPermissions("item:query")
    public YcBbsResult test02() throws CustomException {
        return YcBbsResult.build(200,"这个已授权:item:query");
    }

    @RequestMapping("/test03")
    @RequiresPermissions("item:update")
    public YcBbsResult test03() throws CustomException {
        Subject subject = SecurityUtils.getSubject();
        return YcBbsResult.build(200,"这个已授权:item:update",subject.getPrincipal());
    }

    @RequestMapping("/test04")
    @RequiresPermissions("item:delete")
    public YcBbsResult test04() throws CustomException {
        Subject subject = SecurityUtils.getSubject();
        return YcBbsResult.build(200,"测试无授权",subject.getPrincipal());
    }

    @Autowired
    private PermissionInfoService permissionInfoService;

    /**
     * 清空缓存(这个需要写在业务逻辑层)
     */
    @Autowired
    private CustomRealm customRealm;
    @RequestMapping("/clearCache")
    public YcBbsResult clearCache() throws CustomException {
        customRealm.clearCached();
        return YcBbsResult.build(200,"清空缓存成功!!!");
    }
}
