package com.ycbbs.crud.service;

import com.ycbbs.crud.entity.PermissionInfo;

import java.util.List;

public interface PermissionInfoService{

    /**
     * 获取menu菜单
     * @param uid
     * @return
     * @throws Exception
     */
    List<PermissionInfo> getMenuInfo(String uid) throws Exception;

    /**
     * 获取可操作权限
     * @param uid
     * @return
     * @throws Exception
     */
    List<PermissionInfo> getPermissionInfo(String uid) throws Exception;

}
