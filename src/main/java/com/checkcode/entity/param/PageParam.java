package com.checkcode.entity.param;

import lombok.Data;

@Data
public class PageParam {

    /**
     * 页码
     */
    protected Integer page;

    /**
     * 每页数量
     */
    protected Integer limit;

}
