package com.yamo.cdcommoncore.utils;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.ArrayUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author mowanhui
 * @version 1.0
 * @date 2023/5/27 21:41
 */
public class ServletUtils {
    public static String getClientIP(HttpServletRequest request, String... otherHeaderNames) {
        String[] headers = new String[]{"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};
        if (ArrayUtil.isNotEmpty(otherHeaderNames)) {
            headers = (String[])ArrayUtil.addAll(new String[][]{headers, otherHeaderNames});
        }

        return getClientIPByHeader(request, headers);
    }
    public static String getClientIPByHeader(HttpServletRequest request, String... headerNames) {
        String[] var3 = headerNames;
        int var4 = headerNames.length;

        String ip;
        for(int var5 = 0; var5 < var4; ++var5) {
            String header = var3[var5];
            ip = request.getHeader(header);
            if (!NetUtil.isUnknown(ip)) {
                return NetUtil.getMultistageReverseProxyIp(ip);
            }
        }

        ip = request.getRemoteAddr();
        return NetUtil.getMultistageReverseProxyIp(ip);
    }
}
