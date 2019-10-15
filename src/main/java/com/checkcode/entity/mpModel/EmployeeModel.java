package com.checkcode.entity.mpModel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_employee")
public class EmployeeModel {

    @TableId(type = IdType.INPUT)
    private String employeeNo;

    private String name;

    private String password;
}
