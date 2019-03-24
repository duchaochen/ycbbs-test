package com.ycbbs.crud.service;


import com.github.pagehelper.PageInfo;
import com.ycbbs.crud.entity.UserInfo;
import com.ycbbs.crud.exception.CustomException;

import java.util.List;

public interface UserInfoService {
    /**
     * 验证用户名是否存在
     * @param username
     * @return
     */
    UserInfo selectUserInfoByUserName(String username);
    /**
     * 新增用户信息
     * @param userInfo
     * @return
     * @throws CustomException
     */
    boolean insertUserInfo(UserInfo userInfo) throws CustomException;
    /**
     *
     * @param email
     * @param status
     * @return
     */
    boolean selectByObject(String email,int status);
    /**
     * 激活
     * @param uid, code
     * @return
     */
    boolean updateActiveCode(String uid, String code) throws CustomException;
    /**
     * 获取所有用户
     * @param keyname
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<UserInfo> selectKeyAll(String keyname, int pageNum, int pageSize);
    /**
     * 获取所有用户
     * @param keyname
     * @return
     */
    List<UserInfo> selectKeyAll(String keyname);
}
