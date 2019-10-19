package com.checkcode.entity.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DeviceIndividualPojo extends BaseRowModel {

    @ExcelProperty(index = 0)
    private String SN1;

    @ExcelProperty(index = 1)
    private String SN2;

    @ExcelProperty(index = 2)
    private String IMEI1;

    @ExcelProperty(index = 3)
    private String IMEI2;

    @ExcelProperty(index = 4)
    private String IMEI3;

    @ExcelProperty(index = 5)
    private String IMEI4;

    @ExcelProperty(index = 6)
    private String BTADDRESS;

    @ExcelProperty(index = 7)
    private String WIFIADDRESS;

    @ExcelProperty(index = 8)
    private String ETHERNNETMACADDRESS;

    @ExcelProperty(index = 9)
    private String MEID;

    @ExcelProperty(index = 10)
    private String ESN;

    @ExcelProperty(index = 11)
    private String EXTRA1;

    @ExcelProperty(index = 12)
    private String EXTRA2;

    @ExcelProperty(index = 13)
    private String EXTRA3;

    private String worksheetCode;

}
