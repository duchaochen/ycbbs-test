package com.ycbbs.crud.shiro.jwt;


import org.apache.shiro.authc.AuthenticationToken;

/**
 * @Title: JWTToken.java
 * @Package com.tisson.demo.auth
 * @Description: TODO(用一句话描述该文件做什么)
 * @author Joker1995
 * @date 2018年11月8日
 * @version V1.0
 */
public class JWTToken implements AuthenticationToken{
    private static final long serialVersionUID = 1L;
    // 密钥
    private String token;

    public JWTToken(String token) {
        this.token=token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
