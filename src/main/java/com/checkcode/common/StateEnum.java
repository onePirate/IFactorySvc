package com.checkcode.common;

public enum StateEnum {

    OK(200,"成功"),
    FAIL(500,"请求失败，稍后重试"),
    FAIL_UPLOADDATA(501,"上传数据失败，请稍后重试"),
    FAIL_SAVEDATA(502,"保存数据失败，请重试"),
    REQ_HAS_ERR(506,"请求参数有误"),
    USER_HAS_ERR(507,"用户名或密码不能为空"),
    USER_NOT_EXISTS(508,"用户不存在"),
    PWD_NOT_RIGHT(509,"密码不正确"),
    USER_NOT_LOGIN(511,"用户未登录"),
    USER_LOGIN_TIMEOUT(512,"登录超时,请重新登录"),

    ;

    StateEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * code
     */
    private Integer code;

    /**
     * 中文释义
     */
    private String msg;

    /**
     * English Desc
     */
    private String enDesc;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
    
}
