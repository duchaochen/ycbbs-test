package com.ycbbs.crud.mapper;

import com.ycbbs.crud.entity.RoleInfo;
import com.ycbbs.crud.entity.RolePermissionInfo;
import com.ycbbs.crud.entity.querybean.QueryBeanRoleInfo;
import com.ycbbs.crud.exception.CustomException;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface RoleInfoMapper extends Mapper<RoleInfo> {
    /**
     * 批量删除角色权限
     * @param roleid
     * @return
     * @throws CustomException
     */
    int deleteBatch(String roleid) throws CustomException;
    /**
     * 批量插入角色权限
     * @param rolePermissionInfos
     * @return
     * @throws CustomException
     */
    int insertBatch(List<RolePermissionInfo> rolePermissionInfos) throws CustomException;

    /**
     * 获取对应角色的权限
     * @param queryBeanRoleInfo
     * @return
     * @throws CustomException
     */
    List<RoleInfo> getRolePermissionList(QueryBeanRoleInfo queryBeanRoleInfo)throws CustomException;
}
