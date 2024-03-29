package com.checkcode.common.tools;


import com.checkcode.common.CustomerException;
import com.checkcode.common.StateEnum;
import com.checkcode.common.entity.ListResult;
import com.checkcode.common.entity.Result;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

public class ResultTool {

    /**
     * 成功不返回任何数据
     *
     * @return
     */
    public static Result success() {
        return customResp(StateEnum.OK, null);
    }

    /**
     * 成功返回map数据
     *
     * @return
     */
    public static Result successWithMap(Object map) {
        return customResp(StateEnum.OK, map);
    }

    /**
     * 成功返回list数据
     *
     * @return
     */
    public static Result successWithList(List<?> list, int pageIndex, int pageSize) {
        Result lResult = customResp(StateEnum.OK, null);
        lResult.setCount(list.size());
        int start = (pageIndex - 1) * pageSize;
        int end = pageIndex * pageSize;
        List subLst = new ArrayList();
        for (int i = start; i < end; i++) {
            if (i < list.size()) {
                subLst.add(list.get(i));
            }
        }
        lResult.setData(subLst);
        return lResult;
    }

    /**
     * 失败统一返回数据
     */
    public static Result failed() {
        return customResp(StateEnum.FAIL, null);
    }

    /**
     * 失败返回定义好的数据
     */
    public static Result failed(StateEnum stateEnum) {
        return customResp(stateEnum, null);
    }

    /**
     * 失败返回定义好的数据
     */
    public static Result failed(String enumError) {
        return customResp(StateEnum.valueOf(enumError), null);
    }

    /**
     * 自定义返回的数据
     */
    private static Result customResp(StateEnum stateEnum, Object data) {
        Result result = new Result();
        result.setCode(stateEnum.getCode());
        result.setMsg(stateEnum.getMsg());
        result.setData(data);
        return result;
    }

    /**
     * 自定义返回的数据
     */
    public static Result failedOnly(String errMsg) {
        Result result = new Result();
        result.setCode(500);
        result.setMsg(errMsg);
        return result;
    }

    /**
     * 参数校验统一处理
     *
     * @param bindingResult
     */
    public static void valid(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                throw new CustomerException(error.getDefaultMessage());
            }
        }
    }

}
