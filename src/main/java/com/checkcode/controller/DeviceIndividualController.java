package com.checkcode.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.checkcode.common.entity.Result;
import com.checkcode.common.tools.ResultTool;
import com.checkcode.entity.mpModel.DeviceIndividualModel;
import com.checkcode.entity.mpModel.IndividualFlowModel;
import com.checkcode.entity.pojo.SearchPojo;
import com.checkcode.entity.pojo.WSBasePojo;
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
    public Result getOne(@RequestBody WSBasePojo WSBasePojo, BindingResult bindingResult) {
        ResultTool.valid(bindingResult);
        String wsCode = WSBasePojo.getCode();
        workSheetService.getWsByCode(wsCode);
        //第一步：首先判断已经被获取完
        QueryWrapper<DeviceIndividualModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DeviceIndividualModel.PROPERTIES_WORKSHEET_CODE, wsCode);
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
        queryOneWrapper.eq(DeviceIndividualModel.PROPERTIES_WORKSHEET_CODE, wsCode);
        queryOneWrapper.eq(DeviceIndividualModel.PROPERTIES_STATUS, 0);
        DeviceIndividualModel deviceIndividualModel = deviceIndividualService.getOne(queryOneWrapper);
        if (deviceIndividualModel == null) {
            return ResultTool.failedOnly("没有设备信息");
        }
        //finishedCount 不加1，因为要机码打印状态完成才能加1
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

        /**
         * 判断运行中的工单是否存在，没有异常则说明存在
         */
        String wsCode = searchPojo.getCode();
        workSheetService.getWsByCode(wsCode);

        List<DeviceIndividualModel> deviceIndividualModelList = deviceIndividualService.getIndividualListByCondition(searchPojo);

        List<String> snList = workSheetService.getWsSnList(deviceIndividualModelList);

        List<DeviceInfoComposeVo> deviceInfoComposeVos = new ArrayList<>();
        if (snList != null && snList.size() > 0) {
            List<IndividualFlowModel> flowModelList = individualFlowService.getOperStatusBySnList(wsCode, snList);
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
                String keySn = sourceModel.getIndividualSn();
                deviceIndividualDetailVo.setIndividualSn(keySn);
                deviceIndividualDetailVo.setOper(flowModelMap.get(keySn).getOper());
                deviceIndividualDetailVo.setStatus(flowModelMap.get(keySn).getStatus());

                DeviceInfoComposeVo deviceInfoComposeVo = DeviceInfoComposeVo.builder().deviceInfo(deviceIndividualVo).deviceOtherInfo(deviceIndividualDetailVo).build();
                deviceInfoComposeVos.add(deviceInfoComposeVo);
            }
        }
        return ResultTool.successWithMap(deviceInfoComposeVos);
    }


}
