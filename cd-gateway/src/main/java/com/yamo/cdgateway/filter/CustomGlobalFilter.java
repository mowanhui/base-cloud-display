package com.yamo.cdgateway.filter;

import cn.hutool.core.util.StrUtil;
import com.yamo.cdcommoncore.constants.CommonConstants;
import com.yamo.cdcommoncore.exception.BizException;
import com.yamo.cdcommonredis.redisObject.LoginUserRedis;
import com.yamo.cdcommonredis.redisObject.TokenRedis;
import com.yamo.cdgateway.config.IgnoreUrlsConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

/**
 * @author mowanhui
 * @version 1.0
 * @date 2023/8/14 15:14
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {
    private final TokenRedis tokenRedis;
    private final IgnoreUrlsConfig ignoreUrlsConfig;
    private final LoginUserRedis loginUserRedis;

    @Override
    public int getOrder() {
        return 0;
    }

//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        return chain.filter(exchange);
//    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        //放行
//        if (isPass(exchange)) {
//            return chain.filter(exchange);
//        }
        Mono<SecurityContext> contextMono= ReactiveSecurityContextHolder.getContext();
        return contextMono.doOnNext(securityContext -> {
            Authentication authentication=securityContext.getAuthentication();
            Object principal=authentication.getPrincipal();
            if(principal instanceof OAuth2IntrospectionAuthenticatedPrincipal){
                String userId = ((OAuth2IntrospectionAuthenticatedPrincipal) principal).getAttributes().get("user_name").toString();
                System.out.println(userId);
                if (StrUtil.isBlank(userId)) {
                    throw new BizException("未授权");
                }
                //获取token
                String token = exchange.getRequest().getHeaders().getFirst("Authorization");
                if(StrUtil.isNotBlank(token)){
                    token=token.replace("Bearer ","");
                    ServerHttpRequest.Builder builder=exchange.getRequest().mutate()
                            .header(CommonConstants.LOGIN_TOKEN, token)
                            .header(CommonConstants.LOGIN_USER_ID,userId);
                    ServerHttpRequest request = builder.build();
                    exchange.mutate().request(request).build();
                }
            }else {
                throw new BizException("认证异常！");
            }
        }).then(chain.filter(exchange));
    }

    /**
     * 放行处理
     *
     * @param exchange
     * @return
     */
    private boolean isPass(ServerWebExchange exchange) {
        List<String> ignoreUrls = ignoreUrlsConfig.getUrls();
        if (ignoreUrls.size() > 0) {
            URI uri = exchange.getRequest().getURI();
            PathMatcher pathMatcher = new AntPathMatcher();
            for (String ignoreUrl : ignoreUrls) {
                if (pathMatcher.match(ignoreUrl, uri.getPath())) {
                    return true;
                }
            }
        }
        return false;
    }

}
