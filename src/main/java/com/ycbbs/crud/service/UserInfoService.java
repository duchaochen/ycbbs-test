package com.ycbbs.crud.service;


import com.ycbbs.crud.entity.UserInfo;
import com.ycbbs.crud.exception.CustomException;

public interface UserInfoService {

    UserInfo selectUserInfoByUserName(String username);
    boolean insertUserInfo(UserInfo userInfo) throws CustomException;

    /**
     *
     * @param email
     * @param status
     * @return
     */
    boolean selectByObject(String email,int status);
}
