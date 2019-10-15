package com.checkcode.entity.pojo;

import com.checkcode.entity.param.EmployeeVaildGroup;
import com.checkcode.entity.vo.CustomerBaseVo;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class EmployeePojo extends CustomerBaseVo {

    @NotEmpty (message = "员工号不能为空", groups = {EmployeeVaildGroup.LoginGroup.class})
    private String employeeNo;

    private String name;

    @NotEmpty(message="密码不能为空")
    private String password;

    @NotEmpty (message = "确认密码不能为空", groups = {EmployeeVaildGroup.CreateGroup.class})
    private String confirmPwd;

}
