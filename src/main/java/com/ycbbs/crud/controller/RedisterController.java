package com.ycbbs.crud.controller;

import com.ycbbs.crud.entity.UserInfo;
import com.ycbbs.crud.exception.CustomException;
import com.ycbbs.crud.pojo.YcBbsResult;
import com.ycbbs.crud.service.UserInfoService;
import com.ycbbs.crud.utils.AccountValidatorUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 注册控制器
 */
//@RequestMapping("/ycbbs")
@RestController
public class RedisterController {
    @Autowired
    private UserInfoService userInfoService;

    /**
     * 前台注册
     * @return
     * @throws CustomException
     */
    @CrossOrigin
    @PostMapping(value = {"/register"})
    public YcBbsResult register(@RequestBody UserInfo userInfo, HttpServletRequest request) throws CustomException {
        return insertUserInfo(userInfo,request);
    }

    /**
     * 后台添加
     * @param userInfo
     * @param request
     * @return
     * @throws CustomException
     */
    @CrossOrigin
    @PostMapping(value = {"/users/add"})
    @RequiresPermissions("user:create")
    public YcBbsResult addUserInfo(@RequestBody UserInfo userInfo, HttpServletRequest request) throws CustomException {
        return insertUserInfo(userInfo,request);
    }
    /**
     * 新增用户
     * @param userInfo
     * @param request
     * @return
     */
    private YcBbsResult insertUserInfo(UserInfo userInfo, HttpServletRequest request){
        String path = request.getRequestURI();
        System.out.println(userInfo);
        //用户检查，如果注册信息不符合信息的将存进map中
        Map<String, String> stringStringMap = this.checkUserInfo(userInfo);
        if (stringStringMap.size() > 0) {
            return YcBbsResult.build(300,"注册失败",stringStringMap);
        }

        boolean isSave = userInfoService.insertUserInfo(userInfo,path);
        if (!isSave) {
            return YcBbsResult.build(500, "内部错误:注册失败", stringStringMap);
        }
        return YcBbsResult.build(200,"注册成功,5秒内跳转到登录页面!!!",stringStringMap);
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
            //注册时oldUserName可以直接为空
            UserInfo user = userInfoService.selectUserInfoByUserName("",userInfo.getUsername().trim());
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
        //密码不一致
        if (null == userInfo.getConfirmPassword() || !userInfo.getConfirmPassword().trim().equals(userInfo.getPassword())) {
            erorrMap.put("erorrConfirmPassword","确认密码与密码不一致!!!");
        }
        //判断邮箱
        if (null == userInfo.getEmail() || "".equals(userInfo.getEmail().trim())) {
            erorrMap.put("erorrEmail","email不能为空!!!");
        } else if (!AccountValidatorUtil.isEmail(userInfo.getEmail())) {
            erorrMap.put("erorrEmail", "email格式不对!!!");
        } else {
            //判断email是否存在
            boolean isEmail = userInfoService.selectByObject("",userInfo.getEmail().trim(),0);
            if (isEmail) {
                erorrMap.put("erorrEmail", "该email已经被占用!!!");
            }
        }
//        //判断电话号码
//        if (null == userInfo.getTelephone() || "".equals(userInfo.getTelephone().trim())) {
//            erorrMap.put("erorrTelephone", "电话号码不能为空!!!");
//        } else if (!AccountValidatorUtil.isMobile(userInfo.getTelephone())) {
//            erorrMap.put("erorrTelephone", "电话号码格式错误!!!");
//        } else {
//            //判断手机号是否存在
//            boolean isTelephone = userInfoService.selectByObject(userInfo.getTelephone().trim(),1);
//            if (isTelephone) {
//                erorrMap.put("erorrTelephone", "电话号码已存在!!!");
//            }
//        }
        return erorrMap;
    }

    /**
     * 管理员后台修改验证去除自己之后是否重名
     * @param oldUserName:自己目前的用户名
     * @param validaNewUserName:需要验证的用户名
     * @return
     */
    @CrossOrigin
    @GetMapping("/validateUserName")
    public YcBbsResult checkUserName(String oldUserName,String validaNewUserName) {
        //用户名判断
        if (null == validaNewUserName || "".equals(validaNewUserName.trim())) {
            return YcBbsResult.build(300,"用户名不能为空!!!");
        }
        else{
            UserInfo user = userInfoService.selectUserInfoByUserName(oldUserName,validaNewUserName);
            if (null != user) {
                return YcBbsResult.build(301,"用户名被占用!!!");
            }
        }
        return YcBbsResult.build(200,"用户名可以使用!!!");
    }

    /**
     * 验证邮箱
     * @param oldEmail
     * @param validaNewEmail
     * @return
     */
    @CrossOrigin
    @GetMapping("/validateEmail")
    public YcBbsResult checkEmail(String oldEmail,String validaNewEmail) {
        //用户名判断
        if (null == validaNewEmail || "".equals(validaNewEmail.trim())) {
            return YcBbsResult.build(300,"email不能为空!!!");
        }
        else{
            boolean isEmail = userInfoService.selectByObject(oldEmail,validaNewEmail,0);
            if (isEmail) {
                return YcBbsResult.build(300,"email已被占用!!!");
            }
        }
        return YcBbsResult.build(200,"email可以使用!!!");
    }
    /**
     * 验证手机号是否存在,可以和前台注册共用此验证
     * @param oldTelephone : 原始邮箱/手机号
     * @param validaTelephone  ： 新邮箱/手机号
     * @return
     */
    @CrossOrigin
    @GetMapping("/validateTelephone")
    public YcBbsResult checkTelephone(String oldTelephone,String validaTelephone) {
        //判断电话号码
        if (null == validaTelephone || "".equals(validaTelephone.trim())) {
            return YcBbsResult.build(300,"电话号码不能为空!!!");
        } else if (!AccountValidatorUtil.isMobile(validaTelephone)) {
            return YcBbsResult.build(300,"电话号码格式错误!!!");
        } else {
            //判断手机号是否存在
            boolean isTelephone = userInfoService.selectByObject(oldTelephone,validaTelephone,1);
            if (isTelephone) {
                return YcBbsResult.build(300,"电话号码被占用!!!");
            }
        }
        return YcBbsResult.build(200,"电话可以使用!!!");
    }
}
