package com.yamo.cdsysmng.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.yamo.cdcommonmybatis.domain.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统日志
 * </p>
 *
 * @author mwh
 * @since 2023-08-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_sys_log")
public class SysLog extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 日志ID
     */
    private String logId;

    /**
     * 日志类型，字典：log_type
     */
    private String logType;

    /**
     * 操作行为，字典：opt_action
     */
    private String optAction;

    /**
     * 日志标签
     */
    private String logTag;

    /**
     * 日志来源：log_source
     */
    private String logSource;

    /**
     * 日志种类：log_category
     */
    private String logCategory;

    /**
     * 标题
     */
    private String title;

    /**
     * 远程地址
     */
    private String remoteAddr;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 请求uri
     */
    private String requestUri;

    /**
     * 操作方法
     */
    private String method;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 请求时长
     */
    private Integer time;

    /**
     * 异常信息
     */
    private String exception;

    /**
     * 响应体
     */
    private String response;

    /**
     * 客户端标识
     */
    private String clientId;
}
