package com.yamo.cdcommonlog.enums;

/**
 * 日志种类
 * @author mowanhui
 * @version 1.0
 * @date 2023/5/27 21:32
 */
public enum LogCategoryEnums {
    LOGIN("login","登录日志"),
    SERVICE("service","服务日志"),
    SYSTEM("system","系统日志")
            ;
    private final String value;
    private final String description;

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    LogCategoryEnums(String value, String description) {
        this.value = value;
        this.description = description;
    }
}
