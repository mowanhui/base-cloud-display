package com.yamo.cdcommonsupport.handler;

import cn.hutool.json.JSONUtil;
import com.yamo.cdcommoncore.result.ResultVO;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice(annotations = {RestController.class})
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object data, MethodParameter returnType, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest request, ServerHttpResponse response) {
        //void返回处理
        if(returnType.getGenericParameterType().equals(void.class)){
            return ResultVO.ok();
        }
        //通用返回实体类处理
        if(data instanceof ResultVO){
            return data;
        }
        // String类型不能直接包装
        if(returnType.getGenericParameterType().equals(String.class)){
            return JSONUtil.toJsonStr(ResultVO.ok(data));
        }
        // 否则直接包装成ResultVo返回
        return ResultVO.ok(data);
    }
}