package com.checkcode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.checkcode.entity.mpModel.IndividualFlowModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IIndividualFlowDao extends BaseMapper<IndividualFlowModel> {

    /**
     * @param snList
     * @param oper
     * @param status 如果等于空，表示查询所有状态
     * @return
     */
    List<IndividualFlowModel> getAnyOperFinishedData(@Param("snList") List<String> snList, @Param("wsCode") String wsCode, @Param("oper") String oper, @Param("status") String status);

    /**
     * 获得流程状态
     *
     * @param snList
     * @return
     */
    List<IndividualFlowModel> getOperStatusBySnList(@Param("wsCode") String wsCode, @Param("snList") List<String> snList);

}
