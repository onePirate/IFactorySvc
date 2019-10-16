package com.checkcode.common;

import lombok.Setter;

/**
 * @Author: gaodw
 * @Date: 20:29 2019/7/18
 * @Desc:
 */
@Setter
public class CustomerException extends RuntimeException{

    private Integer code;

    public CustomerException(String message)
    {
        super(message);
        this.setCode(500);
    }

    public CustomerException(Integer errorCode, String message)
    {
        super(message);
        this.setCode(errorCode);
    }

    public CustomerException(StateEnum stateEnum)
    {
        super(stateEnum.toString());
        this.setCode(stateEnum.getCode());
    }
}
