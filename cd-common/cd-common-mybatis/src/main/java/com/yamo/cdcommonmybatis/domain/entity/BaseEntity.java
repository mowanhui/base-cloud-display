package com.yamo.cdcommonmybatis.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author mowanhui
 * @version 1.0
 * @date 2023/8/17 11:42
 */
@Data
public class BaseEntity {
    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 删除人
     */
    private String deleteBy;

    /**
     * 删除时间
     */
    private LocalDateTime deleteTime;

    /**
     * 逻辑删除
     */
    private String isDeleted;
}
