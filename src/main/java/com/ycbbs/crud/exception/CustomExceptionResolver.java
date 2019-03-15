package com.ycbbs.crud.exception;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ycbbs.crud.pojo.YcBbsResult;
import org.apache.shiro.authc.*;
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
        e.printStackTrace();
        CustomException exception;
        if (e instanceof CustomException) {
            //表示自定义异常
            exception = (CustomException)e;
        } else {
            //将改为自定义异常
            exception = new CustomException("未知错误");
        }
        YcBbsResult result = YcBbsResult.build(500, exception.getMessage());
        try {
            response.getWriter().write(new ObjectMapper().writeValueAsString(result));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return new ModelAndView();
    }

//    /**
//     * 转换shiro异常为自定义异常
//     * @param request
//     * @throws CustomException
//     */
//    private CustomException convertShiroException(HttpServletRequest request){
//        //如果登陆失败从request中获取认证异常信息，shiroLoginFailure就是shiro异常类的全限定名
//        String exceptionClassName = (String) request.getAttribute("shiroLoginFailure");
//        //根据shiro返回的异常类路径判断，抛出指定异常信息
//        if(exceptionClassName!=null) {
//            if (LockedAccountException.class.getName().equals(exceptionClassName)) {
//                //最终会抛给异常处理器
//                return new CustomException("账号被锁定!!!");
//            } else if (DisabledAccountException.class.getName().equals(exceptionClassName)) {
//                return new CustomException("账号被禁用!!!");
//            } else if (UnknownAccountException.class.getName().equals(exceptionClassName)) {
//                return new CustomException("身份验证失败!!!");
//            } else if (ExcessiveAttemptsException.class.getName().equals(exceptionClassName)) {
//                return new CustomException("登录失败次数过多!!!");
//            } else if (IncorrectCredentialsException.class.getName().equals(exceptionClassName)) {
//                return new CustomException("密码错误!!!");
//            } else if (ExpiredCredentialsException.class.getName().equals(exceptionClassName)) {
//                return new CustomException("过期的凭证!!!");
//            }
//        }
//        //最终在异常处理器生成未知错误
//        return new CustomException("认证失败!!!");
//    }
}
