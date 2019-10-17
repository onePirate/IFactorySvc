package com.checkcode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.checkcode.entity.mpModel.WorkSheetModel;
import com.checkcode.entity.param.WorkSheetParam;

public interface IWorkSheetService extends IService<WorkSheetModel> {

    void updateRunningWs(WorkSheetParam workSheetParam);

}
