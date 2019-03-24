package com.ycbbs.crud.controller;

import com.ycbbs.crud.entity.UserInfo;
import com.ycbbs.crud.exception.CustomException;
import com.ycbbs.crud.pojo.YcBbsResult;
import com.ycbbs.crud.realm.CustomRealm;
import com.ycbbs.crud.service.UserInfoService;
import com.ycbbs.crud.utils.AccountValidatorUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private UserInfoService userInfoService;

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

    /**
     * 登陆
     * @param user
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @PostMapping("/loginApp")
    public YcBbsResult login(@RequestBody UserInfo user) throws Exception {
        Subject currentUser = SecurityUtils.getSubject();
        //表示是否登录过
        if (!currentUser.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(),user.getPassword());
            //表示是否登录过
            if (!currentUser.isAuthenticated()) {
                try {
                    currentUser.login(token);
                } catch (LockedAccountException e) {
                    //5、身份验证失败
                    return YcBbsResult.build(100,"账号被锁定!!!");
                }catch (DisabledAccountException e) {
                    //5、身份验证失败
                    return YcBbsResult.build(100,"账号被禁用!!!");
                }
                catch (UnknownAccountException e) {
                    //5、用户名错误
                    return YcBbsResult.build(100,"用户名错误!!!");
                }catch (ExcessiveAttemptsException e) {
                    //5、登录失败次数过多
                    return YcBbsResult.build(100,"登录失败次数过多!!!");
                }catch (IncorrectCredentialsException e) {
                    //5、密码错误
                    return YcBbsResult.build(100,"密码错误!!!");
                }catch (ExpiredCredentialsException e) {
                    //5、过期的凭证
                    return YcBbsResult.build(500,"过期的凭证");
                }catch (AuthenticationException e) {
                    //6、账号未激活
                    return YcBbsResult.build(500,e.getMessage());
                }
            }
        }
        return this.successUrl();
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
        return YcBbsResult.build(400,"激活失败");
    }





    @CrossOrigin
    @RequestMapping("/test02")
    @RequiresPermissions("item:query")
    public YcBbsResult test02() throws CustomException {
        return YcBbsResult.build(200,"这个已授权:item:query");
    }
    @CrossOrigin
    @RequestMapping("/test03")
    @RequiresPermissions("item:update")
    public YcBbsResult test03() throws CustomException {
        Subject subject = SecurityUtils.getSubject();
        return YcBbsResult.build(200,"这个已授权:item:update",subject.getPrincipal());
    }
    @CrossOrigin
    @RequestMapping("/test04")
    @RequiresPermissions("item:delete")
    public YcBbsResult test04() throws CustomException {
        Subject subject = SecurityUtils.getSubject();
        return YcBbsResult.build(200,"测试无授权",subject.getPrincipal());
    }

    /**
     * 清空缓存(这个需要写在业务逻辑层)
     */

    @Autowired
    private CustomRealm customRealm;
    @CrossOrigin
    @RequestMapping("/clearCache")
    public YcBbsResult clearCache() throws CustomException {
        customRealm.clearCached();
        return YcBbsResult.build(200,"清空缓存成功!!!");
    }
}
