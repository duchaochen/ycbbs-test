package com.ycbbs.crud.shiro.filter;


import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ycbbs.crud.entity.ActiveUser;
import com.ycbbs.crud.exception.CustomException;
import com.ycbbs.crud.pojo.YcBbsResult;
import com.ycbbs.crud.shiro.jwt.JWTToken;
import com.ycbbs.crud.shiro.jwt.JWTUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

/**
 * @Title: JWTFilter.java
 * @Package com.tisson.demo.auth
 * @Description: TODO(用一句话描述该文件做什么)
 * @author Joker1995
 * @date 2018年11月8日
 * @version V1.0
 */
@Slf4j
@SuppressWarnings("all")
public class JWTFilter extends BasicHttpAuthenticationFilter {
    /**
     * 距离失效前5分钟重发签名
     */
    private static final int tokenRefreshInterval = 300;
    private final static Logger LOGGER = LoggerFactory.getLogger(JWTFilter.class);

    /**
     * 判断用户是否想要登入。 检测header里面是否包含Authorization字段即可
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader("Authorization");
        return authorization != null;
    }

    /**
     * @Title: executeLogin @Description:@return 返回类型 @throws
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws AuthenticationException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader("Authorization");
        if(StringUtils.isEmpty(authorization)) {
            throw new CustomException("授权失效!!!");
        }
        JWTToken token = new JWTToken(authorization);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        Subject subject = getSubject(request, response);
        subject.login(token);
        // 如果没有抛出异常则代表登入成功，返回true
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        Object principal = subject.getPrincipal();
        String newToken = null;
        if (principal instanceof ActiveUser) {
            ActiveUser loginUser = (ActiveUser) principal;
            String userName = JWTUtil.getUserName(authorization);

            //校验token是否正确
            if (!JWTUtil.verify(authorization, userName, loginUser.getPassword())) {
                throw new CustomException("校验失败");
            }

            //生成签名
            boolean shouldRefresh = shouldTokenRefresh(JWTUtil.getIssuedAt(authorization));
            if (shouldRefresh) {
                newToken = JWTUtil.sign(userName, loginUser.getPassword());
                //存redis
//                if (!cache.hashKeyExists("ssoToken:" + userName, newToken)) {
//                    Map<String,String> dataMap=new HashMap<String,String>();
//                    dataMap.put("kickOut", "false");
//                    cache.setHash("ssoToken:" + userName, newToken,dataMap);
//                    cache.expire("ssoToken:" + userName,
//                            Double.valueOf(JWTUtil.EXPIRE_TIME / 1000 + new Random().nextInt(30)).intValue());
//                }
//                cache.delHash("ssoToken:" + userName,authorization);
            }
        }
        if (!StringUtils.isEmpty(newToken)) {
            httpResponse.setHeader("Authorization", newToken);
            httpResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
        }
        return true;
    }

    /**
     * 这里我们详细说明下为什么最终返回的都是true，即允许访问 例如我们提供一个地址 GET /article 登入用户和游客看到的内容是不同的
     * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西 所以我们在这里返回true，Controller中可以通过
     * subject.isAuthenticated() 来判断用户是否登入
     * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
     * 但是这样做有一个缺点，就是不能够对GET,POST等请求进行分别过滤鉴权(因为我们重写了官方的方法)，但实际上对应用影响不大
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws AuthenticationException {
        if (isLoginAttempt(request, response)) {
            try {
                executeLogin(request, response);
            } catch (Exception e) {
                if(e instanceof AuthenticationException) {
                    //接口未授权或无授权码
                    response(request, response,1003,e.getMessage());
                }
                else if(e instanceof CustomException) {
                    response(request, response, 1000,e.getMessage());
                }else {
                    response(request, response,500, "系统错误");
                }
            }
        }
        return true;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers",
                httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        System.out.println(RequestMethod.OPTIONS.name());
        System.out.println(httpServletRequest.getMethod());
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     * 将错误信息输出给response
     */
    private void response(ServletRequest req, ServletResponse resp,Integer statusCode, String msg) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) resp;
        httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        try (PrintWriter out = httpServletResponse.getWriter()) {
            if(StringUtils.isEmpty(msg)) {
                msg="Null Point Exception";
            }
            String data = new Gson().toJson(YcBbsResult.build(statusCode, msg));
            out.append(data);
        } catch (IOException e) {
            LOGGER.error("ERROR IN WRITE RESPONSE:", e);
        }
    }

    protected boolean shouldTokenRefresh(Date issueDate) {
        LocalDateTime issueTime = LocalDateTime.ofInstant(issueDate.toInstant(), ZoneId.systemDefault());
        return LocalDateTime.now().minusSeconds(tokenRefreshInterval).isAfter(issueTime);
    }
}