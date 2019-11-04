package com.checkcode.entity.pojo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class WSBasePojo {

    @NotEmpty(message = "工单号不能为空")
    protected String code;

}
