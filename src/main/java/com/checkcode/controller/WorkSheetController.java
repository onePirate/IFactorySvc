package com.checkcode.controller;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.checkcode.common.CustomerException;
import com.checkcode.common.StateEnum;
import com.checkcode.common.entity.Result;
import com.checkcode.common.tools.IdWorker;
import com.checkcode.common.tools.ResultTool;
import com.checkcode.entity.mpModel.DeviceIndividualModel;
import com.checkcode.entity.mpModel.WorkSheetModel;
import com.checkcode.entity.param.WorkSheetCreateParam;
import com.checkcode.entity.param.WorkSheetGroup;
import com.checkcode.entity.param.WorkSheetParam;
import com.checkcode.entity.pojo.DeviceIndividualPojo;
import com.checkcode.entity.vo.WorkSheetSimpleVo;
import com.checkcode.service.IDeviceIndividualService;
import com.checkcode.service.IWorkSheetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
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
    IWorkSheetService workSheetService;

    /**
     * 创建工单，并且添加工单包含的所有设备信息
     *
     * @param workSheetCreateParam
     * @param bindingResult
     * @return
     */
    @PostMapping("/create")
    public Result create(@Valid @RequestBody WorkSheetCreateParam workSheetCreateParam, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                return ResultTool.failedOnly(error.getDefaultMessage());
            }
        }
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
            createWorkSheet(deviceIndividualList, code, workSheetCreateParam);
        } catch (Exception ex) {
            log.error("create worksheet has error", ex);
            throw new CustomerException("创建工单失败");
        }
        return ResultTool.successWithMap(WorkSheetSimpleVo.builder().worksheetCode(code).build());
    }

    /**
     * todo 创建工单放在一个事物当中
     *
     * @param deviceIndividualList
     * @param code
     * @param workSheetCreateParam
     * @throws ParseException
     */
    private void createWorkSheet(List<DeviceIndividualModel> deviceIndividualList, String code, WorkSheetCreateParam workSheetCreateParam) throws ParseException {
        deviceIndividualService.saveBatch(deviceIndividualList);
        try {
            WorkSheetModel workSheetModel = new WorkSheetModel();
            BeanUtils.copyProperties(workSheetCreateParam, workSheetModel);
            workSheetModel.setCode(code);
            workSheetModel.setStatus(0);
            //todo 日期不对
            workSheetModel.setDeadline(workSheetCreateParam.getDeadline());
            workSheetService.save(workSheetModel);
        } catch (Exception ex) {
            log.error("create worksheet has error", ex);
            try {
                QueryWrapper<DeviceIndividualModel> removeWrapper = new QueryWrapper<>();
                removeWrapper.eq("worksheet_code", code);
                deviceIndividualService.remove(removeWrapper);
            } catch (Exception subEx) {
                log.error("remove individual device has error," + code + " worksheet data is invalid.", ex);
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
            //如果两个参数为null，则返回所有可运行的数据
            if (StringUtils.isEmpty(workSheetParam.getCode()) && workSheetParam.getStatus() == null) {
                //运行中的在前面（默认应该只有一条可运行）
                QueryWrapper<WorkSheetModel> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq(WorkSheetModel.STATUS, 1);
                List<WorkSheetModel> runningWsList = workSheetService.list(queryWrapper);

                QueryWrapper<WorkSheetModel> queryReadyWrapper = new QueryWrapper<>();
                queryReadyWrapper.in(WorkSheetModel.STATUS, 0, 2);
                List<WorkSheetModel> readyWsList = workSheetService.list(queryReadyWrapper);
                runningWsList.addAll(readyWsList);
                return ResultTool.successWithMap(runningWsList);
            }

            QueryWrapper<WorkSheetModel> queryWrapper = new QueryWrapper<>();
            if (!StringUtils.isEmpty(workSheetParam.getCode())) {
                queryWrapper.eq(WorkSheetModel.CODE, workSheetParam.getCode());
            }
            if (workSheetParam.getStatus() != null) {
                queryWrapper.eq(WorkSheetModel.STATUS, workSheetParam.getStatus());
            }
            List<WorkSheetModel> queryWsList = workSheetService.list(queryWrapper);
            return ResultTool.successWithMap(queryWsList);
        }
        return ResultTool.failed(StateEnum.REQ_HAS_ERR);
    }


    @PostMapping("/oper")
    public Result operWorkSheet(@Validated(WorkSheetGroup.LoginGroup.class) @RequestBody WorkSheetParam workSheetParam, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                return ResultTool.failedOnly(error.getDefaultMessage());
            }
        }
        //todo 现在是简单实现，后续考虑不可逆转流程
        workSheetService.updateRunningWs(workSheetParam);
        return ResultTool.success();
    }



}
