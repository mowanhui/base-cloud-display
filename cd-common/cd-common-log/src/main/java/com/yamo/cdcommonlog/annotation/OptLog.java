package com.yamo.cdcommonlog.annotation;


import com.yamo.cdcommonlog.enums.OptActionEnum;

import java.lang.annotation.*;

/**
 * 操作日志
 * @author mowanhui
 * @version 1.0
 * @date 2023/5/28 19:07
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OptLog {
    //日志内容
    String value() default "";
    //操作行为
    OptActionEnum optAction() default OptActionEnum.READ;
    //是否记录错误日志
    boolean error() default true;
}
