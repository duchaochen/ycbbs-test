package com.ycbbs.crud.controller;

import com.ycbbs.crud.entity.UserInfo;
import com.ycbbs.crud.exception.CustomException;
import com.ycbbs.crud.pojo.YcBbsResult;
import com.ycbbs.crud.service.UserInfoService;
import com.ycbbs.crud.utils.AccountValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 注册控制器
 */
@RestController
public class RedisterController {
    @Autowired
    private UserInfoService userInfoService;
    /**
     * 注册
     * @return
     * @throws CustomException
     */
    @CrossOrigin
    @PostMapping(value = {"/register","/users/add"})
    public YcBbsResult register(@RequestBody UserInfo userInfo, HttpServletRequest request) throws CustomException {

        String path = request.getRequestURI();
        System.out.println(userInfo);
        //用户检查，如果注册信息不符合信息的将存进map中
        Map<String, String> stringStringMap = this.checkUserInfo(userInfo);
        if (stringStringMap.size() > 0) {
            return YcBbsResult.build(300,"注册失败",stringStringMap);
        }
        //暂时设置为全部都是激活状态,如果实行激活操作这里直接去掉
        if (!"/register".equals(path)) {
            userInfo.setState("1");
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
            boolean isEmail = userInfoService.selectByObject(userInfo.getEmail().trim(),0);
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
     * 验证用户
     * @param username
     * @return
     */
    @CrossOrigin
    @GetMapping("/validateUserName")
    public YcBbsResult checkUserName(@RequestParam("value") String username) {
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
     * 验证邮箱
     * @param email
     * @return
     */
    @CrossOrigin
    @GetMapping("/validateEmail")
    public YcBbsResult checkEmail(@RequestParam("value") String email) {
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
     * 验证电话
     * @param telephone
     * @return
     */
    @CrossOrigin
    @GetMapping("/validateTelephone")
    public YcBbsResult checkTelephone(@RequestParam("value") String telephone) {
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
}
