package com.ycbbs.crud.shiro.jwt;


import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
/**
 * @Title: JWTUtil.java
 * @Package com.tisson.demo.auth
 * @Description: TODO(用一句话描述该文件做什么)
 * @author Joker1995
 * @date 2018年11月8日
 * @version V1.0
 */
public class JWTUtil {
    // 过期时间30分钟
    public static final long EXPIRE_TIME = 30*60*1000;
//	public static final long EXPIRE_TIME = 60*1000;

    /**
     *  校验token是否正确
     * @param token 密钥
     * @param userName 用户名
     * @param password 用户密码
     * @return 是否正确
     */
    public static boolean verify(String token , String userName , String password) {
        try {
            // 使用HMAC256加密算法。
            // password是用来加密数字签名的密钥。
            Algorithm algorithm = Algorithm.HMAC256(password);
            JWTVerifier verifier = JWT.require(algorithm).withClaim("userName", userName).build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     *  获取token中的信息，无需password
     * @param token
     * @return token中包含的用户名
     */
    public static String getUserName(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userName").asString();
        }  catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     *  生成签名，5min之后过期
     * @param userName 用户名
     * @param password  密码
     * @return 加密的token
     */
    public static String sign(String userName , String password) {
        try {
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(password);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("alg", "HS256");
            map.put("typ", "JWT");
            // 附带userName信息
            return JWT.create().withHeader(map).withClaim("userName",userName).withIssuedAt(new Date()).withExpiresAt(date).sign(algorithm);
        }  catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static Date getIssuedAt(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getIssuedAt();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static Date getExpiresAt(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getExpiresAt();
        } catch (JWTDecodeException e) {
            return null;
        }
    }
}