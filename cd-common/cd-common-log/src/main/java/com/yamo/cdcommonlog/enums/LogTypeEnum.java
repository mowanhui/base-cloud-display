package com.yamo.cdcommonlog.enums;

/**
 * 日志类型
 * @author mowanhui
 * @version 1.0
 * @date 2023/5/27 21:10
 */
public enum LogTypeEnum{
    NORMAL("normal","正常"),
    WARNING("warning","警告"),
    ERROR("error","错误")
    ;
    private String value;
    private String description;
    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
    LogTypeEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }
}
