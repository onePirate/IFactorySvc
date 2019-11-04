package com.checkcode.common;

import java.util.HashMap;
import java.util.Map;

public class FlowOrderConstant {

    public static final Map<String,String> flowMap = new HashMap<>();
    static {
        flowMap.put("-1_initialize","INITIALIZE");
        flowMap.put("0_mechine_print","MECHINE_PRINT");
        flowMap.put("1_write_code","WRITE_CODE");
        flowMap.put("2_check_code","CHECK_CODE");
        flowMap.put("3_print_code","PRINT_CODE");
        flowMap.put("4_three_code_to_one","THREE_CODE_TO_ONE");
        flowMap.put("5_weighing","WEIGHING");
        flowMap.put("6_box_up","BOX_UP");
    }

    public static final String INITIALIZE = "-1_initialize";
    public static final String ZERO = "0_mechine_print";
    public static final String FIFTH = "5_weighing";
    public static final String SIXTH = "6_box_up";


    public static final String FLOW_SPLIT_CHAR = ":";

}
