package com.ycbbs.crud.controller.admin;

import com.github.pagehelper.PageInfo;
import com.ycbbs.crud.entity.UserInfo;
import com.ycbbs.crud.entity.querybean.UserInfoQueryBean;
import com.ycbbs.crud.exception.CustomException;
import com.ycbbs.crud.pojo.YcBbsResult;
import com.ycbbs.crud.service.UserInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;
    /**
     * 查询用户列表
     * @return
     * @throws CustomException
     */
    @CrossOrigin
    @GetMapping("/list")
    @RequiresPermissions("user:query")
    public PageInfo<UserInfo> queryUserInfoList(UserInfoQueryBean userInfoQueryBean) throws CustomException {
        PageInfo<UserInfo> pageInfo = userInfoService.selectKeyAll(userInfoQueryBean);
        return pageInfo;
    }
    /**
     * 更新用户
     * @param userInfo
     * @return
     */
    @CrossOrigin
    @PostMapping("/update")
    @RequiresPermissions("user:update")
    public YcBbsResult eidtUserInfo(@RequestBody UserInfo userInfo) {
        return updateUserInfo(userInfo,"更新");
    }
    /**
     * 软删除即为修改
     * @param userInfo
     * @return
     */
    @CrossOrigin
    @PostMapping("/delete")
    @RequiresPermissions("user:delete")
    public YcBbsResult deleteUserInfo(@RequestBody UserInfo userInfo) {
       return updateUserInfo(userInfo,"删除");
    }

    /**
     * 根据msg显示对应的删除/更新信息
     * @param userInfo
     * @param msg
     * @return
     */
    private YcBbsResult updateUserInfo(UserInfo userInfo,String msg){
        //修改时不更新密码
        userInfo.setPassword(null);
        boolean b = userInfoService.updateUserInfo(userInfo);
        if(b){
            return YcBbsResult.build(200,msg + "成功!");
        }else{
            return YcBbsResult.build(500,msg +"失败!");
        }
    }

}
