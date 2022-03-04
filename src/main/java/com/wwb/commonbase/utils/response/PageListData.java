package com.wwb.commonbase.utils.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author weibo
 */
@Data
@ApiModel(value = "PageListData<T>", description = "分页数据集")
public class PageListData<T> implements Serializable {


    @ApiModelProperty(name = "total", value = "总记录数")
    private Long total;


    @ApiModelProperty(name = "list", value = "数据集合")
    private List<T> list;

    public PageListData(Long total, List<T> list) {
        this.total = total;
        this.list = list;
    }

    public PageListData() {
    }
}