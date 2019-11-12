package com.checkcode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.checkcode.common.CustomerException;
import com.checkcode.common.FlowOrderConstant;
import com.checkcode.common.tools.IdWorker;
import com.checkcode.dao.IBoxDao;
import com.checkcode.dao.IIndividualFlowDao;
import com.checkcode.entity.mpModel.BoxModel;
import com.checkcode.entity.mpModel.DeviceIndividualModel;
import com.checkcode.entity.mpModel.IndividualFlowModel;
import com.checkcode.entity.mpModel.WorkSheetModel;
import com.checkcode.entity.param.FlowBoxUpRecordParam;
import com.checkcode.entity.param.FlowRecordParam;
import com.checkcode.entity.vo.FlowProgressVo;
import com.checkcode.service.IDeviceIndividualService;
import com.checkcode.service.IIndividualFlowService;
import com.checkcode.service.IWorkSheetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IndividualFlowServiceImpl extends ServiceImpl<IIndividualFlowDao, IndividualFlowModel> implements IIndividualFlowService {

    @Autowired
    IWorkSheetService workSheetService;
    @Autowired
    IBoxDao boxDao;
    @Autowired
    IDeviceIndividualService deviceIndividualService;

    /**
     * 获得最后操作状态
     *
     * @param flowRecordParam
     * @return
     */
    @Override
    public IndividualFlowModel getDeviceLastRecord(FlowRecordParam flowRecordParam) {
        QueryWrapper<IndividualFlowModel> queryDeviceLastRecordWrapper = new QueryWrapper<>();
        queryDeviceLastRecordWrapper.eq(IndividualFlowModel.PROPERTIES_WORKSHEET_CODE, flowRecordParam.getCode());
        queryDeviceLastRecordWrapper.eq(IndividualFlowModel.PROPERTIES_INDIVIDUAL_SN, flowRecordParam.getIndividualSn());
        queryDeviceLastRecordWrapper.eq(IndividualFlowModel.PROPERTIES_STATUS, "1");
        queryDeviceLastRecordWrapper.orderByDesc(IndividualFlowModel.PROPERTIES_RESET_TIMES, IndividualFlowModel.PROPERTIES_OPER_TIME);
        return getOne(queryDeviceLastRecordWrapper);
    }


    /**
     * 没有重置的情况下，记录流程
     *
     * @param flowRecordParam
     */
    private void recordFlow(FlowRecordParam flowRecordParam, boolean isReset) {
        IndividualFlowModel individualFlowModel = new IndividualFlowModel();
        BeanUtils.copyProperties(flowRecordParam, individualFlowModel);
        individualFlowModel.setWorksheetCode(flowRecordParam.getCode());
        IndividualFlowModel flowModel = getDeviceLastRecord(flowRecordParam);
        if (isReset) {
            individualFlowModel.setResetTimes(flowModel.getResetTimes() + 1);
            individualFlowModel.setStatus("1");
        } else {
            individualFlowModel.setResetTimes(flowModel.getResetTimes());
        }
        save(individualFlowModel);
    }

    /**
     * 获得当前进程完成的设备数量
     *
     * @param flowRecordParam
     * @return
     */
    @Override
    @Transactional
    public FlowProgressVo recordFlowAndGetProcess(FlowRecordParam flowRecordParam, boolean isReset) {
        //去记录生产流程
        recordFlow(flowRecordParam, isReset);

        return getFlowProgressVo(flowRecordParam.getCode(), flowRecordParam.getOper());
    }


    @Override
    public List<IndividualFlowModel> getOperStatusBySnList(String wsCode, List<String> snList) {
        return baseMapper.getOperStatusBySnList(wsCode, snList);
    }


    /**
     * 装箱
     */
    @Override
    @Transactional
    public FlowProgressVo boxUp(FlowBoxUpRecordParam flowBoxUpRecordParam) {
        //第一步：创建一个箱子
        String wsCode = flowBoxUpRecordParam.getCode();
        WorkSheetModel workSheetModel = workSheetService.getWsByCode(wsCode);
        BoxModel boxModel = createBox(flowBoxUpRecordParam);

        //第二步：将所有sn的状态都修改为装箱状态
        Map<String, String> wsFlowMap = getWsFlowMap(workSheetModel.getWsFlow());
        List<String> snList = flowBoxUpRecordParam.getIndividualSnArr();
        List<IndividualFlowModel> individualFlowModelList = baseMapper.getOperStatusBySnList(wsCode, snList);
        Map<String, Integer> resetTimesMap = new HashMap<>();
        int flowListSize = individualFlowModelList.size();
        for (int i = 0; i < flowListSize; i++) {
            IndividualFlowModel individualFlowModel = individualFlowModelList.get(i);
            //需要校验设备的上个流程是否正确为（称重）
            if (!FlowOrderConstant.SIXTH.equals(wsFlowMap.get(individualFlowModel.getOper()))) {
                throw new CustomerException("正在装箱的部分设备流程有误");
            }
            resetTimesMap.put(individualFlowModel.getIndividualSn(), individualFlowModel.getResetTimes());
        }

        List<IndividualFlowModel> batchBoxFlowList = new ArrayList<>();
        int snListSize = snList.size();
        for (int i = 0; i < snListSize; i++) {
            IndividualFlowModel individualFlowModel = new IndividualFlowModel();
            individualFlowModel.setWorksheetCode(wsCode);
            individualFlowModel.setIndividualSn(snList.get(i));
            individualFlowModel.setEmployeeNo(flowBoxUpRecordParam.getEmployeeNo());
            individualFlowModel.setOper(FlowOrderConstant.SIXTH);
            individualFlowModel.setStatus("1");
            Integer resetTimes = resetTimesMap.get(snList.get(i));
            if (resetTimes == null) {
                throw new CustomerException("部门设备流程有误");
            }
            individualFlowModel.setResetTimes(resetTimes);
            individualFlowModel.setBoxCode(boxModel.getCode());
            batchBoxFlowList.add(individualFlowModel);
        }
        saveBatch(batchBoxFlowList);


        //第三步：返回装箱进程结果
        return getFlowProgressVo(wsCode, FlowOrderConstant.SIXTH);

    }

    @Override
    @Transactional
    public FlowProgressVo mechinePrintSuccessUpdateIndividualStatusOne(FlowRecordParam flowRecordParam) {
        QueryWrapper<DeviceIndividualModel> queryOneWrapper = new QueryWrapper<>();
        queryOneWrapper.eq(DeviceIndividualModel.PROPERTIES_WORKSHEET_CODE, flowRecordParam.getCode());
        queryOneWrapper.eq(DeviceIndividualModel.PROPERTIES_STATUS, 0);
        DeviceIndividualModel deviceIndividualModel = deviceIndividualService.getOne(queryOneWrapper);
        deviceIndividualModel.setStatus(1);
        QueryWrapper<DeviceIndividualModel> updateWrapper = new QueryWrapper<>();
        updateWrapper.eq(DeviceIndividualModel.PROPERTIES_WORKSHEET_CODE, flowRecordParam.getCode());
        if (!StringUtils.isEmpty(deviceIndividualModel.getSN1())) {
            updateWrapper.eq(DeviceIndividualModel.PROPERTIES_SN1, deviceIndividualModel.getSN1());
        }
        if (!StringUtils.isEmpty(deviceIndividualModel.getSN2())) {
            updateWrapper.eq(DeviceIndividualModel.PROPERTIES_SN2, deviceIndividualModel.getSN2());
        }
        deviceIndividualService.update(deviceIndividualModel, updateWrapper);
        return recordFlowAndGetProcess(flowRecordParam, false);
    }

    @Override
    @Transactional
    public FlowProgressVo resetToInitializeNeedUpdateIndividualStatusZero(FlowRecordParam flowRecordParam) {
        QueryWrapper<DeviceIndividualModel> OneWrapper = new QueryWrapper<>();
        OneWrapper.eq(DeviceIndividualModel.PROPERTIES_WORKSHEET_CODE, flowRecordParam.getCode());
        OneWrapper.and(properties ->
                properties.eq(DeviceIndividualModel.PROPERTIES_SN1, flowRecordParam.getIndividualSn())
                        .or().eq(DeviceIndividualModel.PROPERTIES_SN2, flowRecordParam.getIndividualSn()));
        DeviceIndividualModel deviceIndividualModel = deviceIndividualService.getOne(OneWrapper);
        deviceIndividualModel.setStatus(0);
        deviceIndividualService.update(deviceIndividualModel, OneWrapper);
        return recordFlowAndGetProcess(flowRecordParam, true);
    }


    /**
     * 获得流程的进程
     *
     * @param wsCode
     * @param oper
     * @return
     */
    @Override
    public FlowProgressVo getFlowProgressVo(String wsCode, String oper) {
        QueryWrapper<DeviceIndividualModel> queryCusWsDeviceWrapper = new QueryWrapper<>();
        queryCusWsDeviceWrapper.eq(DeviceIndividualModel.PROPERTIES_WORKSHEET_CODE, wsCode);
        List<DeviceIndividualModel> cusWsDeviceList = deviceIndividualService.list(queryCusWsDeviceWrapper);

        int total = cusWsDeviceList.size();
        if (cusWsDeviceList == null || total == 0) {
            throw new CustomerException("生产中的工单没有设备信息");
        }
        List<String> allSnList = workSheetService.getWsSnList(cusWsDeviceList);
        List<IndividualFlowModel> flowModelList = baseMapper.getAnyOperFinishedData(allSnList, wsCode, oper, "1");
        int finishedCount = flowModelList.size();

        FlowProgressVo flowProgressVo = FlowProgressVo.builder().total(total).finished(finishedCount).build();
        return flowProgressVo;
    }

    /**
     * 获取工单流程的Map，key为当前流程，val为当前流程的下一流程
     *
     * @param wsFlow
     * @return
     */
    @Override
    public Map<String, String> getWsFlowMap(String wsFlow) {
        String[] wsFlowArr = wsFlow.split(FlowOrderConstant.FLOW_SPLIT_CHAR);
        int length = wsFlowArr.length;
        Map<String, String> wsFlowMap = new HashMap<>();
        for (int i = 0; i < length; i++) {
            if (i == (length - 1)) {
                wsFlowMap.put(wsFlowArr[i], "");
            } else {
                wsFlowMap.put(wsFlowArr[i], wsFlowArr[i + 1]);
            }
        }
        return wsFlowMap;
    }

    /**
     * 创建一个箱子
     *
     * @param flowBoxUpRecordParam
     * @return
     */
    private BoxModel createBox(FlowBoxUpRecordParam flowBoxUpRecordParam) {
        BoxModel boxModel = new BoxModel();
        BeanUtils.copyProperties(flowBoxUpRecordParam, boxModel);
        boxModel.setCode("box" + IdWorker.getCodeByUUId());
        if (StringUtils.isEmpty(boxModel.getName())) {
            boxModel.setName(boxModel.getCode());
        }
        boxModel.setWorksheetNo(flowBoxUpRecordParam.getCode());
        boxDao.insert(boxModel);
        return boxModel;
    }
}
