package com.checkcode.entity.mpModel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("tb_worksheet")
public class WorkSheetModel{

    @TableId(type = IdType.INPUT)
    private String code;
    private String name;
    private Date createTime;
    private Date startTime;
    private Date deadline;
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
}
