package com.yamo.cdcommonsupport.annotation.enable;

import com.yamo.cdcommonsupport.handler.GlobalExceptionHandler;
import com.yamo.cdcommonsupport.handler.GlobalResponseHandler;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({
    GlobalResponseHandler.class,
    GlobalExceptionHandler.class
})
public @interface EnableControllerResponse {
}
