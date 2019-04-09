package com.ycbbs.crud.pojo;

/**
 * 统一返回类
 */
public class YcBbsResult {

    /**
     * 错误代码:
     *  100 : 权限验证错误
     *  200 ：所有成功
     *  300 : 信息不合法
     *  302 : 身份信息未认证
     *  303 : 记住我失败
     *  304 : 没有此权限
     *  400 : 激活失败
     *  500 : 内部错误...
     *  1000：权限验证错误
     *  1001:"Token失效"
     *  1002:"用户名或密码错误"
     *  1003:"接口未授权或无授权码"
     *  1004:"会话无效"
     *  1005:"请求参数错误"
     *  1006:"请求方法错误"
     *  1007:"在线会话超出限制"
     */
    private Integer code;
    private String msg;
    private Object object;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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

    private YcBbsResult(Integer code, String msg, Object object, String token) {
        this.code = code;
        this.msg = msg;
        this.object = object;
        this.token = token;
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

    /**
     * 带有token的
     * @param code
     * @param msg
     * @param object
     * @param token
     * @return
     */
    public static YcBbsResult build(Integer code, String msg, Object object,String token) {
        YcBbsResult ycBbsResult = new YcBbsResult(code, msg, object,token);
        return ycBbsResult;
    }
    @Override
    public String toString() {
        return "YcBbsResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", object=" + object +
                ", token='" + token + '\'' +
                '}';
    }
}
