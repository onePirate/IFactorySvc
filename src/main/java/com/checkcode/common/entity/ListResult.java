package com.checkcode.common.entity;

import lombok.Data;

/**
 * @Author: gaodw
 * @Date: 20:29 2019/7/18
 * @Desc: 如果是List则返回
 */
@Data
public class ListResult {

    //总条数
    protected long totalRow;

    //列表
    protected Object list;

}
