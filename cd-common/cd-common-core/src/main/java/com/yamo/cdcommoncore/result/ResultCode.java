package com.yamo.cdcommoncore.result;

/**
 * @author mowanhui
 * @version 1.0
 * @date 2023/5/22 21:55
 */
public enum ResultCode implements IErrorCode{
    /**
     * 系统错误
     */
    SUCCESS(200,"操作成功"),
    FAILED(500,"操作失败"),
    BUSY(10003,"系统繁忙"),
    PARAM_VALIDATE_FAILED(10004,"参数校验错误"),

    /**
     * token错误
     */
    TOKEN_REQUIRED(30001,"token_required"),
    TOKEN_INVALID(30002,"token_invalid"),
    TOKEN_EXPIRED(30003,"token_expired"),

    /**
     * 授权错误
     */
    OAUTH_USER_UNAUTHORIZED(40001,"user unauthorized"),
    OAUTH_INVALID_CODE(40002,"invalid code"),
    OAUTH_UNAUTHORIZED(40003,"unauthorized"),
    OAUTH_CLIENT_AUTHENTICATION_FAILED(40004,"client authentication failed"),
    OAUTH_GRANT_TYPE_ERROR(40005,"grant type error"),
    OAUTH_LOGIN(40006,"请重新登录"),
    OAUTH_LOGOUT(40007,"登出成功"),
    /**
     * 业务代码错误
     */

    ENUM_TYPE_NOT_EXIST(50001,"枚举类型不存在"),
    JWT_PARSE_FAILED(50002,"jwt解析错误"),
    RECORD_NOT_EXIST(50003,"记录不存在"),
    CLIENT_ID_EXIST(50004,"客户端Id已存在"),
    USER_NAME_EXIST(50005,"用户名已存在"),
    PHONE_EXIST(50006,"手机号已存在"),
    ID_CARD_EXIST(50007,"身份证已存在"),
    USER_NOT_EXIST(50008,"用户不存在"),

    //图层配置相关
    ERROR_PARAMETER(50009,"参数错误"),
    DATA_NOT_FOUND(50010,"数据不存在"),
    FACE_RETURN_ERROR(50012,"人脸接口返回失败"),
    KAKOU_TASKID_ERROR(50013,"卡口taskId获取失败"),
    KAKOU_RETURN_ERROR(50014,"卡口接口返回失败"),
    KAKOU_NOT_FOUND_ERROR(50015,"卡口对应关系为空");
    ;

    private final Integer code;
    private final String msg;

    ResultCode(int code, String msg) {
        this.code=code;
        this.msg=msg;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}
