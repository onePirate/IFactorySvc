package com.checkcode.common;

import java.util.HashMap;
import java.util.Map;

public class FlowOrderConstant {

    public static final Map<String,String> flowMap = new HashMap<>();
    static {
        flowMap.put("0_mechine_print","MECHINE_PRINT");
        flowMap.put("1_write_code","WRITE_CODE");
        flowMap.put("2_check_code","CHECK_CODE");
        flowMap.put("3_print_code","PRINT_CODE");
        flowMap.put("4_three_code_to_one","THREE_CODE_TO_ONE");
        flowMap.put("5_weighing","WEIGHING");
        flowMap.put("6_box_up","BOX_UP");
    }
}
