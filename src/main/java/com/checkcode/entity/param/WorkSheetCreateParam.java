package com.checkcode.entity.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class WorkSheetCreateParam {

    private String code;

    @NotEmpty(message = "工单名称不能为空")
    private String name;

    @NotEmpty(message = "截止日期不能为空")
    private String deadline;

    @NotEmpty(message = "客户不能为空")
    private String customerNo;

    @NotEmpty(message = "员工工号不能为空")
    private String employeeNo;

    @NotEmpty(message = "平台不能为空")
    private String platform;

    @NotEmpty(message = "设备类型不能为空")
    private String deviceType;
    private String typeCode;
    private String brandName;
    private String color;
    private String fullWeight;
    private String trueWeight;
    private String deviceSize;
    private String thickness;
    private String extendInfo;

    /**
     * 用于记录工单的流程信息
     */
    @NotNull(message = "工单流程不能为空")
    private List<String> wsFlowList;
    /**
     * 用于解析Excel中的设备信息
     */
    @NotEmpty(message = "文件链接不能为空")
    private String fileUrl;

}
