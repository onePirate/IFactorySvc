package com.checkcode.entity.vo;

import com.checkcode.entity.mpModel.WorkSheetModel;
import lombok.Data;

@Data
public class WorkSheetVo extends WorkSheetModel {

    private String cusCompany;
    private String cusName;
    private String cusPhone;
    private String cusAddress;
    private String cusIcon;

}
