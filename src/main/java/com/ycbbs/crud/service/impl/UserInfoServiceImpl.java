package com.ycbbs.crud.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ycbbs.crud.entity.UserInfo;
import com.ycbbs.crud.exception.CustomException;
import com.ycbbs.crud.mapper.UserInfoMapper;
import com.ycbbs.crud.service.UserInfoService;
import com.ycbbs.crud.utils.SendMail;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.UUID;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    /**
     * 邮件发送类
     */
    @Autowired
    private SendMail sendMail;

    /**
     * 激活地址
     */
    @Value("${mail.url}")
    private String url;
    @Value("${mail.head}")
    private String head;
    /**
     * 根据用户名查询
     * @param username
     * @return
     */
    @Override
    public UserInfo selectUserInfoByUserName(String username) {
        UserInfo user = new UserInfo();
        user.setUsername(username);
        return userInfoMapper.selectOne(user);
    }
    /**
     * 添加数据
     * @param userInfo
     * @return
     */
    @Override
    public boolean insertUserInfo(UserInfo userInfo,String path) throws CustomException {
        //用来生成数据库的主键id非常不错。
        String uuid = UUID.randomUUID().toString();
        userInfo.setUid(uuid);
        userInfo.setSalt(uuid);
        userInfo.setCode(uuid.substring(0, 8));
        Md5Hash md5Hash = new Md5Hash(userInfo.getPassword(),userInfo.getSalt(),1);
        String password = md5Hash.toString();
        //将加密后的密码存入数据库
        userInfo.setPassword(password);
        userInfo.setLocked("0");
        //暂时设置为全部都是激活状态,如果实行激活操作这里直接去掉
        if (!"/register".equals(path)) {
            userInfo.setState("1");
        }
        int rows = userInfoMapper.insertSelective(userInfo);
        if (rows == 0) {
            throw new CustomException("系统错误，注册失败!!!");
        }
        if ("/register".equals(path)) {
            this.sendToMail(userInfo);
        }
        return true;
    }

    private void sendToMail(UserInfo userInfo) {
        String ativeUrl = url + "?method=active&uid="+userInfo.getUid()+"&code="+userInfo.getCode();
        // 发送 HTML 消息, 可以插入html标签
        StringBuilder msg = new StringBuilder();
        msg.append("<style>" +
                ".subject{\n" +
                "        width:550px;\n" +
                "        padding: 30px 30px 30px 100px;\n" +
                "        margin: 0 auto;\n" +
                "        background: rgb(235,237,240);\n" +
                "        border-radius: 10px;\n" +
                "    }\n" +
                "    p{\n" +
                "        font-size: 14px;\n" +
                "    }\n" +
                "    a{\n" +
                "        font-size: 18px;\n" +
                "    }");
        msg.append("</style>\n" +
                "<div style=\" padding-top: 50px; padding-bottom:200px;\">\n" +
                "    <div class=\"subject\">\n" +
                "        <h4>尊敬的"+userInfo.getEmail()+"，您好,</h4>\n" +
                "        <p style=\"padding-left: 5px;\">感谢您使用猿程社区服务</p>\n" +
                "        <p style=\"padding-left: 5px;\">请点击以下链接进行邮箱验证，以便开始使用您的猿程社区账户：</p>\n" +
                "        <p style=\"padding-left: 5px;\">\n" +
                "            <a href='" + ativeUrl + "'>"+ativeUrl+"</a>\n" +
                "            </br>\n" +
                "            如果您并未申请猿程社区服务账户，可能是其他用户误输入了您的邮箱地址。请忽略此邮件，或者联系我们。\n" +
                "        </p>\n" +
                "    </div>\n" +
                "</div>");
        //发送邮件
        sendMail.sendMessage(userInfo.getEmail(),this.head,msg.toString());
    }
    /**
     * 邮箱判断
     * @param str
     * @param status 0:邮箱，1：手机号
     * @return
     */
    @Override
    public boolean selectByObject(String str,int status) {
        UserInfo u = this.getUserinfo(str,status);
        int count = userInfoMapper.selectCount(u);
        if (count > 0) {
            return true;
        }
        return false;
    }
    /**
     * 用户激活
     * @param uid, code
     * @param code
     * @return
     * @throws CustomException
     */
    @Override
    public boolean updateActiveCode(String uid, String code) throws CustomException {

        Example example = new Example(UserInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("uid",uid)
                .andEqualTo("code",code);

        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(uid);
        if (null == userInfo) {
            throw new CustomException("激活码与用户账号匹配无效");
        } else if ("1".equals(userInfo.getState())) {
            throw new CustomException("该账号无需再次激活");
        }
        int rows = userInfoMapper.updateByExampleSelective(new UserInfo().setState("1"),example);
        if (rows > 0) {
            return true;
        }
        return false;
    }
    /**
     * 查询所有用户
     * @param username
     * @return
     */
    @Override
    public PageInfo<UserInfo> selectKeyAll(String username, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        //查询所有书籍
        List<UserInfo> userInfos = this.selectKeyAll(username);

        PageInfo<UserInfo> pageInfo = new PageInfo<>(userInfos);
        return pageInfo;
    }
    /**
     * 根据关键字查询所有用户
     * @param keyname
     * @return
     */
    @Override
    public List<UserInfo> selectKeyAll(String keyname){
        Example example = new Example(UserInfo.class);
        if (null != keyname && !"".equals(keyname.trim())){
            Example.Criteria criteria = example.createCriteria();
            criteria.andLike("username","%"+keyname+"%")
                    .orLike("realname","%"+keyname+"%");
        }
        List<UserInfo> userInfos = userInfoMapper.selectByExample(example);
        return userInfos;
    }
    /**
     * 根据状态来为userinfo赋值
     * @param str
     * @param status 0为手机号，1为邮箱
     * @return
     */
    private UserInfo getUserinfo(String str,int status) {
        UserInfo u = new UserInfo();
        if (0 == status) {
            u.setEmail(str);
        } else if (1 == status) {
            u.setTelephone(str);
        }
        return u;
    }
}
