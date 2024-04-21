package com.yamo.cdcommonlog.enums;

/**
 * 日志来源
 * @author mowanhui
 * @version 1.0
 * @date 2023/5/27 21:19
 */
public enum LogSourceEnum {
    SYS_MNG("sys_mng","系统管理"),
    LAYER_CONFIG("layer_config","图层配置"),
    OTHER("other","其他系统")
    ;
    private String value;
    private String description;
    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
    LogSourceEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }
}
