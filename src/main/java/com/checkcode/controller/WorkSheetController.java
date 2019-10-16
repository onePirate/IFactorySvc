package com.checkcode.controller;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.checkcode.common.CustomerException;
import com.checkcode.common.StateEnum;
import com.checkcode.common.entity.Result;
import com.checkcode.common.tools.DateTool;
import com.checkcode.common.tools.IdWorker;
import com.checkcode.common.tools.ResultTool;
import com.checkcode.dao.IWorkSheetDao;
import com.checkcode.entity.mpModel.DeviceIndividualModel;
import com.checkcode.entity.mpModel.WorkSheetModel;
import com.checkcode.entity.param.WorkSheetParam;
import com.checkcode.entity.pojo.DeviceIndividualPojo;
import com.checkcode.entity.vo.WorkSheetSimpleVo;
import com.checkcode.service.IDeviceIndividualService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/worksheet")
public class WorkSheetController {

    @Autowired
    IDeviceIndividualService deviceIndividualService;
    @Autowired
    IWorkSheetDao workSheetDao;

    /**
     * 创建工单，并且添加工单包含的所有设备信息
     *
     * @param workSheetParam
     * @param bindingResult
     * @return
     */
    @PostMapping("/create")
    public Result create(@Valid @RequestBody WorkSheetParam workSheetParam, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                return ResultTool.failedOnly(error.getDefaultMessage());
            }
        }
        String code = "ws" + IdWorker.getCodeByUUId();
        //首先解析Excel数据是否正确
        //如果解析数据有错，则提示Excel数据有错
        List<DeviceIndividualPojo> deviceIndividualPojoList = simpleRead(workSheetParam.getFileUrl(), code);
        List<DeviceIndividualModel> deviceIndividualList = deviceIndividualPojoList.stream().map(o -> {
            DeviceIndividualModel deviceIndividualModel = new DeviceIndividualModel();
            BeanUtils.copyProperties(o, deviceIndividualModel);
            return deviceIndividualModel;
        }).collect(Collectors.toList());
        //如果解析都正确，则插入数据到tb_device_individual中
        //如果插入报错，直接返回创建工单失败
        try {
            createWorkSheet(deviceIndividualList, code, workSheetParam);
        } catch (Exception ex) {
            log.error("create worksheet has error", ex);
            throw new CustomerException("创建工单失败");
        }
        return ResultTool.successWithMap(WorkSheetSimpleVo.builder().worksheetCode(code).build());
    }

    /**
     * 创建工单放在一个事物当中
     *
     * @param deviceIndividualList
     * @param code
     * @param workSheetParam
     * @throws ParseException
     */
    private void createWorkSheet(List<DeviceIndividualModel> deviceIndividualList, String code, WorkSheetParam workSheetParam) throws ParseException {
        deviceIndividualService.saveBatch(deviceIndividualList);
        try {
            WorkSheetModel workSheetModel = new WorkSheetModel();
            BeanUtils.copyProperties(workSheetParam, workSheetModel);
            workSheetModel.setCode(code);
            workSheetModel.setStatus(0);
            //todo 日期不对
            workSheetModel.setDeadline(DateTool.parseDate(workSheetParam.getDeadline()));
            workSheetDao.insert(workSheetModel);
        } catch (Exception ex) {
            log.error("create worksheet has error", ex);
            try {
                QueryWrapper<DeviceIndividualModel> removeWrapper = new QueryWrapper<>();
                removeWrapper.eq("worksheet_code", code);
                deviceIndividualService.remove(removeWrapper);
            } catch (Exception subEx) {
                log.error("remove individual device has error,"+code+" worksheet data is invalid.", ex);
            }
            throw new CustomerException("创建工单失败");
        }
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
                if (StringUtils.isEmpty(deviceIndividualPojo.getSN1()) && StringUtils.isEmpty(deviceIndividualPojo.getSN2())) {
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

}
