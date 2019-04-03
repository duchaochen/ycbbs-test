package com.ycbbs.crud.shiro.realm;


import com.ycbbs.crud.entity.ActiveUser;
import com.ycbbs.crud.entity.PermissionInfo;
import com.ycbbs.crud.entity.UserInfo;
import com.ycbbs.crud.service.PermissionInfoService;
import com.ycbbs.crud.service.UserInfoService;
import com.ycbbs.crud.shiro.jwt.JWTToken;
import com.ycbbs.crud.shiro.jwt.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@SuppressWarnings("all")
@Slf4j
public class AuthRealm extends AuthorizingRealm {

    @Autowired
    private PermissionInfoService permissionInfoService;

    @Autowired
    private UserInfoService userInfoService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * @Title: doGetAuthenticationInfo
     * @Description: 获取认证信息
     * @return    返回类型
     * @throws
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        // 解密获得userName，用于和数据库对比
        String userName = JWTUtil.getUserName(token);
        if (userName == null ) {
            throw new AuthenticationException("token不存在!!!");
        }
        UserInfo userInfo = userInfoService.selectUserInfoByUserName(userName);
        if (userInfo == null ) {
            throw new AuthenticationException("账户不存在!!!");
        }
        //验证用户名和密码
        if ( !JWTUtil.verify(token,userName,userInfo.getPassword())){
            throw new AuthenticationException("账户不存在!!!");
        }
        //账号未激活
        if ("0".equals(userInfo.getState())) {
            throw new AuthenticationException("账号未激活,请尽快去绑定的邮箱激活!!!");
        }
        ActiveUser activeUser = null;
        try {
            //开始封装权限
            activeUser = new ActiveUser();
            //查询菜单
            List<PermissionInfo> menuInfo = permissionInfoService.getMenuInfo(userInfo.getUid());
            activeUser.setUsername(userInfo.getUsername());
            activeUser.setRealname(userInfo.getRealname());
//            activeUser.setPassword(userInfo.getPassword());
            activeUser.setMenus(menuInfo);
            activeUser.setUserid(userInfo.getUid());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new SimpleAuthenticationInfo(activeUser,token,token);
    }

    /**
     * @Title: doGetAuthorizationInfo
     * @Description: 获取授权信息
     * @return    返回类型
     * @throws
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 获取用户名
        Object principal =  principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        if(principal instanceof ActiveUser) {
            // 根据用户名去查询用户信息
            ActiveUser activeUser = (ActiveUser)principal;
            //查询权限
            List<PermissionInfo> permissionInfos = null;
            try {
                permissionInfos = permissionInfoService.getPermissionInfo(activeUser.getUserid());
                for (PermissionInfo permissionInfo : permissionInfos) {
                    //将查询到授权信息填充到simpleAuthorizationInfo对象中,授权时使用
                    simpleAuthorizationInfo.addStringPermission(permissionInfo.getPercode());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return simpleAuthorizationInfo;
    }
}

