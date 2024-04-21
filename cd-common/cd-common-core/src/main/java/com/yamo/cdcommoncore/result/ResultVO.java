package com.yamo.cdcommoncore.result;

import lombok.Data;

/**
 * @author mowanhui
 * @version 1.0
 * @date 2023/5/22 21:49
 */

@Data
public class ResultVO<T>{
    private long code;
    private String msg;
    private boolean success;
    private T data;

    public ResultVO() {
    }

    /**
     * 该方法已弃用
     * @param data
     */
    @Deprecated
    public ResultVO(T data) {
        this.code = ResultCode.SUCCESS.getCode();
        this.success = true;
        this.data = data;
    }

    private ResultVO(long code, String msg, boolean success, T data) {
        this.code = code;
        this.msg = msg;
        this.success = success;
        this.data = data;
    }
    public static <T> ResultVO<T> ok(){
        return ok(null);
    }
    public static <T> ResultVO<T> ok(T data) {
        return new ResultVO<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg(), true, data);
    }

    public static <T> ResultVO<T> failed() {
        return failed(ResultCode.FAILED);
    }

    public static <T> ResultVO<T> failed(ResultCode resultCode) {
        return failed(resultCode.getCode(), resultCode.getMsg());
    }

    public static <T> ResultVO<T> failed(long code, String msg) {
        return new ResultVO<T>(code, msg, false, null);
    }

    public static <T> ResultVO<T> failed(T data) {
        return failed(ResultCode.FAILED, data);
    }

    public static <T> ResultVO<T> failed(ResultCode resultCode, T data) {
        return failed(resultCode.getCode(), data!=null?data.toString():"", data);
    }

    public static <T> ResultVO<T> failed(long code, String msg, T data) {
        return new ResultVO<T>(code, msg, false, data);
    }


}
