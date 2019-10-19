package com.checkcode.entity.mpModel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_worksheet")
public class WorkSheetModel{

    @TableId(type = IdType.INPUT)
    protected String code;
    protected String name;
    protected String createTime;
    protected String startTime;
    protected String deadline;
    protected Integer status;
    protected String customerNo;
    protected String platform;
    protected String deviceType;
    protected String typeCode;
    protected String brandName;
    protected String color;
    protected String fullWeight;
    protected String trueWeight;
    protected String deviceSize;
    protected String thickness;
    protected String extendInfo;
    private String fileUrl;

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
    public static final String EXTEND_INFO ="extend_info";
    public static final String FILE_URL="file_url";
}
