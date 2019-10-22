package com.checkcode.controller;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.checkcode.common.CustomerException;
import com.checkcode.common.StateEnum;
import com.checkcode.common.entity.Result;
import com.checkcode.common.tools.IdWorker;
import com.checkcode.common.tools.ResultTool;
import com.checkcode.entity.mpModel.CustomerModel;
import com.checkcode.entity.mpModel.DeviceIndividualModel;
import com.checkcode.entity.mpModel.WorkSheetModel;
import com.checkcode.entity.param.WorkSheetCreateParam;
import com.checkcode.entity.param.WorkSheetGroup;
import com.checkcode.entity.param.WorkSheetParam;
import com.checkcode.entity.pojo.DeviceIndividualPojo;
import com.checkcode.entity.vo.WorkSheetSimpleVo;
import com.checkcode.entity.vo.WorkSheetVo;
import com.checkcode.service.ICustomerService;
import com.checkcode.service.IDeviceIndividualService;
import com.checkcode.service.IWorkSheetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/worksheet")
public class WorkSheetController {

    @Autowired
    IDeviceIndividualService deviceIndividualService;
    @Autowired
    IWorkSheetService workSheetService;
    @Autowired
    ICustomerService customerService;

    /**
     * 创建工单，并且添加工单包含的所有设备信息
     *
     * @param workSheetCreateParam
     * @param bindingResult
     * @return
     */
    @PostMapping("/create")
    public Result createWorkSheet(@Valid @RequestBody WorkSheetCreateParam workSheetCreateParam, BindingResult bindingResult) {
        ResultTool.valid(bindingResult);
        String code = "ws" + IdWorker.getCodeByUUId();
        //首先解析Excel数据是否正确
        //如果解析数据有错，则提示Excel数据有错
        List<DeviceIndividualPojo> deviceIndividualPojoList = simpleRead(workSheetCreateParam.getFileUrl(), code);
        List<DeviceIndividualModel> deviceIndividualList = deviceIndividualPojoList.stream().map(o -> {
            DeviceIndividualModel deviceIndividualModel = new DeviceIndividualModel();
            BeanUtils.copyProperties(o, deviceIndividualModel);
            return deviceIndividualModel;
        }).collect(Collectors.toList());
        //如果解析都正确，则插入数据到tb_device_individual中
        //如果插入报错，直接返回创建工单失败
        try {
            workSheetService.createWorkSheet(deviceIndividualList, code, workSheetCreateParam);
        } catch (Exception ex) {
            log.error("create worksheet has error", ex);
            throw new CustomerException("创建工单失败");
        }
        return ResultTool.successWithMap(WorkSheetSimpleVo.builder().worksheetCode(code).build());
    }

    /**
     * 简单读取 (同步读取)
     */
    private List<DeviceIndividualPojo> simpleRead(String filePath, String code) {
        List<DeviceIndividualPojo> deviceIndividualList = new ArrayList<>();
        try {
            Sheet sheet = new Sheet(1, 1, DeviceIndividualPojo.class);
            List<Object> readList = EasyExcelFactory.read(new FileInputStream(filePath), sheet);
            for (Object obj : readList) {
                DeviceIndividualPojo deviceIndividualPojo = (DeviceIndividualPojo) obj;
                if (StringUtils.isEmpty(deviceIndividualPojo.getSN1())
                    && StringUtils.isEmpty(deviceIndividualPojo.getSN2())
                    && StringUtils.isEmpty(deviceIndividualPojo.getIMEI1())
                    && StringUtils.isEmpty(deviceIndividualPojo.getIMEI2())
                    && StringUtils.isEmpty(deviceIndividualPojo.getIMEI3())
                    && StringUtils.isEmpty(deviceIndividualPojo.getIMEI4())
                    && StringUtils.isEmpty(deviceIndividualPojo.getBTADDRESS())
                    && StringUtils.isEmpty(deviceIndividualPojo.getETHERNNETMACADDRESS())
                    && StringUtils.isEmpty(deviceIndividualPojo.getMEID())
                    && StringUtils.isEmpty(deviceIndividualPojo.getESN())
                    && StringUtils.isEmpty(deviceIndividualPojo.getEXTRA1())
                    && StringUtils.isEmpty(deviceIndividualPojo.getEXTRA2())
                    && StringUtils.isEmpty(deviceIndividualPojo.getEXTRA3())) {
                    continue;
                }
                if (StringUtils.isEmpty(deviceIndividualPojo.getSN1()) && StringUtils.isEmpty(deviceIndividualPojo.getSN2())){
                    throw new CustomerException(StateEnum.FAIL_EXCEL_DATA_EX);
                }
                deviceIndividualPojo.setWorksheetCode(code);
                deviceIndividualList.add(deviceIndividualPojo);
            }
            return deviceIndividualList;
        } catch (FileNotFoundException e) {
            throw new CustomerException(StateEnum.FAIL);
        }
    }

    /**
     * 查询工单状态
     * 如果两个参数为null，则返回所有可运行(0,1,2)的数据
     * 运行中的在前面（默认应该只有一条可运行）
     *
     * @param workSheetParam
     * @return
     */
    @PostMapping("/list")
    public Result listWorkSheet(@RequestBody WorkSheetParam workSheetParam) {
        if (workSheetParam != null) {
            List<WorkSheetVo> workSheetVoList = new ArrayList<>();
            //如果两个参数为null，则返回所有可运行的数据
            if (StringUtils.isEmpty(workSheetParam.getCode()) && workSheetParam.getStatus() == null) {
                List<WorkSheetModel> runningWsList = workSheetService.getRunningWs();

                QueryWrapper<WorkSheetModel> queryReadyWrapper = new QueryWrapper<>();
                queryReadyWrapper.in(WorkSheetModel.STATUS, 0, 2);
                List<WorkSheetModel> readyWsList = workSheetService.list(queryReadyWrapper);
                runningWsList.addAll(readyWsList);

                if (runningWsList != null && runningWsList.size()>0) {
                    workSheetVoList = getWsVoDetailList(runningWsList);
                }

                return ResultTool.successWithMap(workSheetVoList);
            }

            QueryWrapper<WorkSheetModel> queryWrapper = new QueryWrapper<>();
            if (!StringUtils.isEmpty(workSheetParam.getCode())) {
                queryWrapper.eq(WorkSheetModel.CODE, workSheetParam.getCode());
            }
            if (workSheetParam.getStatus() != null) {
                queryWrapper.eq(WorkSheetModel.STATUS, workSheetParam.getStatus());
            }
            List<WorkSheetModel> queryWsList = workSheetService.list(queryWrapper);
            if (queryWsList != null && queryWsList.size()>0) {
                workSheetVoList = getWsVoDetailList(queryWsList);
            }
            return ResultTool.successWithMap(workSheetVoList);
        }
        return ResultTool.failed(StateEnum.REQ_HAS_ERR);
    }

    /**
     * 获取对应的客户信息
     */
    private List<WorkSheetVo> getWsVoDetailList(List<WorkSheetModel> queryWsList){
        List<String> customerNos = queryWsList.stream().map(WorkSheetModel::getCustomerNo).collect(Collectors.toList());
        QueryWrapper<CustomerModel> queryCustomerWrapper = new QueryWrapper<>();
        queryCustomerWrapper.in(CustomerModel.PROPERTIES_CUSTOMER_NO, customerNos);
        List<CustomerModel> customerModelList = customerService.list(queryCustomerWrapper);
        Map<String, CustomerModel> cusMap = new HashMap<>();
        int cusSize = customerModelList.size();
        for (int i = 0; i < cusSize; i++) {
            cusMap.put(customerModelList.get(i).getCustomerNo(), customerModelList.get(i));
        }

        //组装对应的客户信息
        List<WorkSheetVo> workSheetVoList = new ArrayList<>();
        int wsSize =  queryWsList.size();
        for (int i = 0; i < wsSize; i++) {
            WorkSheetModel workSheetModel = queryWsList.get(i);
            WorkSheetVo workSheetVo = new WorkSheetVo();
            BeanUtils.copyProperties(workSheetModel,workSheetVo);
            CustomerModel customerModel = cusMap.get(workSheetModel.getCustomerNo());
            if (customerModel != null) {
                workSheetVo.setCusName(customerModel.getName());
                workSheetVo.setCusAddress(customerModel.getAddress());
                workSheetVo.setCusCompany(customerModel.getCompany());
                workSheetVo.setCusPhone(customerModel.getPhone());
                workSheetVo.setCusIcon(customerModel.getIcon());
            }
            workSheetVoList.add(workSheetVo);
        }
        return workSheetVoList;
    }


    @PostMapping("/oper")
    public Result operWorkSheet(@Validated(WorkSheetGroup.LoginGroup.class) @RequestBody WorkSheetParam workSheetParam, BindingResult bindingResult) {
        ResultTool.valid(bindingResult);
        //todo 现在是简单实现，后续考虑不可逆转流程
        workSheetService.updateRunningWs(workSheetParam);
        return ResultTool.success();
    }



}
