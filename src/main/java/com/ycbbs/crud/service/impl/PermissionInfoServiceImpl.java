package com.ycbbs.crud.service.impl;

import com.ycbbs.crud.entity.PermissionInfo;
import com.ycbbs.crud.mapper.PermissionInfoMapper;
import com.ycbbs.crud.realm.CustomRealm;
import com.ycbbs.crud.service.PermissionInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionInfoServiceImpl implements PermissionInfoService {
    @Autowired
    private PermissionInfoMapper permissionInfoMapper;
    /**
     * 获取menu菜单
     * @param uid
     * @return
     * @throws Exception
     */
    @Override
    public List<PermissionInfo> getMenuInfo(String uid) throws Exception {
        return permissionInfoMapper.getMenuInfo(uid);
    }
    /**
     * 获取可操作权限
     * @param uid
     * @return
     * @throws Exception
     */
    @Override
    public List<PermissionInfo> getPermissionInfo(String uid) throws Exception {
        return permissionInfoMapper.getPermissionInfo(uid);
    }
}
