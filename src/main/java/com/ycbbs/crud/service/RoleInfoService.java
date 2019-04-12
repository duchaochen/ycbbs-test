package com.ycbbs.crud.service;

import com.github.pagehelper.PageInfo;
import com.ycbbs.crud.entity.RoleInfo;
import com.ycbbs.crud.entity.querybean.QueryBeanRoleInfo;
import com.ycbbs.crud.entity.querybean.SaveBeanRolePermission;
import com.ycbbs.crud.exception.CustomException;

import java.util.List;

public interface RoleInfoService {

    /**
     * 获取查询数据（带分页）
     * @param queryBeanRoleInfo
     * @return
     * @throws CustomException
     */
    PageInfo<RoleInfo> getRoleInfoPageList(QueryBeanRoleInfo queryBeanRoleInfo)throws CustomException;
    /**
     * 获取查询数据
     * @param queryBeanRoleInfo
     * @return
     * @throws CustomException
     */
    List<RoleInfo> getRoleInfoList(QueryBeanRoleInfo queryBeanRoleInfo)throws CustomException;
    /**
     * 添加角色数据
     * @param roleInfo
     * @return
     * @throws CustomException
     */
    boolean insertRoleInfo(RoleInfo roleInfo) throws CustomException;
    /**
     * 修改权限数据(包括删除)
     * @param roleInfo
     * @return
     * @throws CustomException
     */
    boolean updateRoleInfo(RoleInfo roleInfo) throws CustomException;

    RoleInfo selectRoleInfoByUserName(String oldRoleName, String validaNewRoleName);

    /**
     * 批量删除角色权限
     * @param roleid
     * @return
     * @throws CustomException
     */
    boolean deleteBatch(String roleid) throws CustomException;
    /**
     * 批量插入权限
     * @param saveBeanRolePermission
     * @return
     * @throws CustomException
     */
    boolean insertBatch(SaveBeanRolePermission saveBeanRolePermission) throws CustomException;
}
