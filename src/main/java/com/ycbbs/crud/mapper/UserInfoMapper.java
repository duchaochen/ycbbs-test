package com.ycbbs.crud.mapper;

import com.ycbbs.crud.entity.UserInfo;
import com.ycbbs.crud.entity.UserRoleInfo;
import com.ycbbs.crud.entity.querybean.QueryBeanUserInfo;
import com.ycbbs.crud.exception.CustomException;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserInfoMapper extends Mapper<UserInfo> {
    /**
     * 获取用户对应角色
     * @param queryBeanUserInfo
     * @return
     * @throws CustomException
     */
    List<UserInfo> getUserRoleList(QueryBeanUserInfo queryBeanUserInfo)throws CustomException;
    /**
     * 删除角色
     * @param uid
     * @return
     */
    int deleteBatch(String uid);
    /**
     * 添加用户角色
     * @param userRoleInfos
     * @return
     */
    int insertBatch(List<UserRoleInfo> userRoleInfos);
}
