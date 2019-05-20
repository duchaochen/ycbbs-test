package com.ycbbs.crud.controller.admin;

import com.ycbbs.crud.entity.RoleInfo;
import com.ycbbs.crud.entity.querybean.SaveBeanUserInfoRole;
import com.ycbbs.crud.exception.CustomException;
import com.ycbbs.crud.pojo.YcBbsResult;
import com.ycbbs.crud.service.RoleInfoService;
import com.ycbbs.crud.service.UserInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/ycbbs/userRole")
@RestController
public class UserInfoRoleInfoController {

    @Autowired
    private RoleInfoService roleInfoService;
    @Autowired
    private UserInfoService userInfoService;

    /**
     * 获取所有角色
     * @param uid
     * @return
     * @throws CustomException
     */
    @CrossOrigin
    @RequestMapping(value="/getRoles",method = RequestMethod.GET)
    public YcBbsResult getRoleAll(String uid) throws CustomException {
        if(null == uid){
            return YcBbsResult.build(1000,"Token失效");
        }
        List<RoleInfo> permissionList = roleInfoService.getRoleInfoList(null);
        return YcBbsResult.build(200,"查询成功",permissionList);
    }

    /**
     * 用户角色添加与修改
     * @param saveBeanUserInfoRole
     * @return
     * @throws CustomException
     */
    @CrossOrigin
    @RequestMapping(value = "/saveRole",method = RequestMethod.POST)
    @RequiresPermissions("userRole:save")
    public YcBbsResult saveRole(@RequestBody SaveBeanUserInfoRole saveBeanUserInfoRole) throws CustomException {
        boolean bool = userInfoService.insertBatch(saveBeanUserInfoRole);
        if (bool) {
            return YcBbsResult.build(200,"保存成功");
        }
        return YcBbsResult.build(200,"保存失败");
    }
}
