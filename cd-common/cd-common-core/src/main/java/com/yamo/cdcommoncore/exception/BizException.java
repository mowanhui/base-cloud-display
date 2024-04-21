package com.yamo.cdcommoncore.exception;


import com.yamo.cdcommoncore.result.ResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author mowanhui
 * @Date 2021/7/7 11:53
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BizException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = -1551815663445213938L;
    private long code;
    private String msg;
    private Object data;
    public BizException(){
        super(ResultCode.FAILED.getMsg());
        this.code = ResultCode.FAILED.getCode();
        this.msg = ResultCode.FAILED.getMsg();
    }
    public BizException(ResultCode resultCode){
        super(resultCode.getMsg());
        this.code=resultCode.getCode();
        this.msg=resultCode.getMsg();
    }
    public BizException(String msg) {
        super(msg);
        this.code = ResultCode.FAILED.getCode();
        this.msg = msg;
    }
    public BizException(String msg, Object data) {
        this(msg);
        this.data=data;
    }
    public BizException(long code, String msg) {
        this(msg);
        this.code = code;
        this.msg = msg;
    }
}
