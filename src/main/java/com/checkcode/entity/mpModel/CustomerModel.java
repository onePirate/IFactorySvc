package com.checkcode.entity.mpModel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_customer")
public class CustomerModel {

    @TableId(type = IdType.INPUT)
    private String customerNo;

    private String company;

    private String name;

    private String phone;

    private String address;

    private String icon;

    public static final String PROPERTIES_CUSTOMER_NO = "customer_no";
    public static final String PROPERTIES_COMPANY = "company";
    public static final String PROPERTIES_NAME = "name";
    public static final String PROPERTIES_PHONE = "phone";
    public static final String PROPERTIES_ADDRESS = "address";
    public static final String PROPERTIES_ICON = "icon";
}
