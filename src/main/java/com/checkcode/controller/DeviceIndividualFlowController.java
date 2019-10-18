package com.checkcode.controller;

import com.checkcode.common.FlowOrderConstant;
import com.checkcode.common.FlowOrderEnum;
import com.checkcode.common.entity.Result;
import com.checkcode.common.tools.ResultTool;
import com.checkcode.entity.mpModel.IndividualFlowModel;
import com.checkcode.entity.param.FlowRecordParam;
import com.checkcode.service.IIndividualFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/device/individual/flow")
public class DeviceIndividualFlowController {

    private static final String MECHINE_PRINT = "MECHINE_PRINT";
    private static final String BOX_UP = "BOX_UP";

    @Autowired
    IIndividualFlowService individualFlowService;


    @PostMapping("/record")
    public Result recordDeviceFlow(@Valid @RequestBody FlowRecordParam flowRecordParam, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                return ResultTool.failedOnly(error.getDefaultMessage());
            }
        }

        if (MECHINE_PRINT.equals(FlowOrderConstant.flowMap.get(flowRecordParam.getOper()))) {
            //如果当前流程是第一个流程直接记录，判断是否以及记录过了
            IndividualFlowModel individualFlowModel = individualFlowService.getDeviceLastRecord(flowRecordParam.getIndividualSn());
            if (individualFlowModel != null) {
                String errMsg = "设备机码打印流程已经记录";
                log.warn(flowRecordParam.getIndividualSn() + "->" + errMsg);
                return ResultTool.failedOnly(errMsg);
            }
            return ResultTool.successWithMap(individualFlowService.recordFlowAndGetProcess(flowRecordParam));
        }else if (BOX_UP.equals(FlowOrderConstant.flowMap.get(flowRecordParam.getOper()))){
            //如果当前流程是最后一个流程，返回错误提示，装箱流程不在这个接口中操作
            String errMsg = "装箱不在这里操作";
            log.warn(flowRecordParam.getIndividualSn() + "->" + errMsg);
            return ResultTool.failedOnly(errMsg);
        }

        //其他流程
        IndividualFlowModel individualFlowModel = individualFlowService.getDeviceLastRecord(flowRecordParam.getIndividualSn());
        if (individualFlowModel == null) {
            String errMsg = "流程有误，请确保操作流程正确";
            log.warn(flowRecordParam.getIndividualSn() + "->" + errMsg);
            return ResultTool.failedOnly(errMsg);
        }
        FlowOrderEnum flowOrderEnum = FlowOrderEnum.valueOf(FlowOrderConstant.flowMap.get(individualFlowModel.getOper()));
        if (flowOrderEnum.getNextFlow().equals(flowRecordParam.getOper())){
            return ResultTool.successWithMap(individualFlowService.recordFlowAndGetProcess(flowRecordParam));
        }
        String errMsg = "流程有误，请确保操作流程正确";
        log.warn(flowRecordParam.getIndividualSn() + "->" + errMsg);
        return ResultTool.failedOnly(errMsg);

    }


}
