package com.checkcode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.checkcode.common.CustomerException;
import com.checkcode.common.FlowOrderConstant;
import com.checkcode.common.tools.DateTool;
import com.checkcode.dao.IWorkSheetDao;
import com.checkcode.entity.mpModel.DeviceIndividualModel;
import com.checkcode.entity.mpModel.IndividualFlowModel;
import com.checkcode.entity.mpModel.WorkSheetModel;
import com.checkcode.entity.param.WorkSheetCreateParam;
import com.checkcode.entity.param.WorkSheetParam;
import com.checkcode.service.IDeviceIndividualService;
import com.checkcode.service.IIndividualFlowService;
import com.checkcode.service.IWorkSheetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkSheetServiceImpl extends ServiceImpl<IWorkSheetDao, WorkSheetModel> implements IWorkSheetService {

    @Autowired
    IIndividualFlowService individualFlowService;
    @Autowired
    IDeviceIndividualService deviceIndividualService;


    /**
     * 创建工单
     *
     * @param deviceIndividualList
     * @param workSheetCreateParam
     * @throws ParseException
     */
    @Override
    @Transactional
    public void createWorkSheet(List<DeviceIndividualModel> deviceIndividualList, WorkSheetCreateParam workSheetCreateParam){
        deviceIndividualService.saveBatch(deviceIndividualList);
        WorkSheetModel workSheetModel = new WorkSheetModel();
        BeanUtils.copyProperties(workSheetCreateParam, workSheetModel);
        workSheetModel.setStatus(0);

        /**数据库当中用“:”拼接流程*/
        List<String> flowList = workSheetCreateParam.getWsFlowList();
        flowList.add(FlowOrderConstant.INITIALIZE);
        List<String> newFlowList = flowList.stream().sorted().collect(Collectors.toList());
        workSheetModel.setWsFlow(String.join(FlowOrderConstant.FLOW_SPLIT_CHAR, newFlowList));
        save(workSheetModel);

        //以下是组装设备流程初始化数据
        List<IndividualFlowModel> flowModelList = new ArrayList<>();
        int deviceNumSize = deviceIndividualList.size();
        for (int i = 0; i < deviceNumSize; i++) {
            DeviceIndividualModel individualModel = deviceIndividualList.get(i);
            IndividualFlowModel individualFlowModel = new IndividualFlowModel();
            individualFlowModel.setWorksheetCode(workSheetModel.getCode());
            individualFlowModel.setIndividualSn(individualModel.getSN1());
            if (StringUtils.isEmpty(individualFlowModel.getIndividualSn())){
                individualFlowModel.setIndividualSn(individualModel.getSN2());
            }
            individualFlowModel.setEmployeeNo(workSheetCreateParam.getEmployeeNo());
            individualFlowModel.setOper(FlowOrderConstant.INITIALIZE);
            individualFlowModel.setStatus("1");
            flowModelList.add(individualFlowModel);
        }
        individualFlowService.saveBatch(flowModelList);
    }


    /**
     * 更新工单状态
     *
     * @param workSheetParam
     */
    @Override
    @Transactional
    public void updateRunningWs(WorkSheetParam workSheetParam){
        //需要更新的model
        QueryWrapper<WorkSheetModel> queryUpdateWrapper = new QueryWrapper<>();
        queryUpdateWrapper.eq(WorkSheetModel.CODE, workSheetParam.getCode());
        WorkSheetModel updateWsModel = getOne(queryUpdateWrapper);
        if (updateWsModel != null) {
            updateWsModel.setStatus(workSheetParam.getStatus());
            if (workSheetParam.getStatus() == 1 && updateWsModel.getStartTime() == null) {
                updateWsModel.setStartTime(DateTool.getCurStringDate());
            }
            updateById(updateWsModel);
        }else{
            throw new CustomerException("更新工单不存在");
        }
    }


    @Override
    public List<WorkSheetModel> getRunningWs() {
        QueryWrapper<WorkSheetModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(WorkSheetModel.STATUS, 1);
        List<WorkSheetModel> runningWsList = list(queryWrapper);
        return runningWsList;
    }


    @Override
    public WorkSheetModel getWsByCode(String wsCode){
        QueryWrapper<WorkSheetModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(WorkSheetModel.CODE, wsCode);
        queryWrapper.eq(WorkSheetModel.STATUS, 1);
        WorkSheetModel workSheetModel = getOne(queryWrapper);
        if (workSheetModel == null) {
            throw new CustomerException("工单不存在，或者工单不在生产中");
        }
        return workSheetModel;
    }


    @Override
    public List<String> getWsSnList(List<DeviceIndividualModel> deviceIndividualModelList){
        List<String> snList = deviceIndividualModelList.stream().map(o -> {
            if (o.getSN1() != null) {
                return o.getSN1();
            }
            return o.getSN2();
        }).collect(Collectors.toList());
        return snList;
    }

}
