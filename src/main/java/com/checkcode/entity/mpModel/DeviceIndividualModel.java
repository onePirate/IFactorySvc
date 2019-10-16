package com.checkcode.entity.mpModel;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_device_individual")
public class DeviceIndividualModel {

    @TableField("SN1")
    private String SN1;

    @TableField("SN2")
    private String SN2;

    @TableField("IMEI1")
    private String IMEI1;

    @TableField("IMEI2")
    private String IMEI2;

    @TableField("IMEI3")
    private String IMEI3;

    @TableField("IMEI4")
    private String IMEI4;

    @TableField("BTADDRESS")
    private String BTADDRESS;

    @TableField("WIFIADDRESS")
    private String WIFIADDRESS;

    @TableField("ETHERNNETMACADDRESS")
    private String ETHERNNETMACADDRESS;

    @TableField("MEID")
    private String MEID;

    @TableField("ESN")
    private String ESN;

    @TableField("EXTRA1")
    private String EXTRA1;

    @TableField("EXTRA2")
    private String EXTRA2;

    @TableField("EXTRA3")
    private String EXTRA3;

    @TableField("weight")
    private String weight;

    @TableField("worksheet_code")
    private String worksheetCode;

    @TableField("box_code")
    private String boxCode;



}
