package com.ycbbs.crud.exception;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ycbbs.crud.pojo.YcBbsResult;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 全局异常
 * @author Administrator
 */
public class CustomExceptionResolver implements HandlerExceptionResolver {

    @Nullable
    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @Nullable Object handler, Exception e) {
//        e.printStackTrace();
        Integer code = 500;
        CustomException exception;

        if (e instanceof CustomException) {
            //表示自定义异常
            exception = (CustomException)e;
        } else if (e instanceof UnauthorizedException) {
            //将改为自定义异常
            exception = new CustomException("无访问权限!!!");
            code = 304;
        } else if (e instanceof LockedAccountException) {
            //身份验证失败
            exception = new CustomException("账号被锁定!!!");
            code = 100;
        }else if (e instanceof DisabledAccountException) {
            //身份验证失败
            exception = new CustomException("账号被禁用!!!");
            code = 100;
        }else if (e instanceof UnknownAccountException) {
            //用户名错误
            exception = new CustomException("用户名错误!!!");
            code = 100;
        }else if (e instanceof ExcessiveAttemptsException) {
            //登录失败次数过多
            exception = new CustomException("登录失败次数过多!!!");
            code = 100;
        }else if (e instanceof IncorrectCredentialsException) {
            //密码错误
            exception = new CustomException("密码错误!!!");
            code = 100;
        }else if (e instanceof AuthenticationException) {
            //密码错误
            exception = new CustomException(e.getMessage());
            code = 100;
        }
        else {
            //将改为自定义异常
            exception = new CustomException("未知错误!!!");
        }
        YcBbsResult result = YcBbsResult.build(code, exception.getMessage());
        System.out.println(result);

        try {
            response.getWriter().write(new ObjectMapper().writeValueAsString(result));
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return new ModelAndView();
    }
}
