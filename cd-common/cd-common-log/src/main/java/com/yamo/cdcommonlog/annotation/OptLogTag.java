package com.yamo.cdcommonlog.annotation;

import com.yamo.cdcommonlog.enums.LogSourceEnum;

import java.lang.annotation.*;

/**
 * 操作项
 * @author mowanhui
 * @version 1.0
 * @date 2023/5/28 19:13
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OptLogTag {
    String value() default "";
    LogSourceEnum logSource() default LogSourceEnum.OTHER;
}
