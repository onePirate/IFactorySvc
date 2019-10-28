package com.checkcode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.checkcode.common.CustomerException;
import com.checkcode.dao.IDeviceIndividualDao;
import com.checkcode.entity.mpModel.DeviceIndividualModel;
import com.checkcode.entity.mpModel.WorkSheetModel;
import com.checkcode.service.IDeviceIndividualService;
import com.checkcode.service.IWorkSheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceIndividualServiceImpl extends ServiceImpl<IDeviceIndividualDao, DeviceIndividualModel> implements IDeviceIndividualService {

    @Autowired
    IWorkSheetService workSheetService;

    @Override
    public List<DeviceIndividualModel> getIndividualListByCondition(String searchVal){
        //查询当前正在运行的工单号
        List<WorkSheetModel> runningWs = workSheetService.getRunningWs();

        if (runningWs == null || runningWs.size() == 0) {
            throw new CustomerException("没有正在生产中的工单");
        }

        QueryWrapper<DeviceIndividualModel> queryIndividualListWrapper = new QueryWrapper<>();
        queryIndividualListWrapper.eq(DeviceIndividualModel.PROPERTIES_WORKSHEET_CODE, runningWs.get(0).getCode());
        String searchValue = searchVal == null ? "" : searchVal;
        queryIndividualListWrapper.and(properties ->
                properties.eq(DeviceIndividualModel.PROPERTIES_SN1, searchValue)
                        .or().eq(DeviceIndividualModel.PROPERTIES_SN2, searchValue)
                        .or().eq(DeviceIndividualModel.PROPERTIES_IMEI1, searchValue)
                        .or().eq(DeviceIndividualModel.PROPERTIES_IMEI2, searchValue)
                        .or().eq(DeviceIndividualModel.PROPERTIES_IMEI3, searchValue)
                        .or().eq(DeviceIndividualModel.PROPERTIES_IMEI4, searchValue)
        );
        List<DeviceIndividualModel> deviceIndividualModelList = list(queryIndividualListWrapper);
        return deviceIndividualModelList;
    }


}
