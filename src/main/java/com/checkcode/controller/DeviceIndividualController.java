package com.checkcode.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.checkcode.common.entity.Result;
import com.checkcode.common.tools.ResultTool;
import com.checkcode.entity.mpModel.DeviceIndividualModel;
import com.checkcode.entity.mpModel.IndividualFlowModel;
import com.checkcode.entity.mpModel.WorkSheetModel;
import com.checkcode.entity.pojo.SearchPojo;
import com.checkcode.entity.vo.DeviceIndividualDetailVo;
import com.checkcode.entity.vo.DeviceIndividualVo;
import com.checkcode.entity.vo.DeviceInfoComposeVo;
import com.checkcode.entity.vo.FlowProgressVo;
import com.checkcode.service.IDeviceIndividualService;
import com.checkcode.service.IIndividualFlowService;
import com.checkcode.service.IWorkSheetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
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
@RequestMapping("/device/individual/info")
public class DeviceIndividualController {

    @Autowired
    IWorkSheetService workSheetService;
    @Autowired
    IDeviceIndividualService deviceIndividualService;
    @Autowired
    IIndividualFlowService individualFlowService;

    /**
     * 获取一个设备信息(机码打印)
     *
     * @return
     */
    @PostMapping("/one")
    public Result getOne() {
        //查询当前正在运行的工单号
        List<WorkSheetModel> workSheetModelList = workSheetService.getRunningWs();

        if (workSheetModelList == null || workSheetModelList.size() == 0) {
            return ResultTool.failedOnly("没有正在生产中的工单");
        }
        WorkSheetModel workSheetModel = workSheetModelList.get(0);
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

        FlowProgressVo flowProgressVo = FlowProgressVo.builder().finished(finishedCount).total(total).info(deviceIndividualModel).build();
        return ResultTool.successWithMap(flowProgressVo);
    }


    /**
     * 根据以下搜索值匹配所有字段（SN,IMEI）查询出所有信息
     *
     * @param searchPojo
     * @param bindingResult
     * @return
     */
    @PostMapping("/query")
    public Result getDeviceIndividualInfoBySnOrImei(@Valid @RequestBody SearchPojo searchPojo, BindingResult bindingResult) {
        ResultTool.valid(bindingResult);

        List<DeviceIndividualModel> deviceIndividualModelList = deviceIndividualService.getIndividualListByCondition(searchPojo.getSearchVal());

        List<String> snList = deviceIndividualModelList.stream().map(o -> {
            if (o.getSN1() != null) {
                return o.getSN1();
            }
            return o.getSN2();
        }).collect(Collectors.toList());

        List<DeviceInfoComposeVo> deviceInfoComposeVos = new ArrayList<>();
        if (snList != null && snList.size() > 0) {
            List<IndividualFlowModel> flowModelList = individualFlowService.getOperStatusBySnList(snList);
            Map<String, IndividualFlowModel> flowModelMap = new HashMap<>();
            int flowListSize = flowModelList.size();
            for (int i = 0; i < flowListSize; i++) {
                flowModelMap.put(flowModelList.get(i).getIndividualSn(), flowModelList.get(i));
            }

            int size = deviceIndividualModelList.size();
            for (int i = 0; i < size; i++) {
                DeviceIndividualModel sourceModel = deviceIndividualModelList.get(i);
                DeviceIndividualVo deviceIndividualVo = new DeviceIndividualVo();
                BeanUtils.copyProperties(sourceModel, deviceIndividualVo);

                DeviceIndividualDetailVo deviceIndividualDetailVo = new DeviceIndividualDetailVo();
                deviceIndividualDetailVo.setWorksheetCode(sourceModel.getWorksheetCode());
                deviceIndividualDetailVo.setWeight(sourceModel.getWeight());
                if (!StringUtils.isEmpty(sourceModel.getSN1())) {
                    if (flowModelMap.get(sourceModel.getSN1()) != null) {
                        deviceIndividualDetailVo.setOper(flowModelMap.get(sourceModel.getSN1()).getOper());
                        deviceIndividualDetailVo.setStatus(flowModelMap.get(sourceModel.getSN1()).getStatus());
                    }
                } else {
                    if (flowModelMap.get(sourceModel.getSN2()) != null) {
                        deviceIndividualDetailVo.setOper(flowModelMap.get(sourceModel.getSN2()).getOper());
                        deviceIndividualDetailVo.setStatus(flowModelMap.get(sourceModel.getSN2()).getStatus());
                    }
                }

                DeviceInfoComposeVo deviceInfoComposeVo = DeviceInfoComposeVo.builder().deviceInfo(deviceIndividualVo).deviceOtherInfo(deviceIndividualDetailVo).build();
                deviceInfoComposeVos.add(deviceInfoComposeVo);
            }
        }
        return ResultTool.successWithMap(deviceInfoComposeVos);
    }


}
