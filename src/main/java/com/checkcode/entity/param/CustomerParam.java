package com.checkcode.entity.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CustomerParam {

    @NotEmpty(message = "公司名称不能为空")
    private String company;

    @NotEmpty(message = "公司联系人不能为空")
    private String name;

    @NotEmpty(message = "联系方式不能为空")
    private String phone;

    @NotEmpty(message = "地址不能为空")
    private String address;

    private String icon;
}
