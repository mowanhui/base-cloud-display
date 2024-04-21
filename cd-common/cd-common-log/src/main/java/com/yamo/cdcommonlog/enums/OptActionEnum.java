package com.yamo.cdcommonlog.enums;

/**
 * 日志操作行为类型
 * @author mowanhui
 * @version 1.0
 * @date 2023/5/29 1:12
 */
public enum OptActionEnum {
    INSERT("insert","添加"),
    UPDATE("update","更新"),
    DELETE("delete","删除"),
    READ("read","查询"),
    OTHER("other","其他")
    ;
    private final String value;
    private final String description;

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    OptActionEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }
}
