package com.checkcode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.checkcode.entity.mpModel.IndividualFlowModel;
import com.checkcode.entity.param.FlowBoxUpRecordParam;
import com.checkcode.entity.param.FlowRecordParam;
import com.checkcode.entity.vo.FlowProgressVo;

import java.util.List;
import java.util.Map;

public interface IIndividualFlowService extends IService<IndividualFlowModel> {

    /**
     * 获得设备最后一条记录
     *
     * @param flowRecordParam
     * @return
     */
    IndividualFlowModel getDeviceLastRecord(FlowRecordParam flowRecordParam);

    /**
     * @param flowRecordParam
     * @return
     */
    FlowProgressVo recordFlowAndGetProcess(FlowRecordParam flowRecordParam, boolean isReset);

    /**
     * 通过sn获取每一个最新操作流程状态
     *
     * @param wsCode
     * @param snList
     * @return
     */
    List<IndividualFlowModel> getOperStatusBySnList(String wsCode, List<String> snList);

    /**
     * 装箱
     *
     * @param flowBoxUpRecordParam
     * @return
     */
    FlowProgressVo boxUp(FlowBoxUpRecordParam flowBoxUpRecordParam);

    /**
     * 当机码打印成功才去更新已经获取过设备个体为已获取状态
     *
     * @param flowRecordParam
     * @return
     */
    FlowProgressVo mechinePrintSuccessUpdateIndividualStatusOne(FlowRecordParam flowRecordParam);

    /**
     * 重置到初始化的话需要更新设备个体状态为未获取过
     *
     * @param flowRecordParam
     * @return
     */
    FlowProgressVo resetToInitializeNeedUpdateIndividualStatusZero(FlowRecordParam flowRecordParam);

    /**
     * 获得某个操作的状态数据
     *
     * @param wsCode
     * @param oper
     * @return
     */
    FlowProgressVo getFlowProgressVo(String wsCode, String oper);

    /**
     * 获取工单流程的Map，key为当前流程，val为当前流程的下一流程
     *
     * @param wsFlow
     * @return
     */
    Map<String, String> getWsFlowMap(String wsFlow);
}
