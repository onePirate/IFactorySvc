package com.checkcode.entity.mpModel;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tb_individual_flow")
public class IndividualFlowModel {


    private String worksheetCode;

    private String individualSn;

    private String employeeNo;

    private String oper;

    private String status;

    private Integer resetTimes;

    private String operTime;

    private String boxCode;

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        final IndividualFlowModel individualFlowModel = (IndividualFlowModel) obj;
        if (this == individualFlowModel) {
            return true;
        } else {
            return (this.individualSn.equals(individualFlowModel.individualSn) && this.oper == individualFlowModel.oper);
        }
    }
    @Override
    public int hashCode() {
        int hashno = 7;
        hashno = 13 * hashno + (individualSn == null ? 0 : individualSn.hashCode());
        return hashno;
    }

    public static final String PROPERTIES_WORKSHEET_CODE = "worksheet_code";
    public static final String PROPERTIES_INDIVIDUAL_SN = "individual_sn";
    public static final String PROPERTIES_EMPLOYEE_NO = "employee_no";
    public static final String PROPERTIES_OPER = "oper";
    public static final String PROPERTIES_STATUS = "status";
    public static final String PROPERTIES_RESET_TIMES = "reset_times";
    public static final String PROPERTIES_OPER_TIME = "oper_time";
    public static final String PROPERTIES_BOX_CODE = "box_code";

}
