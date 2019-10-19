package com.checkcode.entity.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FlowProgressVo {

    private Integer total;

    private Integer finished;

    private Object info;

}
