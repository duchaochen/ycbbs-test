package com.ycbbs.crud.service;


import com.ycbbs.crud.entity.ActiveUser;
import com.ycbbs.crud.entity.UserInfo;
import com.ycbbs.crud.exception.CustomException;

public interface UserInfoService {

    UserInfo selectUserInfoByUserName(String username);
}
