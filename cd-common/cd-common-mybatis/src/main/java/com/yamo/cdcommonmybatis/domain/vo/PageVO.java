package com.yamo.cdcommonmybatis.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("分页")
@Data
public class PageVO<T>{
    //总记录数
    @ApiModelProperty("总记录数")
    private Long total;
    //总页数
    @ApiModelProperty("总页数")
    private Integer pages;
    //当前页数
    @ApiModelProperty("当前页数")
    private Integer pageNum;
    //每页条数
    @ApiModelProperty("每页条数")
    private Integer pageSize;
    //数据列表
    @ApiModelProperty("数据列表")
    private List<T> list;

}
