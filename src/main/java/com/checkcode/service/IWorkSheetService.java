package com.checkcode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.checkcode.entity.mpModel.DeviceIndividualModel;
import com.checkcode.entity.mpModel.WorkSheetModel;
import com.checkcode.entity.param.WorkSheetCreateParam;
import com.checkcode.entity.param.WorkSheetParam;

import java.util.List;

public interface IWorkSheetService extends IService<WorkSheetModel> {

    void createWorkSheet(List<DeviceIndividualModel> deviceIndividualList, WorkSheetCreateParam workSheetCreateParam);

    void updateRunningWs(WorkSheetParam workSheetParam);

    List<WorkSheetModel> getRunningWs();

    /**
     * 根据工单号获取正在生产中的工单
     *
     * @param wsCode
     * @return
     */
    WorkSheetModel getWsByCode(String wsCode);

    /**
     * 获取工单的SN列表
     *
     * @param deviceIndividualModelList
     * @return
     */
    List<String> getWsSnList(List<DeviceIndividualModel> deviceIndividualModelList);
}
