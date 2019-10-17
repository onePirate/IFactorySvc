package com.checkcode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.checkcode.common.CustomerException;
import com.checkcode.dao.IWorkSheetDao;
import com.checkcode.entity.mpModel.WorkSheetModel;
import com.checkcode.entity.param.WorkSheetParam;
import com.checkcode.service.IWorkSheetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkSheetServiceImpl extends ServiceImpl<IWorkSheetDao, WorkSheetModel> implements IWorkSheetService {

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
            updateById(updateWsModel);
        }else{
            throw new CustomerException("更新工单不存在");
        }
    }

}
