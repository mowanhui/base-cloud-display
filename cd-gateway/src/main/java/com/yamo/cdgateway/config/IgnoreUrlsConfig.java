package com.yamo.cdgateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 放行请求配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "ignore")
public class IgnoreUrlsConfig {
    private List<String> urls;
    public List<String> getUrls(){
        if(urls==null){
            return new ArrayList<>();
        }
        return urls;
    }
}
