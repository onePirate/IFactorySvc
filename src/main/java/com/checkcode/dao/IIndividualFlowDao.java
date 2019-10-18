package com.checkcode.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.checkcode.entity.mpModel.IndividualFlowModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IIndividualFlowDao extends BaseMapper<IndividualFlowModel> {

    List<IndividualFlowModel> getAnyOperFinishedData(@Param("snList") List<String> snList, @Param("oper") String oper);

}
