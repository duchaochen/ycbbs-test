package com.ycbbs.crud.controller.admin;

import com.ycbbs.crud.entity.PermissionInfo;
import com.ycbbs.crud.entity.querybean.SaveBeanRolePermission;
import com.ycbbs.crud.exception.CustomException;
import com.ycbbs.crud.pojo.YcBbsResult;
import com.ycbbs.crud.service.PermissionInfoService;
import com.ycbbs.crud.service.RoleInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/rolePermission")
@RestController
public class RolePermissionController {

    @Autowired
    private PermissionInfoService permissionInfoService;
    @Autowired
    private RoleInfoService roleInfoService;

    @CrossOrigin
    @RequestMapping(value="/getAuth",method = RequestMethod.GET)
//    @RequiresPermissions("rolePermission:getAuth")
    public YcBbsResult getAuthAll(String roleid) throws CustomException {
        if(null == roleid){
            return YcBbsResult.build(1000,"Token失效");
        }
        List<PermissionInfo> permissionList = permissionInfoService.getPermissionList(null);
        return YcBbsResult.build(200,"查询成功",permissionList);
    }
    @CrossOrigin
    @RequestMapping(value = "/saveAuth",method = RequestMethod.POST)
//    @RequiresPermissions("rolePermission:save")
    public YcBbsResult saveAuth(@RequestBody SaveBeanRolePermission saveBeanRolePermission) throws CustomException {
        boolean bool = roleInfoService.insertBatch(saveBeanRolePermission);
        if (bool) {
            return YcBbsResult.build(200,"保存成功");
        }
        return YcBbsResult.build(200,"保存失败");
    }
}
