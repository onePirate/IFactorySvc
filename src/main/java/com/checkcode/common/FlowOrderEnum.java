package com.checkcode.common;

import lombok.Getter;

@Getter
public enum FlowOrderEnum {

    /**
     * 记录当前流程的上个流程和下个流程
     */
    MECHINE_PRINT("","1_write_code"),
    WRITE_CODE("0_mechine_print","2_check_code"),
    CHECK_CODE("1_write_code","3_print_code"),
    PRINT_CODE("2_check_code","4_three_code_to_one"),
    THREE_CODE_TO_ONE("3_print_code","5_weighing"),
    WEIGHING("4_three_code_to_one","6_box_up"),
    BOX_UP("5_weighing",""),

    ;

    FlowOrderEnum(String lastFlow, String nextFlow) {
        this.lastFlow = lastFlow;
        this.nextFlow = nextFlow;
    }

    /**
     * 当前流程的上个流程
     */
    private String lastFlow;

    /**
     * 当前流程的上个流程
     */
    private String nextFlow;



}
