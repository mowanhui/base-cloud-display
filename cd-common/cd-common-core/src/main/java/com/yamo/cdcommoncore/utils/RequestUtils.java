package com.yamo.cdcommoncore.utils;

import com.yamo.cdcommoncore.constants.CommonConstants;
import com.yamo.cdcommoncore.domain.pojo.LoginUser;
import com.yamo.cdcommoncore.exception.BizException;
import com.yamo.cdcommoncore.result.ResultCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求工具
 * @author mowanhui
 * @version 1.0
 * @date 2023/8/14 15:05
 */
public class RequestUtils {
    /**
     * 获取零信任appToken
     * @return
     */
    public static String getAppTokenOfZeroTrust(){
        String token=getRequest().getHeader("RZZX-APPTOKEN");
        if(StringUtils.isBlank(token)){
            throw new BizException("零信任appToken不存在");
        }
        return token;
    }

    public static String getUserTokenOfZeroTrust(){
        String token=getRequest().getHeader("RZZX-USERTOKEN");
        return token;
    }

    /**
     * 获取登录token
     * @return
     */
    public static String getToken(){
        String token=getRequest().getHeader(CommonConstants.LOGIN_TOKEN);
        if(StringUtils.isBlank(token)){
            throw new BizException("token不存在");
        }
        return token;
    }
    /**
     * 获取登录用户ID
     * @return
     */
    public static String getUserId() {
        String userId=getRequest().getHeader(CommonConstants.LOGIN_USER_ID);
        if(StringUtils.isBlank(userId)){
            throw new BizException("用户信息不存在v1");
        }
        return userId;
    }

    /**
     * 获取登录用户信息
     * 一般情况下，直接调用该方法获取用户信息
     * @return
     */
    public static LoginUser getLoginUser() {
        HttpServletRequest request=getRequest();
        Object o=request.getAttribute(CommonConstants.LOGIN_USER);
        if(o==null){
            throw new BizException("用户信息不存在v2");
        }
        LoginUser login=(LoginUser) o;
        return login;
    }

    /**
     * 获取登录用户信息
     * 不会主动抛出异常，直接报错
     * @return
     */
    public static LoginUser getLoginUserV2() {
        HttpServletRequest request=getRequest();
        Object o=request.getAttribute(CommonConstants.LOGIN_USER);
        LoginUser login=(LoginUser) o;
        return login;
    }

    /**
     * 存储登录用户信息
     * @param loginUser
     */
    public static void setLoginUser(LoginUser loginUser) {
        HttpServletRequest request=getRequest();
        request.setAttribute(CommonConstants.LOGIN_USER,loginUser);
    }

    public static HttpServletRequest getRequest(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null;
        if (requestAttributes == null) {
            throw new BizException(ResultCode.BUSY);
        }
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        request = (HttpServletRequest) requestAttributes
                .resolveReference(RequestAttributes.REFERENCE_REQUEST);
        if(request==null){
            throw new BizException(ResultCode.BUSY);
        }
        return request;
    }
}
