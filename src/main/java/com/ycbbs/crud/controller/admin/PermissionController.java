package com.ycbbs.crud.controller.admin;

import com.github.pagehelper.PageInfo;
import com.ycbbs.crud.entity.PermissionInfo;
import com.ycbbs.crud.entity.querybean.QueryBeanPermission;
import com.ycbbs.crud.pojo.YcBbsResult;
import com.ycbbs.crud.service.PermissionInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
///ycbbs
@RequestMapping("/permission")
@RestController
public class PermissionController {

    @Autowired
    private PermissionInfoService permissionInfoService;

    /**
     * 获取权限目录
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @GetMapping("/menuList")
    public YcBbsResult queryPermissionMenu() throws Exception {
        List<PermissionInfo> permissionList = permissionInfoService.getPermissionMenuList();
        return YcBbsResult.build(200,"查询成功",permissionList);
    }

    /**
     * 获取所有权限
     * @param queryBeanPermission
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @GetMapping("/list")
    @RequiresPermissions("permission:query")
    public PageInfo<PermissionInfo> queryPermission(QueryBeanPermission queryBeanPermission) throws Exception {
        PageInfo<PermissionInfo> permissionKeyList = permissionInfoService.getPermissionKeyList(queryBeanPermission);
        return permissionKeyList;
    }

    /**
     * 添加权限
     * @param permissionInfo
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @PostMapping("/add")
    @RequiresPermissions("permission:create")
    public YcBbsResult addPermission(@RequestBody PermissionInfo permissionInfo) throws Exception {
        boolean b = permissionInfoService.insertPermission(permissionInfo);
        if (b) {
            return YcBbsResult.build(200,"新增成功");
        }
        return YcBbsResult.build(200,"新增失败");
    }

    /**
     * 更新权限
     * @param permissionInfo
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @PostMapping("/update")
    @RequiresPermissions("permission:update")
    public YcBbsResult updatePermission(@RequestBody PermissionInfo permissionInfo) throws Exception {
        if(null == permissionInfo.getId()){
            return YcBbsResult.build(500,"无法找到要更新的对应项!!!");
        }
        boolean b = permissionInfoService.updatePermission(permissionInfo);
        if (b) {
            return YcBbsResult.build(200,"更新成功");
        }
        return YcBbsResult.build(200,"更新失败");
    }

    /**
     * 删除权限
     * @param permissionInfo
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @GetMapping("/delete")
    @RequiresPermissions("permission:delete")
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
