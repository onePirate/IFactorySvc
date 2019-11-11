package com.checkcode.common.entity;

import lombok.Data;

@Data
public class Result {

    protected Integer code;

    protected String msg;

    protected Integer count;

    protected Object data;
}
