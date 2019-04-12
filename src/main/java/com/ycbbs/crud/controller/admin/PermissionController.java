package com.ycbbs.crud.controller.admin;

import com.github.pagehelper.PageInfo;
import com.ycbbs.crud.entity.PermissionInfo;
import com.ycbbs.crud.entity.querybean.QueryBeanPermission;
import com.ycbbs.crud.pojo.YcBbsResult;
import com.ycbbs.crud.service.PermissionInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/permission")
@RestController
public class PermissionController {

    @Autowired
    private PermissionInfoService permissionInfoService;

    @CrossOrigin
    @GetMapping("/menuList")
    public YcBbsResult queryPermissionMenu() throws Exception {
        List<PermissionInfo> permissionList = permissionInfoService.getPermissionMenuList();
        return YcBbsResult.build(200,"查询成功",permissionList);
    }

    @CrossOrigin
    @GetMapping("/list")
//    @RequiresPermissions("permission:query")
    public PageInfo<PermissionInfo> queryPermission(QueryBeanPermission queryBeanPermission) throws Exception {
        PageInfo<PermissionInfo> permissionKeyList = permissionInfoService.getPermissionKeyList(queryBeanPermission);
        return permissionKeyList;
    }

    @CrossOrigin
    @PostMapping("/add")
//    @RequiresPermissions("permission:create")
    public YcBbsResult addPermission(@RequestBody PermissionInfo permissionInfo) throws Exception {
        boolean b = permissionInfoService.insertPermission(permissionInfo);
        if (b) {
            return YcBbsResult.build(200,"新增成功");
        }
        return YcBbsResult.build(200,"新增失败");
    }

    @CrossOrigin
    @PostMapping("/update")
//    @RequiresPermissions("permission:update")
    public YcBbsResult updatePermission(PermissionInfo permissionInfo) throws Exception {
        if(null == permissionInfo.getId()){
            return YcBbsResult.build(500,"无法找到要更新的对应项!!!");
        }
        boolean b = permissionInfoService.updatePermission(permissionInfo);
        if (b) {
            return YcBbsResult.build(200,"更新成功");
        }
        return YcBbsResult.build(200,"更新失败");
    }

    @CrossOrigin
    @GetMapping("/delete")
//    @RequiresPermissions("permission:delete")
    public YcBbsResult deletePermission(PermissionInfo permissionInfo) throws Exception {
        if(null == permissionInfo.getId()){
            return YcBbsResult.build(500,"无法找到要删除的对应项!!!");
        }
        boolean b = permissionInfoService.updatePermission(permissionInfo);
        if (b) {
            return YcBbsResult.build(200,"删除成功");
        }
        return YcBbsResult.build(200,"删除失败");
    }

}
