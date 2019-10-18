package com.checkcode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.checkcode.entity.mpModel.IndividualFlowModel;
import com.checkcode.entity.param.FlowRecordParam;
import com.checkcode.entity.vo.FlowProgressVo;

public interface IIndividualFlowService extends IService<IndividualFlowModel> {

    /**
     * 获得设备最后一条记录
     *
     * @param individualSn
     * @return
     */
    IndividualFlowModel getDeviceLastRecord(String individualSn);

    /**
     * 获得设备最后一条记录
     *
     * @param flowRecordParam
     * @return
     */
    void recordFlow(FlowRecordParam flowRecordParam);

    /**
     *
     * @param flowRecordParam
     * @return
     */
    FlowProgressVo recordFlowAndGetProcess(FlowRecordParam flowRecordParam);
}
