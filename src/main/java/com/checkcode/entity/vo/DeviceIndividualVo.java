package com.checkcode.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DeviceIndividualVo {

    @JsonProperty(value = "SN1")
    protected String SN1;

    @JsonProperty(value = "SN2")
    protected String SN2;

    @JsonProperty(value = "IMEI1")
    protected String IMEI1;

    @JsonProperty(value = "IMEI2")
    protected String IMEI2;

    @JsonProperty(value = "IMEI3")
    protected String IMEI3;

    @JsonProperty(value = "IMEI4")
    protected String IMEI4;

    @JsonProperty(value = "BTADDRESS")
    protected String BTADDRESS;

    @JsonProperty(value = "WIFIADDRESS")
    protected String WIFIADDRESS;

    @JsonProperty(value = "ETHERNNETMACADDRESS")
    protected String ETHERNNETMACADDRESS;

    @JsonProperty(value = "MEID")
    protected String MEID;

    @JsonProperty(value = "ESN")
    protected String ESN;

    @JsonProperty(value = "EXTRA1")
    protected String EXTRA1;

    @JsonProperty(value = "EXTRA2")
    protected String EXTRA2;

    @JsonProperty(value = "EXTRA3")
    protected String EXTRA3;

}
