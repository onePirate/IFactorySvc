package com.checkcode.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.checkcode.common.StateEnum;
import com.checkcode.common.entity.Result;
import com.checkcode.common.tools.IdWorker;
import com.checkcode.common.tools.ResultTool;
import com.checkcode.entity.mpModel.CustomerModel;
import com.checkcode.entity.param.CustomerParam;
import com.checkcode.entity.pojo.SearchPojo;
import com.checkcode.entity.vo.CustomerBaseVo;
import com.checkcode.service.ICustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    ICustomerService customerService;

    @PostMapping("/create")
    public Result create(@Valid @RequestBody CustomerParam customerParam, BindingResult bindingResult) {
        ResultTool.valid(bindingResult);
        CustomerModel customerModel = new CustomerModel();
        BeanUtils.copyProperties(customerParam, customerModel);

        //判断数据库中数据是否已经存在
        QueryWrapper<CustomerModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("company", customerModel.getCompany());
        queryWrapper.eq("name", customerModel.getName());
        queryWrapper.eq("phone", customerModel.getPhone());
        CustomerModel queryCustomerModel = customerService.getOne(queryWrapper);
        if (queryCustomerModel != null) {
            CustomerBaseVo customerBaseVo = new CustomerBaseVo();
            customerBaseVo.setCustomerNo(queryCustomerModel.getCustomerNo());
            return ResultTool.successWithMap(customerBaseVo);
        }

        customerModel.setCustomerNo("cus" + IdWorker.getCodeByUUId());
        if (customerService.save(customerModel)) {
            CustomerBaseVo customerBaseVo = new CustomerBaseVo();
            customerBaseVo.setCustomerNo(customerModel.getCustomerNo());
            return ResultTool.successWithMap(customerBaseVo);
        } else {
            return ResultTool.failed(StateEnum.FAIL_SAVEDATA);
        }
    }


    @PostMapping("/queryByAttr")
    public Result create(@RequestBody SearchPojo searchPojo) {
        QueryWrapper<CustomerModel> queryWrapper = new QueryWrapper<>();
        String searchVal = searchPojo.getSearchVal() == null ? "" : searchPojo.getSearchVal();
        queryWrapper.like("company", searchVal);
        queryWrapper.or().like("name", searchVal);
        queryWrapper.or().like("phone", searchVal);
        List<CustomerModel> customerModels = customerService.list(queryWrapper);
        return ResultTool.successWithMap(customerModels);
    }


}
