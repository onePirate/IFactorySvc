package com.checkcode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.checkcode.common.CustomerException;
import com.checkcode.dao.IIndividualFlowDao;
import com.checkcode.entity.mpModel.DeviceIndividualModel;
import com.checkcode.entity.mpModel.IndividualFlowModel;
import com.checkcode.entity.mpModel.WorkSheetModel;
import com.checkcode.entity.param.FlowRecordParam;
import com.checkcode.entity.vo.FlowProgressVo;
import com.checkcode.service.IDeviceIndividualService;
import com.checkcode.service.IIndividualFlowService;
import com.checkcode.service.IWorkSheetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IndividualFlowServiceImpl extends ServiceImpl<IIndividualFlowDao, IndividualFlowModel> implements IIndividualFlowService {

    @Autowired
    IWorkSheetService workSheetService;
    @Autowired
    IDeviceIndividualService deviceIndividualService;

    @Override
    public IndividualFlowModel getDeviceLastRecord(String individualSn) {
        QueryWrapper<IndividualFlowModel> queryDeviceLastRecordWrapper = new QueryWrapper<>();
        queryDeviceLastRecordWrapper.eq(IndividualFlowModel.PROPERTIES_INDIVIDUAL_SN, individualSn);
        queryDeviceLastRecordWrapper.orderByDesc(IndividualFlowModel.PROPERTIES_OPER_TIME, IndividualFlowModel.PROPERTIES_OPER_TIME);
        return getOne(queryDeviceLastRecordWrapper);
    }


    /**
     * 没有重置的情况下，记录流程
     *
     * @param flowRecordParam
     */
    @Override
    public void recordFlow(FlowRecordParam flowRecordParam) {
        IndividualFlowModel individualFlowModel = new IndividualFlowModel();
        BeanUtils.copyProperties(flowRecordParam, individualFlowModel);
        individualFlowModel.setResetTimes(0);
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
    public FlowProgressVo recordFlowAndGetProcess(FlowRecordParam flowRecordParam) {
        List<WorkSheetModel> runningWsList = workSheetService.getRunningWs();
        if (runningWsList == null || runningWsList.size() == 0) {
            throw new CustomerException("没有正在生产中的工单");
        }
        String code = runningWsList.get(0).getCode();

        QueryWrapper<DeviceIndividualModel> queryCusWsDeviceWrapper = new QueryWrapper<>();
        queryCusWsDeviceWrapper.eq(DeviceIndividualModel.PROPERTIES_WORKSHEET_CODE, code);
        List<DeviceIndividualModel> cusDwDeviceList = deviceIndividualService.list(queryCusWsDeviceWrapper);

        int total = cusDwDeviceList.size();
        if (cusDwDeviceList == null || total == 0) {
            throw new CustomerException("生产中的工单没有设备信息");
        }
        //去记录生产流程
        recordFlow(flowRecordParam);

        List<String> snList = cusDwDeviceList.stream().map(o -> {
            if (o.getSN1() != null) {
                return o.getSN1();
            }
            return o.getSN2();
        }).collect(Collectors.toList());
        List<IndividualFlowModel> flowModelList = baseMapper.getAnyOperFinishedData(snList, flowRecordParam.getOper(), "1");

        int finishedCount = flowModelList.size();

        FlowProgressVo flowProgressVo = FlowProgressVo.builder().total(total).finished(finishedCount).build();
        return flowProgressVo;
    }


    @Override
    public List<IndividualFlowModel> getOperStatusBySnList(List<String> snList) {
        return baseMapper.getOperStatusBySnList(snList);
    }
}
