package com.ycbbs.crud.service;


import com.github.pagehelper.PageInfo;
import com.ycbbs.crud.entity.UserInfo;
import com.ycbbs.crud.entity.querybean.QueryBeanUserInfo;
import com.ycbbs.crud.exception.CustomException;

import java.util.List;

public interface UserInfoService {
    /**
     * 验证用户名是否存在(还要过滤自己原始用户名)，可以和前台注册共用此验证
     * 登录时来根据用户名查询实体，直接将oldUserName为空，validaUserName值为当前用户名就可以查询到数据了
     * @param oldUserName:原始用户名
     * @param validaNewUserName:新用户名
     * @return
     */
    UserInfo selectUserInfoByUserName(String oldUserName,String validaNewUserName);
    /**
     * 新增用户信息
     * @param userInfo
     * @return
     * @throws CustomException
     */
    boolean insertUserInfo(UserInfo userInfo,String path) throws CustomException;
    /**
     * 验证邮箱/手机号是否存在(还要过滤自己原始邮箱/手机号)，可以和前台注册共用此验证
     * @param oldEmailorPhone : 原始邮箱/手机号
     * @param validaNewEmailorPhone  ： 新邮箱/手机号
     * @param status  : 0 表示邮箱，1表示电话
     * @return
     */
    boolean selectByObject(String oldEmailorPhone,String validaNewEmailorPhone,int status);
    /**
     * 激活
     * @param uid, code
     * @return
     */
    boolean updateActiveCode(String uid, String code) throws CustomException;
    /**
     * 获取所有用户
     * @param queryBeanUserInfo
     * @return
     */
    PageInfo<UserInfo> selectKeyAll(QueryBeanUserInfo queryBeanUserInfo);
    /**
     * 获取所有用户
     * @param queryBeanUserInfo
     * @return
     */
    List<UserInfo> selectAll(QueryBeanUserInfo queryBeanUserInfo);
    /**
     * 修改用户(软删除)
     * @param userInfo
     */
    boolean updateUserInfo(UserInfo userInfo);
}
