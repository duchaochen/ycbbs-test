package com.ycbbs.crud.service;


import com.ycbbs.crud.entity.UserInfo;
import com.ycbbs.crud.exception.CustomException;

public interface LoginService {

    UserInfo selectUserInfoByUserName(String username);
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
}
