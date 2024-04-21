package com.yamo.cdcommoncore.config;

import feign.Client;
import feign.Feign;
import feign.Logger;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 基于https服务调用的配置
 */
@Configuration
public class FeignHttpsConfig {
    @Bean
    public Feign.Builder feignBuilder(Client client){
        return Feign.builder().client(client);
    }
    @Bean
    public Client client(){
        return new Client.Default(TrustingSSLSocketFactory.get(), new NoopHostnameVerifier());
    }
    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }
}
