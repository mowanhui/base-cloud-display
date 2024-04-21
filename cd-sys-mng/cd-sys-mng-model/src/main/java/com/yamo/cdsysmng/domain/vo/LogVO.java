package com.yamo.cdsysmng.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("日志信息")
@Data
public class LogVO {
    /**
     * 日志ID
     */
    @ApiModelProperty("logId")
    private String logId;

    /**
     * 日志类型，字典：log_type
     */
    @ApiModelProperty("日志类型，字典：log_type")
    private String logType;

    @ApiModelProperty("日志类型名称")
    private String logTypeName;

    /**
     * 操作行为，字典：opt_action
     */
    @ApiModelProperty("操作行为，字典：opt_action")
    private String optAction;

    @ApiModelProperty("操作行为名称")
    private String optActionName;
    /**
     * 日志标签
     */
    @ApiModelProperty("日志标签")
    private String logTag;

    /**
     * 日志来源：log_source
     */
    @ApiModelProperty("日志来源：log_source")
    private String logSource;

    @ApiModelProperty("日志来源")
    private String logSourceName;

    /**
     * 日志种类：log_category
     */
    @ApiModelProperty("日志种类：log_category")
    private String logCategory;

    @ApiModelProperty("日志种类")
    private String logCategoryName;

    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;

    /**
     * 远程地址
     */
    @ApiModelProperty("远程地址")
    private String remoteAddr;

    /**
     * 用户代理
     */
    @ApiModelProperty("用户代理")
    private String userAgent;

    /**
     * 请求uri
     */
    @ApiModelProperty("请求uri")
    private String requestUri;

    /**
     * 操作方法
     */
    @ApiModelProperty("操作方法")
    private String method;

    /**
     * 请求参数
     */
    @ApiModelProperty("请求参数")
    private String params;

    /**
     * 请求时长
     */
    @ApiModelProperty("请求时长")
    private Integer time;

    /**
     * 异常信息
     */
    @ApiModelProperty("异常信息")
    private String exception;

    /**
     * 响应体
     */
    @ApiModelProperty("响应体")
    private String response;

    /**
     * 客户端标识
     */
    @ApiModelProperty("客户端标识")
    private String clientId;
}
