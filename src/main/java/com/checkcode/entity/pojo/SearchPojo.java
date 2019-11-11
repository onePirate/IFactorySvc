package com.checkcode.entity.pojo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SearchPojo extends WSBasePojo {

    @NotEmpty(message="搜索值不能为空")
    private String searchVal;

}
