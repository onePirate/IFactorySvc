package com.checkcode.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class WorkSheetVo {

    private String code;
    private String name;
    private String createTime;
    private String startTime;
    private String deadline;
    private Integer status;
    private String customerNo;
    private String platform;
    private String deviceType;
    private String typeCode;
    private String brandName;
    private String color;
    private String fullWeight;
    private String trueWeight;
    private String deviceSize;
    private String thickness;
    private String extendInfo;
    private List<String> wsFlowList;
    private String cusCompany;
    private String cusName;
    private String cusPhone;
    private String cusAddress;
    private String cusIcon;

}
