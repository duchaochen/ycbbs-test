package com.ycbbs.crud.service.impl;

import com.ycbbs.crud.entity.UserInfo;
import com.ycbbs.crud.exception.CustomException;
import com.ycbbs.crud.mapper.UserInfoMapper;
import com.ycbbs.crud.service.UserInfoService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
    /**
     * 添加数据
     * @param userInfo
     * @return
     */
    @Override
    public boolean insertUserInfo(UserInfo userInfo) throws CustomException {
        //用来生成数据库的主键id非常不错。
        String uuid = UUID.randomUUID().toString();
        userInfo.setUid(uuid);
        userInfo.setSalt(uuid);
        userInfo.setState("0");
        userInfo.setCode(uuid.substring(0, 8));
        Md5Hash md5Hash = new Md5Hash(userInfo.getPassword(),userInfo.getSalt(),1);
        String password = md5Hash.toString();
        //将加密后的密码存入数据库
        userInfo.setPassword(password);
        userInfo.setLocked("0");
        int rows = userInfoMapper.insertSelective(userInfo);
        if (rows == 0) {
            throw new CustomException("系统错误，注册失败!!!");
        }
        return true;
    }

    /**
     * 邮箱判断
     * @param str
     * @param status 0:邮箱，1：手机号
     * @return
     */
    @Override
    public boolean selectByObject(String str,int status) {
        UserInfo u = this.getUserinfo(str,status);
        int count = userInfoMapper.selectCount(u);
        if (count > 0) {
            return true;
        }
        return false;
    }

    /**
     * 根据状态来为userinfo赋值
     * @param str
     * @param status
     * @return
     */
    private UserInfo getUserinfo(String str,int status) {
        UserInfo u = new UserInfo();
        if (0 == status) {
            u.setEmail(str);
        } else if (1 == status) {
            u.setTelephone(str);
        }
        return u;
    }
}
