package com.checkcode.web.controller;

import com.checkcode.common.entity.TokenModel;
import com.checkcode.common.tools.TokenTool;
import com.checkcode.controller.WorkSheetController;
import com.checkcode.entity.param.WorkSheetParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/")
public class PageController {

    @Autowired
    WorkSheetController workSheetController;

    @GetMapping(value = {"/", "login"})
    public String loginPage() {
        return "login";
    }

    @GetMapping("error")
    public String errorPage() {
        return "error";
    }

    @GetMapping("index")
    public ModelAndView index(String token) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ws/index");
        modelAndView.addObject("token", token);

        TokenModel tokenModel = TokenTool.getTokenInfo(token);
        modelAndView.addObject("empNo", tokenModel.getEmployeeNo());

        return modelAndView;
    }

    @GetMapping("wsCreate")
    public String wsCreate() {
        return "ws/wsCreate";
    }

    @GetMapping("wsList")
    public ModelAndView wsList(String code, Integer status) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ws/wsList");

        //getModel
        try {
            WorkSheetParam workSheetParam = new WorkSheetParam();
            workSheetParam.setCode(code);
            workSheetParam.setStatus(status);
            modelAndView.addObject("wsList", workSheetController.listWorkSheet(workSheetParam).getData());
        } catch (Exception ex) {
            log.warn("has error ", ex);
            List wsList = new ArrayList();
            modelAndView.addObject("wsList", wsList);
        }
        return modelAndView;
    }

}
