package com.checkcode.entity.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class FlowRecordParam {


    @NotEmpty(message = "SN不能为空")
    private String individualSn;

    @NotEmpty(message = "员工号不能为空")
    private String employeeNo;

    @NotEmpty(message = "操作类型不能为空")
    private String oper;

    @NotEmpty(message = "操作状态不能为空")
    private String status;

    private String weight;


}
