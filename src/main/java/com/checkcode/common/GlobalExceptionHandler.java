package com.checkcode.common;

import com.checkcode.common.entity.Result;
import com.checkcode.common.tools.ResultTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @Author: gaodw
 * @Date: 20:29 2019/7/18
 * @Desc: 全局异常处理
 */
@Slf4j
@ControllerAdvice(annotations = RestController.class)
@ResponseBody
public class GlobalExceptionHandler {

    /**
     * 默认统一异常处理方法
     * @param e 默认CustomerException异常对象
     * @return
     */
    @ExceptionHandler(value = CustomerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result runtimeExceptionHandler(CustomerException e) {
        log.error("server has error!",e);
        return ResultTool.failedOnly(e.getMessage());
    }
    /**
     * 默认统一异常处理方法
     * @param e 默认CustomerException异常对象
     * @return
     */
    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result httpMediaTypeNotSupportedExceptionExceptionHandler(HttpMediaTypeNotSupportedException e) {
        log.error("server has error!",e);
        return ResultTool.failedOnly("仅支持application/json类型");
    }
    /**
     * 默认统一异常处理方法
     * @param e SQLIntegrityConstraintViolationException
     * @return
     */
    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result sqlIntegrityConstraintViolationExceptionHandler(SQLIntegrityConstraintViolationException e) {
        log.error("server has error!",e);
        return ResultTool.failedOnly("员工账号重复");
    }

    /**
     * 默认统一异常处理方法
     * @param e 默认CustomerException异常对象
     * @return
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result runtimeExceptionHandler(ConstraintViolationException e) {
        log.error("param check has error!",e);
        return ResultTool.failedOnly("参数有误");
    }
    /**
     * 默认统一异常处理方法
     * @param e 默认CustomerException异常对象
     * @return
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result runtimeExceptionHandler(HttpMessageNotReadableException e) {
        log.error("param check has error!",e);
        return ResultTool.failedOnly("参数有误");
    }
    /**
     * 默认统一异常处理方法
     * @param e 默认CustomerException异常对象
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result runtimeExceptionHandler(Exception e) {
        log.error("server has error!",e);
        return ResultTool.failed();
    }


}
