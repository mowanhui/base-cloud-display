package com.yamo.cdcommonsupport.handler;


import com.yamo.cdcommoncore.exception.BizException;
import com.yamo.cdcommoncore.result.ResultVO;
import com.yamo.cdcommoncore.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

/**
 * 全局异常
 * @author mowanhui
 * @version 1.0
 * @date 2023/5/25 21:48
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * log.error打印e.getMessage()只会打印其类型信息不打印异常堆栈，只能直接打印e
     * */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResultVO<Object> handleParameterInvalidException(HttpServletRequest req, HttpServletResponse res, BindException  e) {
        String message = e.getBindingResult().getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("；"));
        e.printStackTrace();
        log.error("Exception:",e);
        return ResultVO.failed(ResultCode.PARAM_VALIDATE_FAILED,message);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BizException.class)
    public ResultVO<Object> handler(HttpServletRequest request, BizException e) {
        e.printStackTrace();
        log.error("Exception:",e);
        return ResultVO.failed(e.getCode(),e.getMessage(),e.getData());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public ResultVO<Object> handler(HttpServletRequest request, Exception e) {
        e.printStackTrace();
        log.error("Exception:",e);
        return ResultVO.failed(ResultCode.FAILED,e.getMessage());
    }

}
