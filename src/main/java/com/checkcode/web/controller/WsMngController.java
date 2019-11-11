package com.checkcode.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.checkcode.common.entity.Result;
import com.checkcode.common.tools.ResultTool;
import com.checkcode.controller.CustomerController;
import com.checkcode.controller.WorkSheetController;
import com.checkcode.entity.param.CustomerParam;
import com.checkcode.entity.param.WorkSheetCreateParam;
import com.checkcode.web.entity.param.WsCreateParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/web/ws")
public class WsMngController {

    @Autowired
    CustomerController customerController;
    @Autowired
    WorkSheetController workSheetController;

    @PostMapping("/create")
    public Result createWorkSheet(@Valid @RequestBody WsCreateParam wsCreateParam, BindingResult bindingResult) {
        ResultTool.valid(bindingResult);

        CustomerParam customerParam = new CustomerParam();
        BeanUtils.copyProperties(wsCreateParam, customerParam);
        customerParam.setName(wsCreateParam.getCusName());
        Result result = customerController.create(customerParam, bindingResult);
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(result.getData()));
        String customerNo = jsonObject.getString("customerNo");

        WorkSheetCreateParam workSheetCreateParam = new WorkSheetCreateParam();
        BeanUtils.copyProperties(wsCreateParam, workSheetCreateParam);
        workSheetCreateParam.setCustomerNo(customerNo);
        return workSheetController.createWorkSheet(workSheetCreateParam, bindingResult);
    }

}
