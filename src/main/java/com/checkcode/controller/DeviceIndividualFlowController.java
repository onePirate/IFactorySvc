package com.checkcode.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.checkcode.common.CustomerException;
import com.checkcode.common.FlowOrderConstant;
import com.checkcode.common.FlowOrderEnum;
import com.checkcode.common.entity.Result;
import com.checkcode.common.tools.ResultTool;
import com.checkcode.entity.mpModel.DeviceIndividualModel;
import com.checkcode.entity.mpModel.IndividualFlowModel;
import com.checkcode.entity.param.FlowBoxUpRecordParam;
import com.checkcode.entity.param.FlowRecordParam;
import com.checkcode.entity.param.FlowRecordVaildGroup;
import com.checkcode.entity.pojo.SearchPojo;
import com.checkcode.entity.vo.FlowProgressVo;
import com.checkcode.service.IDeviceIndividualService;
import com.checkcode.service.IIndividualFlowService;
import com.checkcode.service.IWorkSheetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/device/individual/flow")
public class DeviceIndividualFlowController {

    private static final String MECHINE_PRINT = "MECHINE_PRINT";
    private static final String BOX_UP = "BOX_UP";

    @Autowired
    IWorkSheetService workSheetService;
    @Autowired
    IIndividualFlowService individualFlowService;
    @Autowired
    IDeviceIndividualService deviceIndividualService;


    @PostMapping("/record")
    public Result recordDeviceFlow(@Validated(FlowRecordVaildGroup.NormalGroup.class) @RequestBody FlowRecordParam flowRecordParam, BindingResult bindingResult) {
        ResultTool.valid(bindingResult);

        if (BOX_UP.equals(FlowOrderConstant.flowMap.get(flowRecordParam.getOper()))) {
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
        if (flowOrderEnum.getNextFlow().equals(flowRecordParam.getOper())) {
            if (FlowOrderConstant.ZERO.equals(flowRecordParam.getOper()) && "1".equals(flowRecordParam.getStatus())) {
                return ResultTool.successWithMap(individualFlowService.mechinePrintSuccessUpdateIndividualStatusOne(flowRecordParam));
            } else {
                return ResultTool.successWithMap(individualFlowService.recordFlowAndGetProcess(flowRecordParam, false));
            }

        }
        String errMsg = "流程有误，请确保操作流程正确";
        log.warn(flowRecordParam.getIndividualSn() + "->" + errMsg);
        return ResultTool.failedOnly(errMsg);

    }


    @PostMapping("/boxUp")
    public Result recordDeviceBoxUpFlow(@Valid @RequestBody FlowBoxUpRecordParam flowBoxUpRecordParam, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                return ResultTool.failedOnly(error.getDefaultMessage());
            }
        }

        return ResultTool.successWithMap(individualFlowService.boxUp(flowBoxUpRecordParam));
    }


    @PostMapping("/query")
    public Result queryFlow(@Valid @RequestBody SearchPojo searchPojo, BindingResult bindingResult) {
        ResultTool.valid(bindingResult);
        List<DeviceIndividualModel> deviceIndividualModelList = deviceIndividualService.getIndividualListByCondition(searchPojo.getSearchVal());

        List<String> snList = deviceIndividualModelList.stream().map(o -> {
            if (o.getSN1() != null) {
                return o.getSN1();
            }
            return o.getSN2();
        }).collect(Collectors.toList());

        Map<String, List<IndividualFlowModel>> flowMap = new HashMap<>();
        /**
         * 获取符合条件设备所有的流程
         */
        if (snList != null && snList.size() > 0) {

            QueryWrapper<IndividualFlowModel> flowModelsQueryWrapper = new QueryWrapper<>();
            flowModelsQueryWrapper.eq(IndividualFlowModel.PROPERTIES_WORKSHEET_CODE, deviceIndividualModelList.get(0).getWorksheetCode());
            flowModelsQueryWrapper.in(IndividualFlowModel.PROPERTIES_INDIVIDUAL_SN, snList);
            flowModelsQueryWrapper.orderByAsc(IndividualFlowModel.PROPERTIES_OPER_TIME);
            List<IndividualFlowModel> flowModelList = individualFlowService.list(flowModelsQueryWrapper);

            /**
             * 组装返回数据
             */
            int flowModelSize = flowModelList.size();
            for (int i = 0; i < flowModelSize; i++) {
                IndividualFlowModel flowModel = flowModelList.get(i);
                if (flowMap.containsKey(flowModel.getIndividualSn())) {
                    List<IndividualFlowModel> individualFlowModels = flowMap.get(flowModel.getIndividualSn());
                    individualFlowModels.add(flowModel);
                    flowMap.put(flowModel.getIndividualSn(), individualFlowModels);
                } else {
                    List<IndividualFlowModel> individualFlowModels = new ArrayList<>();
                    individualFlowModels.add(flowModel);
                    flowMap.put(flowModel.getIndividualSn(), individualFlowModels);
                }
            }
        }
        return ResultTool.successWithMap(flowMap);
    }


    /**
     * 需要确认一下重置有没有什么特殊限制
     * 没有流程的情况下，直接调用流程重置，直接报错
     * 如：只能向前面的流程重置
     *
     * @param flowRecordParam
     * @param bindingResult
     * @return
     */
    @PostMapping("/reset")
    public Result resetFlow(@Validated(FlowRecordVaildGroup.ResetGroup.class) @RequestBody FlowRecordParam flowRecordParam, BindingResult bindingResult) {
        ResultTool.valid(bindingResult);
        IndividualFlowModel individualFlowModel = individualFlowService.getDeviceLastRecord(flowRecordParam.getIndividualSn());
        int lastFlowNum = Integer.valueOf(individualFlowModel.getOper().split("_")[0]);
        int inputFlowNum = Integer.valueOf(flowRecordParam.getOper().split("_")[0]);
        if (inputFlowNum > lastFlowNum) {
            throw new CustomerException("请保证重置流程正确");
        }
        if (FlowOrderConstant.INITIALIZE.equals(flowRecordParam.getOper())) {
            FlowProgressVo flowProgressVo = individualFlowService.resetToInitializeNeedUpdateIndividualStatusZero(flowRecordParam);
            return ResultTool.successWithMap(flowProgressVo);
        }
        FlowProgressVo flowProgressVo = individualFlowService.recordFlowAndGetProcess(flowRecordParam, true);
        return ResultTool.successWithMap(flowProgressVo);
    }


}
