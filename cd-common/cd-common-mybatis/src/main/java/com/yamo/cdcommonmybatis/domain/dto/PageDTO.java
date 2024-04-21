package com.yamo.cdcommonmybatis.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Setter;

/**
 * @author mowanhui
 * @version 1.0
 * @date 2023/5/29 15:03
 */
@ApiModel(description = "分页")
@Setter
public class PageDTO {
    @ApiModelProperty("页码")
    private Integer pageNum;
    @ApiModelProperty("页大小")
    private Integer pageSize;

    public Integer getPageNum() {
        return pageNum==null||pageNum<1?1:pageNum;
    }

    public Integer getPageSize() {
        return pageSize==null||pageSize<1?10:pageSize;
    }
}
