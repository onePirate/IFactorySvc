package com.checkcode.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.checkcode.common.CustomerException;
import com.checkcode.common.StateEnum;
import com.checkcode.common.entity.Result;
import com.checkcode.common.tools.IdWorker;
import com.checkcode.common.tools.MD5Tool;
import com.checkcode.common.tools.ResultTool;
import com.checkcode.common.tools.TokenTool;
import com.checkcode.dao.IEmployeeDao;
import com.checkcode.entity.mpModel.EmployeeModel;
import com.checkcode.entity.param.EmployeeVaildGroup;
import com.checkcode.entity.pojo.EmployeePojo;
import com.checkcode.entity.vo.EmployeeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/")
public class EmployeeController {

    @Autowired
    IEmployeeDao userDao;

    @PostMapping("login")
    public Result login(@Validated(EmployeeVaildGroup.LoginGroup.class) @RequestBody EmployeePojo employeePojo, BindingResult bindingResult) {
        ResultTool.valid(bindingResult);
        QueryWrapper<EmployeeModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("employee_no", employeePojo.getEmployeeNo());
        EmployeeModel employeeModel = userDao.selectOne(queryWrapper);
        if (employeeModel == null) {
            throw new CustomerException(StateEnum.USER_NOT_EXISTS);
        }
        String computeMD5 = MD5Tool.getMD5(employeePojo.getPassword());
        if (!employeeModel.getPassword().equals(computeMD5)) {
            throw new CustomerException(StateEnum.PWD_NOT_RIGHT);
        }
        String token = TokenTool.createToken(employeePojo);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", token);
        return ResultTool.successWithMap(jsonObject);
    }

    /**
     * 创建员工
     *
     * @param employeePojo
     * @param bindingResult
     * @return
     */
    @PostMapping("employee/create")
    public Result create(@Validated(EmployeeVaildGroup.CreateGroup.class) @RequestBody EmployeePojo employeePojo, BindingResult bindingResult) {
        ResultTool.valid(bindingResult);
        if (!employeePojo.getPassword().equals(employeePojo.getConfirmPwd())){
            return ResultTool.failedOnly("密码与确认密码不一致");
        }

        EmployeeModel employeeModel = new EmployeeModel();
        BeanUtils.copyProperties(employeePojo, employeeModel);

        String computeMD5 = MD5Tool.getMD5(employeePojo.getPassword());
        employeeModel.setPassword(computeMD5);

        if (StringUtils.isEmpty(employeePojo.getEmployeeNo())) {
            employeeModel.setEmployeeNo("emp" + IdWorker.getNoByUUId());
        }
        int count = userDao.insert(employeeModel);
        if (count > 0) {
            return ResultTool.successWithMap(EmployeeVo.builder().employeeNo(employeeModel.getEmployeeNo()).build());
        } else {
            return ResultTool.failed(StateEnum.FAIL_SAVEDATA);
        }
    }


}
