package com.checkcode.common;

public enum StateEnum {

    OK(0,"成功"),
    FAIL(500,"请求失败，稍后重试"),
    FAIL_UPLOADDATA(500,"上传数据失败，请稍后重试"),
    FAIL_SAVEDATA(500,"保存数据失败，请重试"),
    FAIL_EXCEL_DATA_EX(500,"Excel中设备信息有误"),
    REQ_HAS_ERR(500,"请求参数有误"),
    USER_HAS_ERR(500,"用户名或密码不能为空"),
    USER_NOT_EXISTS(500,"用户不存在"),
    PWD_NOT_RIGHT(500,"密码不正确"),
    USER_NOT_LOGIN(500,"用户未登录"),
    USER_LOGIN_TIMEOUT(500,"登录超时,请重新登录"),

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


    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
    
}
