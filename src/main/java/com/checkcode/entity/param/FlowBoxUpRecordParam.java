package com.checkcode.entity.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class FlowBoxUpRecordParam {


    @NotNull(message = "设备不能为空")
    private List<String> individualSnArr;

    @NotEmpty(message = "员工号不能为空")
    private String employeeNo;

    private String code;

    private String name;

    private String boxNum;

    private String weight;

    private String size;


}
