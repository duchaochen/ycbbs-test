package com.ycbbs.crud.mapper;

import com.ycbbs.crud.entity.PermissionInfo;
import com.ycbbs.crud.exception.CustomException;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PermissionInfoMapper extends Mapper<PermissionInfo> {

    /**
     * 获取menu菜单
     * @param username
     * @return
     * @throws Exception
     */
    List<PermissionInfo> getMenuInfo(String username) throws CustomException;

    /**
     * 获取可操作权限
     * @param username
     * @return
     * @throws Exception
     */
    List<PermissionInfo> getPermissionInfo(String username) throws CustomException;
}
