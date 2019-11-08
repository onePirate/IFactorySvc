package com.checkcode.web.entity.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class WsCreateParam {

    private String code;

    @NotEmpty(message = "工单名称不能为空")
    private String name;

    @NotEmpty(message = "截止日期不能为空")
    private String deadline;

    private String customerNo;

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
    @NotEmpty(message = "文件不能为空")
    private String fileUrl;

    @NotEmpty(message = "公司名称不能为空")
    private String company;

    @NotEmpty(message = "公司联系人不能为空")
    private String cusName;

    @NotEmpty(message = "联系方式不能为空")
    private String phone;

    @NotEmpty(message = "地址不能为空")
    private String address;

    private String icon;

}
