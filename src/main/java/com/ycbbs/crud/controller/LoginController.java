package com.ycbbs.crud.controller;

import com.ycbbs.crud.entity.UserInfo;
import com.ycbbs.crud.exception.CustomException;
import com.ycbbs.crud.pojo.YcBbsResult;
import com.ycbbs.crud.realm.CustomRealm;
import com.ycbbs.crud.service.PermissionInfoService;
import com.ycbbs.crud.service.UserInfoService;
import com.ycbbs.crud.utils.AccountValidatorUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private UserInfoService userInfoService;
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
     * 这里是认证通过后
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @PostMapping("/login")
    public YcBbsResult login(@RequestParam("username") String username,
                             @RequestParam("password") String password) throws Exception {
        Subject currentUser = SecurityUtils.getSubject();
        //表示是否登录过
        if (!currentUser.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken(username,password);
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
                }
            }
        }
        return this.successUrl();
    }
    /**
     * 注册
     * @return
     * @throws CustomException
     */
    @CrossOrigin
    @PostMapping("/register")
    public YcBbsResult register(UserInfo userInfo) throws CustomException {
        //用户检查，如果注册信息不符合信息的将存进map中
        Map<String, String> stringStringMap = this.checkUserInfo(userInfo);
        if (stringStringMap.size() > 0) {
            return YcBbsResult.build(300,"用户注册信息不合法",stringStringMap);
        }
        boolean isSave = userInfoService.insertUserInfo(userInfo);
        if (!isSave) {
            return YcBbsResult.build(500, "内部错误:注册失败", stringStringMap);
        }
        return YcBbsResult.build(200,"注册成功",stringStringMap);
    }

    /**
     * 检查用户信息是否合法
     * @param userInfo
     * @return
     */
    private Map<String,String> checkUserInfo(UserInfo userInfo) {
        Map<String,String> erorrMap = new HashMap<>(10);
        if (null == userInfo) {
            erorrMap.put("userinfo","用户信息还没有填写");
            return erorrMap;
        }
        //用户名判断
        if (null == userInfo.getUsername() || "".equals(userInfo.getUsername().trim())) {
            erorrMap.put("erorrUsername","用户名不能为空!!!");
        }
        else{
            UserInfo user = userInfoService.selectUserInfoByUserName(userInfo.getUsername().trim());
            if (null != user) {
                erorrMap.put("erorrUsername","用户名已存在!!!");
            }
        }
        //密码判断
        if (null == userInfo.getPassword() || "".equals(userInfo.getPassword().trim())) {
            erorrMap.put("erorrPassword","密码不能为空!!!");
        } else if (6 > userInfo.getPassword().trim().length()
                || userInfo.getPassword().trim().length() > 18) {
            erorrMap.put("erorrPassword","密码必须为6~18位!!!");
        }
        //判断邮箱
        if (null == userInfo.getEmail() || "".equals(userInfo.getEmail().trim())) {
            erorrMap.put("erorrEmail","email不能为空!!!");
        } else if (!AccountValidatorUtil.isEmail(userInfo.getEmail())) {
            erorrMap.put("erorrEmail", "email格式不对!!!");
        } else {
            //判断email是否存在
            boolean isEmail = userInfoService.selectByObject(userInfo.getEmail().trim(),0);
            if (isEmail) {
                erorrMap.put("erorrEmail", "该email已经被占用!!!");
            }
        }
        //判断电话号码
        if (null == userInfo.getTelephone() || "".equals(userInfo.getTelephone().trim())) {
            erorrMap.put("erorrTelephone", "电话号码不能为空!!!");
        } else if (!AccountValidatorUtil.isMobile(userInfo.getTelephone())) {
            erorrMap.put("erorrTelephone", "电话号码格式错误!!!");
        } else {
            //判断手机号是否存在
            boolean isTelephone = userInfoService.selectByObject(userInfo.getTelephone().trim(),1);
            if (isTelephone) {
                erorrMap.put("erorrTelephone", "电话号码已存在!!!");
            }
        }
        return erorrMap;
    }
    /**
     * 验证用户
     * @param username
     * @return
     */
    @CrossOrigin
    @GetMapping("/validateUserName")
    public YcBbsResult checkUserName(@RequestParam("username") String username) {
        //用户名判断
        if (null == username || "".equals(username.trim())) {
            return YcBbsResult.build(300,"用户名不能为空!!!");
        }
        else{
            UserInfo user = userInfoService.selectUserInfoByUserName(username.trim());
            if (null != user) {
                return YcBbsResult.build(300,"用户名已存在!!!");
            }
        }
        return YcBbsResult.build(200,"用户名可以使用!!!");
    }
    /**
     * 验证密码
     * @param password
     * @return
     */
    @CrossOrigin
    @GetMapping("/validatePassword")
    public YcBbsResult checkPassword(@RequestParam("password") String password) {
        //密码判断
        if (null == password || "".equals(password.trim())) {
            return YcBbsResult.build(300,"密码不能为空!!!");
        } else if (6 > password.trim().length()
                || password.trim().length() > 18) {
            return YcBbsResult.build(300,"密码必须为6~18位!!!");
        }
        return YcBbsResult.build(200,"密码可以使用!!!");
    }

    /**
     * 验证邮箱
     * @param email
     * @return
     */
    @CrossOrigin
    @GetMapping("/validateEmail")
    public YcBbsResult checkEmail(@RequestParam("email") String email) {
        //用户名判断
        if (null == email || "".equals(email.trim())) {
            return YcBbsResult.build(300,"email不能为空!!!");
        }
        else{
            boolean isEmail = userInfoService.selectByObject(email.trim(),0);
            if (isEmail) {
                return YcBbsResult.build(300,"email已被占用!!!");
            }
        }
        return YcBbsResult.build(200,"email可以使用!!!");
    }

    /**
     * 判断确认密码
     * @param password
     * @param confirmPassword
     * @return
     */
    @CrossOrigin
    @GetMapping("/validateConfirmPassWord")
    public YcBbsResult checkConfirmPassWord(@RequestParam("password") String password,
                                            @RequestParam("confirmPassword") String confirmPassword) {
        //判断确认密码
        if (!password.equals(confirmPassword.trim())) {
            return YcBbsResult.build(300,"确认密码与密码不匹配!!!");
        }
        return YcBbsResult.build(200,"确认密码验证通过!!!");
    }

    /**
     * 验证电话
     * @param telephone
     * @return
     */
    @CrossOrigin
    @GetMapping("/validateTelephone")
    public YcBbsResult checkTelephone(@RequestParam("telephone") String telephone) {
        //判断电话号码
        if (null == telephone || "".equals(telephone.trim())) {
            return YcBbsResult.build(300,"电话号码不能为空!!!");
        } else if (!AccountValidatorUtil.isMobile(telephone)) {
            return YcBbsResult.build(300,"电话号码格式错误!!!");
        } else {
            //判断手机号是否存在
            boolean isTelephone = userInfoService.selectByObject(telephone.trim(),1);
            if (isTelephone) {
                return YcBbsResult.build(300,"电话号码已存在!!!");
            }
        }
        return YcBbsResult.build(200,"电话可以使用!!!");
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
