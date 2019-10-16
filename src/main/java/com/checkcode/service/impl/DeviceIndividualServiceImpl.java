package com.checkcode.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.checkcode.dao.IDeviceIndividualDao;
import com.checkcode.entity.mpModel.DeviceIndividualModel;
import com.checkcode.service.IDeviceIndividualService;
import org.springframework.stereotype.Service;

@Service
public class DeviceIndividualServiceImpl extends ServiceImpl<IDeviceIndividualDao, DeviceIndividualModel> implements IDeviceIndividualService {
}
