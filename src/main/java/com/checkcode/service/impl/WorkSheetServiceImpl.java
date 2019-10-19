package com.checkcode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.checkcode.common.CustomerException;
import com.checkcode.common.tools.DateTool;
import com.checkcode.dao.IWorkSheetDao;
import com.checkcode.entity.mpModel.DeviceIndividualModel;
import com.checkcode.entity.mpModel.WorkSheetModel;
import com.checkcode.entity.param.WorkSheetCreateParam;
import com.checkcode.entity.param.WorkSheetParam;
import com.checkcode.service.IDeviceIndividualService;
import com.checkcode.service.IWorkSheetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;

@Service
public class WorkSheetServiceImpl extends ServiceImpl<IWorkSheetDao, WorkSheetModel> implements IWorkSheetService {

    @Autowired
    IDeviceIndividualService deviceIndividualService;


    /**
     * 创建工单
     *
     * @param deviceIndividualList
     * @param code
     * @param workSheetCreateParam
     * @throws ParseException
     */
    @Override
    @Transactional
    public void createWorkSheet(List<DeviceIndividualModel> deviceIndividualList, String code, WorkSheetCreateParam workSheetCreateParam){
        deviceIndividualService.saveBatch(deviceIndividualList);
        WorkSheetModel workSheetModel = new WorkSheetModel();
        BeanUtils.copyProperties(workSheetCreateParam, workSheetModel);
        workSheetModel.setCode(code);
        workSheetModel.setStatus(0);
        workSheetModel.setDeadline(workSheetCreateParam.getDeadline());
        save(workSheetModel);
    }


    /**
     * 更新工单状态
     *
     * @param workSheetParam
     */
    @Override
    @Transactional
    public void updateRunningWs(WorkSheetParam workSheetParam){
        //如果有运行中的，先暂停再更新当前工单为运行中
        if (workSheetParam.getStatus() == 1) {
            QueryWrapper<WorkSheetModel> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(WorkSheetModel.STATUS, 1);
            WorkSheetModel workSheetModel = getOne(queryWrapper);
            if (workSheetModel != null) {
                workSheetModel.setStatus(2);
                updateById(workSheetModel);
            }
        }

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

}
