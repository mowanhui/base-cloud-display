package com.yamo.cdcommoncore.config;

import cn.hutool.core.util.StrUtil;
import com.yamo.cdcommoncore.constants.CommonConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * 令牌中继
 *
 * @author mowanhui
 * @version 1.0
 * @date 2023/5/31 16:38
 */
@Component
public class FeignConfig implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(requestAttributes)) {
            String userId = requestAttributes.getRequest().getHeader(CommonConstants.LOGIN_USER_ID);
            String token =requestAttributes.getRequest().getHeader(CommonConstants.LOGIN_TOKEN);
            if (StrUtil.isNotBlank(userId)) {
                // 清除token头 避免传染
                template.header(CommonConstants.LOGIN_USER_ID);
                template.header(CommonConstants.LOGIN_USER_ID, userId);
                template.header(CommonConstants.LOGIN_TOKEN);
                template.header(CommonConstants.LOGIN_TOKEN, token);
            }
        }
    }
}
