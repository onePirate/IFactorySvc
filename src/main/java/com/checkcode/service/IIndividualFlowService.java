package com.checkcode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.checkcode.entity.mpModel.IndividualFlowModel;
import com.checkcode.entity.param.FlowBoxUpRecordParam;
import com.checkcode.entity.param.FlowRecordParam;
import com.checkcode.entity.vo.FlowProgressVo;

import java.util.List;

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

    /**
     * 通过sn获取每一个最新操作流程状态
     *
     * @param snList
     * @return
     */
    List<IndividualFlowModel> getOperStatusBySnList(List<String> snList);

    FlowProgressVo boxUp(FlowBoxUpRecordParam flowBoxUpRecordParam);
}
