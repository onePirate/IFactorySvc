package com.checkcode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.checkcode.common.CustomerException;
import com.checkcode.common.FlowOrderConstant;
import com.checkcode.common.FlowOrderEnum;
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
import java.util.stream.Collectors;

@Service
public class IndividualFlowServiceImpl extends ServiceImpl<IIndividualFlowDao, IndividualFlowModel> implements IIndividualFlowService {

    @Autowired
    IWorkSheetService workSheetService;
    @Autowired
    IBoxDao boxDao;
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
        List<String> snList = new ArrayList<>();
        snList.add(individualFlowModel.getIndividualSn());
        List<IndividualFlowModel> flowModelList = baseMapper.getAnyOperFinishedData(snList, FlowOrderEnum.valueOf(FlowOrderConstant.flowMap.get(flowRecordParam.getOper())).getLastFlow(), "1");

        individualFlowModel.setResetTimes(flowModelList.get(0).getResetTimes());
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
        String wsCode = runningWsList.get(0).getCode();


        //去记录生产流程
        recordFlow(flowRecordParam);

        return getFlowProgressVo(wsCode, flowRecordParam.getOper());
    }


    @Override
    public List<IndividualFlowModel> getOperStatusBySnList(List<String> snList) {
        return baseMapper.getOperStatusBySnList(snList);
    }


    /**
     * 装箱
     */
    @Override
    @Transactional
    public FlowProgressVo boxUp(FlowBoxUpRecordParam flowBoxUpRecordParam) {
        //第一步：创建一个箱子
        List<WorkSheetModel> runningWsList = workSheetService.getRunningWs();
        if (runningWsList == null || runningWsList.size() == 0) {
            throw new CustomerException("没有正在生产中的工单");
        }
        String wsCode = runningWsList.get(0).getCode();
        BoxModel boxModel = createBox(flowBoxUpRecordParam, wsCode);

        //第二步：将所有sn的状态都修改为装箱状态
        List<String> snList = flowBoxUpRecordParam.getIndividualSnArr();
        List<IndividualFlowModel> individualFlowModelList = baseMapper.getOperStatusBySnList(snList);
        Map<String, Integer> resetTimesMap = new HashMap<>();
        int flowListSize = individualFlowModelList.size();
        for (int i = 0; i < flowListSize; i++) {
            IndividualFlowModel individualFlowModel = individualFlowModelList.get(i);
            //需要校验设备的上个流程是否正确为（称重）
            if (!individualFlowModel.getOper().equals(FlowOrderConstant.FIFTH)){
                throw new CustomerException("正在装箱的部分设备流程有误");
            }
            resetTimesMap.put(individualFlowModel.getIndividualSn(), individualFlowModel.getResetTimes());
        }

        List<IndividualFlowModel> batchBoxFlowList = new ArrayList<>();
        int snListSize = snList.size();
        for (int i = 0; i < snListSize; i++) {
            IndividualFlowModel individualFlowModel = new IndividualFlowModel();
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

    /**
     * 获得流程的进程
     *
     * @param wsCode
     * @param oper
     * @return
     */
    private FlowProgressVo getFlowProgressVo(String wsCode, String oper) {
        QueryWrapper<DeviceIndividualModel> queryCusWsDeviceWrapper = new QueryWrapper<>();
        queryCusWsDeviceWrapper.eq(DeviceIndividualModel.PROPERTIES_WORKSHEET_CODE, wsCode);
        List<DeviceIndividualModel> cusDwDeviceList = deviceIndividualService.list(queryCusWsDeviceWrapper);

        int total = cusDwDeviceList.size();
        if (cusDwDeviceList == null || total == 0) {
            throw new CustomerException("生产中的工单没有设备信息");
        }
        List<String> allSnList = cusDwDeviceList.stream().map(o -> {
            if (o.getSN1() != null) {
                return o.getSN1();
            }
            return o.getSN2();
        }).collect(Collectors.toList());
        List<IndividualFlowModel> flowModelList = baseMapper.getAnyOperFinishedData(allSnList, oper, "1");
        int finishedCount = flowModelList.size();

        FlowProgressVo flowProgressVo = FlowProgressVo.builder().total(total).finished(finishedCount).build();
        return flowProgressVo;
    }

    /**
     * 创建一个箱子
     *
     * @param flowBoxUpRecordParam
     * @param wsCode
     * @return
     */
    private BoxModel createBox(FlowBoxUpRecordParam flowBoxUpRecordParam, String wsCode) {
        BoxModel boxModel = new BoxModel();
        BeanUtils.copyProperties(flowBoxUpRecordParam, boxModel);
        if (StringUtils.isEmpty(boxModel.getCode())) {
            boxModel.setCode("box" + IdWorker.getCodeByUUId());
        }
        if (StringUtils.isEmpty(boxModel.getName())) {
            boxModel.setName(boxModel.getCode());
        }
        boxModel.setWorksheetNo(wsCode);
        boxDao.insert(boxModel);
        return boxModel;
    }
}
