package com.yamo.cdcommonlog.pojo;

import com.yamo.cdcommonlog.enums.LogCategoryEnums;
import com.yamo.cdcommonlog.enums.LogSourceEnum;
import com.yamo.cdcommonlog.enums.LogTypeEnum;
import com.yamo.cdcommonlog.enums.OptActionEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author mowanhui
 * @version 1.0
 * @date 2023/5/25 22:36
 */
@Data
public class LogInfo implements Serializable {
    private static final long serialVersionUID = 5777027594715006255L;
    /**
     * 日志类型
     */
    private LogTypeEnum logType;

    /**
     * 操作行为
     */
    private OptActionEnum optAction;

    /**
     * 日志标签
     */
    private String logTag;

    /**
     * 日志来源
     */
    private LogSourceEnum logSource;

    /**
     * 日志种类
     */
    private LogCategoryEnums logCategory;

    /**
     * 日志标题
     */
    private String title;

    /**
     * 操作ip地址
     */
    private String remoteAddr;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 请求URI
     */
    private String requestUri;

    /**
     * 操作方式
     */
    private String method;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 方法执行时间
     */
    private Long time;

    /**
     * 异常信息
     */
    private String exception;

    /**
     * 响应体
     */
    private String response;

    private Date createTime;

    private String createBy;

    private Date updateTime;

    private String updateBy;
}
