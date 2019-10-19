package com.checkcode.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.checkcode.entity.mpModel.DeviceIndividualModel;

import java.util.List;

public interface IDeviceIndividualService extends IService<DeviceIndividualModel> {

    /**
     * 根据条件获取符合添加的设备
     *
     * @param searchVal
     * @return
     */
    List<DeviceIndividualModel> getIndividualListByCondition(String searchVal);
}
