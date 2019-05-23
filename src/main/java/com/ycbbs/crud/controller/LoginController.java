package com.ycbbs.crud.controller;

import com.ycbbs.crud.entity.UserInfo;
import com.ycbbs.crud.exception.CustomException;
import com.ycbbs.crud.pojo.YcBbsResult;
import com.ycbbs.crud.shiro.jwt.JWTToken;
import com.ycbbs.crud.shiro.jwt.JWTUtil;
import com.ycbbs.crud.service.UserInfoService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("ycbbs")
@RestController
public class LoginController {

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 登陆
     * @param user
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @PostMapping("/loginApp")
    public YcBbsResult login(@RequestBody UserInfo user) throws CustomException {
        if (null == user) {
            //用户名不存在
            throw new CustomException("用户传参为空传参错误");
        }
        //必须先查询，要不然密码加盐的算法没办法匹配上
        UserInfo userInfo = userInfoService.selectUserInfoByUserName("",user.getUsername());
        if(userInfo == null){
            //用户名不存在
            throw new UnknownAccountException();
        }
        //这里一定需要判断一下密码是否一致，要不然只需要用户名匹配就可以成功登录了。
        Md5Hash md5Hash = new Md5Hash(user.getPassword(),userInfo.getSalt(),1);
        String password = md5Hash.toString();
        if(!password.equals(userInfo.getPassword())){
            //密码错误
            throw new IncorrectCredentialsException();
        }

        String token= JWTUtil.sign(user.getUsername(), password);
        Subject currentUser = SecurityUtils.getSubject();
        //表示是否登录过或者已经有了记住我
        if (!currentUser.isAuthenticated()) {
            //开始登录认证
            currentUser.login(new JWTToken(token));
        }

        System.out.println(currentUser.getPrincipals());
        System.out.println(currentUser.isAuthenticated());

        return YcBbsResult.build(200,"认证成功",currentUser.getPrincipal(),token);
    }

    @CrossOrigin
    @RequestMapping("/")
    public YcBbsResult home() throws Exception {
        return YcBbsResult.build(200,"首页");
    }
    /**
     * 认证成功之后跳转的页面
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @RequestMapping("/successUrl")
    public YcBbsResult successUrl() throws Exception {
        return YcBbsResult.build(200,"认证成功");
    }




    @CrossOrigin
    @RequestMapping("/loginUrl")
    public YcBbsResult loginUrl(){
        return YcBbsResult.build(200,"认证成功");
    }

    /**
     * 清空缓存
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @GetMapping("/logout")
    public YcBbsResult logout()  {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
        return YcBbsResult.build(200,"缓存情况完成！！！");
    }

    /**
     * 激活
     * @param uid
     * @param code
     * @return
     * @throws CustomException
     */
    @CrossOrigin
    @GetMapping("/active")
    public YcBbsResult activeCode(String uid, String code) throws CustomException {

        boolean isActive = userInfoService.updateActiveCode(uid, code);
        if (isActive) {
            return YcBbsResult.build(200,"激活成功");
        }
        return YcBbsResult.build(400,"激活失败,请重新激活，如果不成功，请联系管理员!!!");
    }
}
