package com.yamo.cdcommonsupport.annotation.enable;

import com.yamo.cdcommonsupport.aspect.AuthAspect;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author mowanhui
 * @version 1.0
 * @date 2024/3/26 19:13
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({
        AuthAspect.class
})
public @interface EnableAuth {
}
