package com.yamo.cdcommonlog.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpUtil;
import com.yamo.cdcommoncore.constants.CommonConstants;
import com.yamo.cdcommoncore.utils.ServletUtils;
import com.yamo.cdcommonlog.pojo.LogInfo;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author mowanhui
 * @version 1.0
 * @date 2023/5/27 19:53
 */
public class LogUtils {
    public static LogInfo getSysLog(HttpServletRequest request) {
        LogInfo logInfo = new LogInfo();
        if(request==null){
            return logInfo;
        }
        logInfo.setRemoteAddr(ServletUtils.getClientIP(request));
        logInfo.setRequestUri(URLUtil.getPath(request.getRequestURI()));
        logInfo.setMethod(request.getMethod());
        logInfo.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
        logInfo.setParams(HttpUtil.toParams(request.getParameterMap()));
        String startTimeStr = request.getHeader(CommonConstants.REQUEST_START_TIME);
        if (StrUtil.isNotBlank(startTimeStr)) {
            Long startTime = Long.parseLong(startTimeStr);
            Long endTime = System.currentTimeMillis();
            logInfo.setTime(endTime - startTime);
        }
        return logInfo;
    }

    public static LogInfo getSysLogV2(ServerHttpRequest request) {
        LogInfo logInfo = new LogInfo();
        if(request==null){
            return logInfo;
        }
        if(request.getRemoteAddress()!=null){
            logInfo.setRemoteAddr(request.getRemoteAddress().getHostName());
        }
        logInfo.setRequestUri(request.getPath().value());
        logInfo.setMethod(request.getMethod().name());
        logInfo.setUserAgent(request.getHeaders().getFirst(HttpHeaders.USER_AGENT));
        logInfo.setParams(HttpUtil.toParams(request.getQueryParams()));
        return logInfo;
    }
}
