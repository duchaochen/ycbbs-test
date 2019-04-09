package com.ycbbs.crud.service;

import com.github.pagehelper.PageInfo;
import com.ycbbs.crud.entity.PermissionInfo;
import com.ycbbs.crud.entity.querybean.PermissionQueryBean;
import com.ycbbs.crud.exception.CustomException;

import java.util.List;

public interface PermissionInfoService{

    /**
     * 获取menu菜单
     * @param uid
     * @return
     * @throws Exception
     */
    List<PermissionInfo> getMenuInfo(String uid) throws CustomException;
    /**
     * 获取可操作权限
     * @param uid
     * @return
     * @throws Exception
     */
    List<PermissionInfo> getPermissionInfo(String uid) throws CustomException;
    /**
     * 获取查询数据（带分页）
     * @param permissionQueryBean
     * @return
     * @throws CustomException
     */
    PageInfo<PermissionInfo> getPermissionKeyList(PermissionQueryBean permissionQueryBean)throws CustomException;
    /**
     * 获取查询数据
     * @param permissionQueryBean
     * @return
     * @throws CustomException
     */
    List<PermissionInfo> getPermissionList(PermissionQueryBean permissionQueryBean)throws CustomException;
    /**
     * 获取所有menuList
     * @return
     * @throws CustomException
     */
    List<PermissionInfo> getPermissionMenuList()throws CustomException;
    /**
     * 添加权限数据
     * @param permissionInfo
     * @return
     * @throws CustomException
     */
    boolean insertPermission(PermissionInfo permissionInfo) throws CustomException;
    /**
     * 修改权限数据(包括删除)
     * @param permissionInfo
     * @return
     * @throws CustomException
     */
    boolean updatePermission(PermissionInfo permissionInfo) throws CustomException;
}
