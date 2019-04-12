package com.ycbbs.crud.controller.admin;

import com.github.pagehelper.PageInfo;
import com.ycbbs.crud.entity.RoleInfo;
import com.ycbbs.crud.entity.querybean.QueryBeanRoleInfo;
import com.ycbbs.crud.pojo.YcBbsResult;
import com.ycbbs.crud.service.RoleInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/role")
@RestController
public class RoleInfoController {

    @Autowired
    private RoleInfoService roleInfoService;

    @CrossOrigin
    @GetMapping("/list")
//    @RequiresPermissions("role:query")
    public PageInfo<RoleInfo> queryRole(QueryBeanRoleInfo queryBeanRoleInfo) throws Exception {
        PageInfo<RoleInfo> roleInfoKeyList = roleInfoService.getRoleInfoPageList(queryBeanRoleInfo);
        return roleInfoKeyList;
    }

    /**
     * 管理员后台修改验证去除自己之后是否重名
     * @param oldRoleName:自己目前的用户名
     * @param validaNewRoleName:需要验证的用户名
     * @return
     */
    @CrossOrigin
    @GetMapping("/validateRoleName")
    public YcBbsResult checkUserName(String oldRoleName,String validaNewRoleName) {
        //用户名判断
        if (null == validaNewRoleName || "".equals(validaNewRoleName.trim())) {
            return YcBbsResult.build(300,"角色名称不能为空!!!");
        }
        else{
            RoleInfo user = roleInfoService.selectRoleInfoByUserName(oldRoleName,validaNewRoleName);
            if (null != user) {
                return YcBbsResult.build(301,"用户名被占用!!!");
            }
        }
        return YcBbsResult.build(200,"用户名可以使用!!!");
    }

    @CrossOrigin
    @PostMapping("/add")
//    @RequiresPermissions("role:create")
    public YcBbsResult addPermission(@RequestBody RoleInfo roleInfo) throws Exception {
        boolean b = roleInfoService.insertRoleInfo(roleInfo);
        if (b) {
            return YcBbsResult.build(200,"新增成功");
        }
        return YcBbsResult.build(200,"新增失败");
    }

    @CrossOrigin
    @PostMapping("/update")
//    @RequiresPermissions("role:update")
    public YcBbsResult updatePermission(@RequestBody RoleInfo roleInfo) throws Exception {
        if(null == roleInfo.getId()){
            return YcBbsResult.build(500,"无法找到要更新的对应项!!!");
        }
        boolean b = roleInfoService.updateRoleInfo(roleInfo);
        if (b) {
            return YcBbsResult.build(200,"更新成功");
        }
        return YcBbsResult.build(200,"更新失败");
    }

    @CrossOrigin
    @GetMapping("/delete")
//    @RequiresPermissions("role:delete")
    public YcBbsResult deletePermission(RoleInfo roleInfo) throws Exception {
        if(null == roleInfo.getId()){
            return YcBbsResult.build(500,"无法找到要删除的对应项!!!");
        }
        boolean b = roleInfoService.updateRoleInfo(roleInfo);
        if (b) {
            return YcBbsResult.build(200,"删除成功");
        }
        return YcBbsResult.build(200,"删除失败");
    }
}
