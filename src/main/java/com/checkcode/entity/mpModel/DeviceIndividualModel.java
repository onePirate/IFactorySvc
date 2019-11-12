package com.checkcode.entity.mpModel;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@TableName("tb_device_individual")
public class DeviceIndividualModel {

    @JsonProperty(value = "individualSn")
    @TableField("individual_sn")
    private String individualSn;

    @JsonProperty(value = "SN1")
    @TableField("SN1")
    private String SN1;

    @JsonProperty(value = "SN2")
    @TableField("SN2")
    private String SN2;

    @JsonProperty(value = "IMEI1")
    @TableField("IMEI1")
    private String IMEI1;

    @JsonProperty(value = "IMEI2")
    @TableField("IMEI2")
    private String IMEI2;

    @JsonProperty(value = "IMEI3")
    @TableField("IMEI3")
    private String IMEI3;

    @JsonProperty(value = "IMEI4")
    @TableField("IMEI4")
    private String IMEI4;

    @JsonProperty(value = "BTADDRESS")
    @TableField("BTADDRESS")
    private String BTADDRESS;

    @JsonProperty(value = "WIFIADDRESS")
    @TableField("WIFIADDRESS")
    private String WIFIADDRESS;

    @JsonProperty(value = "ETHERNNETMACADDRESS")
    @TableField("ETHERNNETMACADDRESS")
    private String ETHERNNETMACADDRESS;

    @JsonProperty(value = "MEID")
    @TableField("MEID")
    private String MEID;

    @JsonProperty(value = "ESN")
    @TableField("ESN")
    private String ESN;

    @JsonProperty(value = "EXTRA1")
    @TableField("EXTRA1")
    private String EXTRA1;

    @JsonProperty(value = "EXTRA2")
    @TableField("EXTRA2")
    private String EXTRA2;

    @JsonProperty(value = "EXTRA3")
    @TableField("EXTRA3")
    private String EXTRA3;

    @JsonProperty(value = "weight")
    @TableField("weight")
    private String weight;

    @JsonProperty(value = "worksheetCode")
    @TableField("worksheet_code")
    private String worksheetCode;

    @JsonProperty(value = "boxCode")
    @TableField("box_code")
    private String boxCode;

    @TableField("status")
    private Integer status;

    public static final String PROPERTIES_INDIVIDUAL_SN = "individual_sn";
    public static final String PROPERTIES_SN1 = "SN1";
    public static final String PROPERTIES_SN2 = "SN2";
    public static final String PROPERTIES_IMEI1 = "IMEI1";
    public static final String PROPERTIES_IMEI2 = "IMEI2";
    public static final String PROPERTIES_IMEI3 = "IMEI3";
    public static final String PROPERTIES_IMEI4 = "IMEI4";
    public static final String PROPERTIES_BTADDRESS = "BTADDRESS";
    public static final String PROPERTIES_WIFIADDRESS = "WIFIADDRESS";
    public static final String PROPERTIES_ETHERNNETMACADDRESS = "ETHERNNETMACADDRESS";
    public static final String PROPERTIES_MEID = "MEID";
    public static final String PROPERTIES_ESN = "ESN";
    public static final String PROPERTIES_EXTRA1 = "EXTRA1";
    public static final String PROPERTIES_EXTRA2 = "EXTRA2";
    public static final String PROPERTIES_EXTRA3 = "EXTRA3";
    public static final String PROPERTIES_WEIGHT = "weight";
    public static final String PROPERTIES_WORKSHEET_CODE = "worksheet_code";
    public static final String PROPERTIES_BOX_CODE = "box_code";
    public static final String PROPERTIES_STATUS = "status";

}
