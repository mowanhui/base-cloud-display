package com.yamo.cdcommoncore.constants.enums;

import java.util.Arrays;
import java.util.Optional;

public enum TimeType {

    ALL("all","0","所有",null),
    TODAY("today","1","今日",null),
    CURRENT_WEEK("week","2","本周",null),
    CURRENT_MONTH("mouth","3","本月",null),
    CURRENT_SEASON("season","4","本季",null),
    CURRENT_YEAR("year","5","本年",null),
    CURRENT_N_DAYS("days","6","最近n天",null),
    _12_HOUR("12hour","7","12小时",null),
    _24_HOUR("24hour","8","24小时",null),
    _48_HOUR("48hour","9","48小时",null),
    LAST_MONTH("last_mouth","10","上一个月",null),
    MONTH_OF_LAST_YEAR("mouth_of_last_year","11","上一年对应本月",null)
    ;
    private final String key;
    private final String code;
    private final String name;
    public Integer val;

    public String getKey() {
        return key;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Integer getVal() {
        return val;
    }

    public void setVal(Integer val) {
        this.val = val;
    }

    TimeType(String key, String code, String name, Integer val) {
        this.key=key;
        this.code = code;
        this.name = name;
        this.val=val;
    }

    public static TimeType getEventTypeEnumByKey(String key) {
        Optional<TimeType> first = Arrays.stream(TimeType.values())
                .filter(eventTypeEnum -> eventTypeEnum.getKey().equals(key))
                .findFirst();
        if (first.isPresent()) {
            return first.get();
        } else {
            throw new RuntimeException("状态码错误");
        }
    }

    public static TimeType getEventTypeEnumByCode(String code) {
        Optional<TimeType> first = Arrays.stream(TimeType.values())
                .filter(eventTypeEnum -> eventTypeEnum.getCode().equals(code))
                .findFirst();
        if (first.isPresent()) {
            return first.get();
        } else {
            throw new RuntimeException("状态码错误");
        }
    }

    public static String getDescriptionByCode(String code) {
        return TimeType.getEventTypeEnumByCode(code).getName();
    }
}
