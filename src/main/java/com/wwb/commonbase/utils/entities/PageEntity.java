package com.wwb.commonbase.utils.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * @author xxx
 */
@ApiModel(value = "PageEntity", description = "分页条件")
@Data
@Builder
public class PageEntity {
    @Tolerate
    public PageEntity() {
    }

    /**
     * 页码
     */
    @ApiModelProperty("查询页码")
    private int pageindex;

    /**
     * 分页大小
     */
    @ApiModelProperty("分页大小，即每页数据条数")
    private int pagesize;
}
