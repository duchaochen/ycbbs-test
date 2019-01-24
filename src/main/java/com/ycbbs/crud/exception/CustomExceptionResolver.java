package com.ycbbs.crud.exception;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ycbbs.crud.pojo.YcBbsResult;
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
}
