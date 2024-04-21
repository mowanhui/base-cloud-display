package com.yamo.cdcommoncore.constants;

/**
 * 通用常量
 * @author mowanhui
 * @version 1.0
 * @date 2023/5/27 22:02
 */
public interface CommonConstants {
    String COMMON_DICT="COMMON_DICT";
    /**
     * 请求开始时间
     */
    String REQUEST_START_TIME="request_start_time";
    /**
     * 用户信息
     */
    String LOGIN_USER="login_user";

    String LOGIN_USER_ID="login_user_id";


    String LOGIN_TOKEN="login_token";


    String DEMO_STRING="demo_string";
    /**
     * 手机号正则表达式
     */
    String PHONE_REGEX="^[1][3,4,5,6,7,8,9][0-9]{9}$";
    /**
     * 身份证18位正则表达式
     */
    String ID_CARD_18_REGEX="^[1-9][0-9]{5}(18|19|20)[0-9]{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)[0-9]{3}([0-9]|(X|x))";
    /**
     * 身份证15位正则表达式
     */
    String ID_CARD_15_REGEX="^[1-9][0-9]{5}[0-9]{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)[0-9]{2}[0-9]";
    /**
     * 身份证正则表达式
     */
    String ID_CARD_REGEX="("+ID_CARD_18_REGEX+")"+"|"+"("+ID_CARD_15_REGEX+")";
}
