package com.checkcode.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.checkcode.common.entity.Result;
import com.checkcode.common.tools.ResultTool;
import com.checkcode.entity.mpModel.DeviceIndividualModel;
import com.checkcode.entity.mpModel.WorkSheetModel;
import com.checkcode.entity.vo.FlowProgressVo;
import com.checkcode.service.IDeviceIndividualService;
import com.checkcode.service.IWorkSheetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/device/individual/info")
public class DeviceIndividualController {

    @Autowired
    IWorkSheetService workSheetService;
    @Autowired
    IDeviceIndividualService deviceIndividualService;

    /**
     * 获取一个设备信息(机码打印)
     *
     * @return
     */
    @PostMapping("/one")
    public Result getOne() {
        //查询当前正在运行的工单号
        QueryWrapper<WorkSheetModel> queryWsWrapper = new QueryWrapper<>();
        queryWsWrapper.eq(WorkSheetModel.STATUS, 1);
        WorkSheetModel workSheetModel = workSheetService.getOne(queryWsWrapper);

        if (workSheetModel == null) {
            return ResultTool.failedOnly("没有正在生产中的工单");
        }
        //第一步：首先判断已经被获取完
        QueryWrapper<DeviceIndividualModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DeviceIndividualModel.PROPERTIES_WORKSHEET_CODE, workSheetModel.getCode());
        int total = deviceIndividualService.count(queryWrapper);
        if (total == 0) {
            return ResultTool.failedOnly("没有设备信息");
        }

        queryWrapper.eq(DeviceIndividualModel.PROPERTIES_STATUS, 1);
        int finishedCount = deviceIndividualService.count(queryWrapper);
        if (total > 0 && finishedCount == total) {
            //说明已经没有设备了
            FlowProgressVo flowProgressVo = FlowProgressVo.builder().finished(finishedCount).total(total).build();
            return ResultTool.successWithMap(flowProgressVo);
        }

        //第二步：查询一个还没有被获取过的设备
        QueryWrapper<DeviceIndividualModel> queryOneWrapper = new QueryWrapper<>();
        queryOneWrapper.eq(DeviceIndividualModel.PROPERTIES_WORKSHEET_CODE, workSheetModel.getCode());
        queryOneWrapper.eq(DeviceIndividualModel.PROPERTIES_STATUS, 0);
        DeviceIndividualModel deviceIndividualModel = deviceIndividualService.getOne(queryOneWrapper);
        if (deviceIndividualModel == null) {
            return ResultTool.failedOnly("没有设备信息");
        }

        //第三步：更新获取状态
        deviceIndividualModel.setStatus(1);

        QueryWrapper<DeviceIndividualModel> updateWrapper = new QueryWrapper<>();
        updateWrapper.eq(DeviceIndividualModel.PROPERTIES_WORKSHEET_CODE, workSheetModel.getCode());
        if (!StringUtils.isEmpty(deviceIndividualModel.getSN1())) {
            updateWrapper.eq(DeviceIndividualModel.PROPERTIES_SN1, deviceIndividualModel.getSN1());
        }
        if (!StringUtils.isEmpty(deviceIndividualModel.getSN2())) {
            updateWrapper.eq(DeviceIndividualModel.PROPERTIES_SN2, deviceIndividualModel.getSN2());
        }
        deviceIndividualService.update(deviceIndividualModel, updateWrapper);

        FlowProgressVo flowProgressVo = FlowProgressVo.builder().finished(finishedCount + 1).total(total).info(deviceIndividualModel).build();
        return ResultTool.successWithMap(flowProgressVo);
    }


    /**
     * 根据以下搜索值匹配所有字段（SN,IMEI）查询出所有信息
     *
     * @param searchPojo
     * @param bindingResult
     * @return
     */
    /*@PostMapping("/query")
    public Result getDeviceIndividualInfoBySnOrImei(@Valid @RequestBody SearchPojo searchPojo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                return ResultTool.failedOnly(error.getDefaultMessage());
            }
        }


    }*/


}
