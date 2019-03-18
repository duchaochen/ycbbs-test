package com.ycbbs.crud.realm;

import com.ycbbs.crud.entity.ActiveUser;
import com.ycbbs.crud.entity.PermissionInfo;
import com.ycbbs.crud.entity.UserInfo;
import com.ycbbs.crud.service.LoginService;
import com.ycbbs.crud.service.PermissionInfoService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private PermissionInfoService permissionInfoService;
    @Autowired
    private LoginService userInfoService;
    /**
     * Realm唯一名称
     * @return
     */
    @Override
    public String getName() {
        return "myrealm1";
    }
    /**
     * 用户认证
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        ActiveUser activeUser = null;
        //判断用户是否存在
        UserInfo userInfo = userInfoService.selectUserInfoByUserName(username);
        if (userInfo == null) {
            return null;
        }
        try {
            //开始封装权限
            activeUser = new ActiveUser();
            //查询菜单
            List<PermissionInfo> menuInfo = permissionInfoService.getMenuInfo(userInfo.getUid());
            activeUser.setUsername(userInfo.getUsername());
            activeUser.setRealname(userInfo.getRealname());
            activeUser.setMenus(menuInfo);
            activeUser.setUserid(userInfo.getUid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //加盐和用户输入的密码md5加密后来和password认证匹配
        return new SimpleAuthenticationInfo(activeUser,userInfo.getPassword(),
                ByteSource.Util.bytes(userInfo.getSalt()), this.getName());
    }
    /**
     * 用户授权
     * @param principal
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        //获取身份信息
        ActiveUser activeUser = (ActiveUser) principal.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        try {
            //查询权限
            List<PermissionInfo> permissionInfos = permissionInfoService.getPermissionInfo(activeUser.getUserid());
            for (PermissionInfo permissionInfo : permissionInfos) {
                //将查询到授权信息填充到simpleAuthorizationInfo对象中,授权时使用
                simpleAuthorizationInfo.addStringPermission(permissionInfo.getPercode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return simpleAuthorizationInfo;
    }

    /**
     * 清除缓存
     */
    public void clearCached() {
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        super.clearCache(principals);
    }
}
