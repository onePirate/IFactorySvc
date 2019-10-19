package com.checkcode.entity.mpModel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_box")
public class BoxModel {

    @TableId(type = IdType.INPUT)
    private String code;

    private String boxNum;

    private String name;

    private String createTime;

    private String weight;

    private String size;

    private String worksheetNo;

}
