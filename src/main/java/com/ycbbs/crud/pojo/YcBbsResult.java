package com.ycbbs.crud.pojo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一返回类
 */
public class YcBbsResult {

    /**
     * 错误代码，200，500...
     */
    private Integer code;
    private String msg;
    private Object object;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    private YcBbsResult(Integer code, String msg, Object object) {
        this.code = code;
        this.msg = msg;
        this.object = object;
    }

    private YcBbsResult() {
        this(200,"成功",null);
    }

    public static YcBbsResult ok() {
        return new YcBbsResult();
    }

    public static YcBbsResult error() {
        return new YcBbsResult(500,"系统错误!",null);
    }

    public static YcBbsResult build(Integer code, String msg) {
        YcBbsResult ycBbsResult = new YcBbsResult(code, msg, null);
        return ycBbsResult;
    }

    public static YcBbsResult build(Integer code, String msg, Object object) {
        YcBbsResult ycBbsResult = new YcBbsResult(code, msg, object);
        return ycBbsResult;
    }
}
