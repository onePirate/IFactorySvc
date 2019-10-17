package com.checkcode.entity.mpModel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_worksheet")
public class WorkSheetModel{

    @TableId(type = IdType.INPUT)
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

    public static final String CODE ="code";
    public static final String NAME ="name";
    public static final String CREATE_TIME ="create_time";
    public static final String START_TIME ="start_time";
    public static final String DEADLINE ="deadline";
    public static final String STATUS ="status";
    public static final String CUSTOMER_NO ="customer_no";
    public static final String PLATFORM ="platform";
    public static final String DEVICE_TYPE ="device_type";
    public static final String TYPE_CODE ="type_code";
    public static final String BRAND_NAME ="brand_name";
    public static final String COLOR ="color";
    public static final String FULL_WEIGHT ="full_weight";
    public static final String TRUE_WEIGHT ="true_weight";
    public static final String DEVICE_SIZE ="device_size";
    public static final String THICKNESS ="thickness";
    public static final String extendinfo ="extend_info";
}
