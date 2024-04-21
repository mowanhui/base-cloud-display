package com.yamo.cdgateway.config;

import cn.hutool.core.util.ArrayUtil;
import com.yamo.cdgateway.handler.RestfulAccessDeniedHandler;
import com.yamo.cdgateway.handler.RestfulAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * 认证授权资源服务器配置
 */
@RequiredArgsConstructor
@Configuration
@EnableWebFluxSecurity
public class ResourceServerConfig {
    private final RestfulAccessDeniedHandler deniedHandler;
    private final RestfulAuthenticationEntryPoint authenticationEntryPoint;
    private final IgnoreUrlsConfig ignoreUrlsConfig;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity httpSecurity) {
        //httpSecurity.addFilterBefore(ignoreUrlsRemoveTokenFilter, SecurityWebFiltersOrder.AUTHORIZATION);
        httpSecurity
                //配置那些路径需要认证
                .authorizeExchange()
                //配置放行路径
                .pathMatchers(ArrayUtil.toArray(ignoreUrlsConfig.getUrls(), String.class)).permitAll()
                .anyExchange()
                //开启认证
                .authenticated()
                .and()
                //资源服务器禁掉登录页
                .formLogin().disable()
                //启动resource-server,并且使用自省token
                .oauth2ResourceServer()
                .opaqueToken()
                .and()
                .accessDeniedHandler(deniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint)
                .and().csrf().disable();
        return httpSecurity.build();
    }
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web)->web.ignoring().antMatchers(ArrayUtil.toArray(ignoreUrlsConfig.getUrls(), String.class));
//    }

}
