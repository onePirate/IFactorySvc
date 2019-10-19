package com.checkcode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.checkcode.entity.mpModel.DeviceIndividualModel;
import com.checkcode.entity.mpModel.WorkSheetModel;
import com.checkcode.entity.param.WorkSheetCreateParam;
import com.checkcode.entity.param.WorkSheetParam;

import java.util.List;

public interface IWorkSheetService extends IService<WorkSheetModel> {

    void createWorkSheet(List<DeviceIndividualModel> deviceIndividualList, String code, WorkSheetCreateParam workSheetCreateParam);

    void updateRunningWs(WorkSheetParam workSheetParam);

    List<WorkSheetModel> getRunningWs();
}
