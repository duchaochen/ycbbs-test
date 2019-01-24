package com.ycbbs.crud.service.impl;

import com.ycbbs.crud.entity.UserInfo;
import com.ycbbs.crud.mapper.UserInfoMapper;
import com.ycbbs.crud.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;
    /**
     * 根据用户名查询
     * @param username
     * @return
     */
    @Override
    public UserInfo selectUserInfoByUserName(String username) {
        UserInfo user = new UserInfo();
        user.setUsername(username);
        return userInfoMapper.selectOne(user);
    }
}
