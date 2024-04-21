package com.yamo.cdgateway.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yamo.cdcommoncore.exception.BizException;
import com.yamo.cdcommoncore.result.ResultVO;
import com.yamo.cdcommoncore.result.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;
import reactor.core.publisher.Mono;

/**
 * 网关异常通用处理器，只作用在webflux 环境下 , 优先级低于 {@link ResponseStatusExceptionHandler} 执行
 * @author mowanhui
 * @version 1.0
 * @date 2023/5/25 14:29
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
    private final ObjectMapper objectMapper;
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        if (response.isCommitted()) {
            return Mono.error(ex);
        }
        // header set
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        if (ex instanceof ResponseStatusException) {
            response.setStatusCode(((ResponseStatusException) ex).getStatus());
        }
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            try {
                ResultVO<Object> result= ResultVO.failed(ResultCode.BUSY,ex.getMessage());
                if (ex instanceof org.springframework.cloud.gateway.support.NotFoundException) {
                    result= ResultVO.failed(ResultCode.FAILED,ex.getMessage());
                }else if(ex instanceof BizException){
                    BizException e=(BizException)ex;
                    result= ResultVO.failed(e.getCode(),ex.getMessage());
                }
                log.warn("Error Spring Cloud Gateway : {} {}", exchange.getRequest().getPath(), ex.getMessage());
                ex.printStackTrace();
                return bufferFactory.wrap(objectMapper.writeValueAsBytes(result));
            }
            catch (JsonProcessingException e) {
                log.error("Error writing response", ex);
                return bufferFactory.wrap(new byte[0]);
            }
        }));
    }

}

