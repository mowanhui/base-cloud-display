package com.yamo.cdgateway.filter;

import cn.hutool.core.text.AntPathMatcher;
import com.yamo.cdgateway.config.IgnoreUrlsConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

/**
 * 白名单路径访问需要移除token请求头
 */
@RequiredArgsConstructor
@Component
public class IgnoreUrlsRemoveTokenFilter implements WebFilter {
    private final IgnoreUrlsConfig ignoreUrlsConfig;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request=exchange.getRequest();
        URI uri=request.getURI();
        AntPathMatcher pathMatcher=new AntPathMatcher();
        //白名单列表
        List<String> ignoreUrls=ignoreUrlsConfig.getUrls();
        for (String ignoreUrl : ignoreUrls) {
            if(pathMatcher.match(ignoreUrl,uri.getPath())){
                //对放行的请求路径，去掉Authorization的值
                Object authorization=exchange.getRequest().getHeaders().get("Authorization");
                if(authorization!=null){
                    String var1=authorization.toString();
                    boolean var2=var1.contains("Basic");
                    if(!var2){
                        request=exchange.getRequest().mutate().header("Authorization","").build();
                        exchange=exchange.mutate().request(request).build();
                    }
                }
            }
        }
        return chain.filter(exchange);
    }
}
