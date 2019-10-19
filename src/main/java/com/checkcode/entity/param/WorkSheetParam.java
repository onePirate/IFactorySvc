package com.checkcode.entity.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class WorkSheetParam {

    @NotEmpty(message = "工单号不能为空", groups = {WorkSheetGroup.LoginGroup.class})
    private String code;

    @NotNull(message = "状态不能为空", groups = {WorkSheetGroup.LoginGroup.class})
    private Integer status;

}
