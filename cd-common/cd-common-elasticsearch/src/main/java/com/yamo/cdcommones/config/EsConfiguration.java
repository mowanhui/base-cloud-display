package com.yamo.cdcommones.config;


import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.*;
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;
import org.elasticsearch.client.RestClientBuilder.RequestConfigCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: Es生成连接对象 配置类
 * @Date: 2020/01/01 17:00
 * @version 1.0
 */
@Configuration
public class EsConfiguration {

    protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private EsConfigProperties properties;

    /**
     * 生成es client
     * @return
     */
    @Bean
    public RestHighLevelClient esHighLevelClient() {
        RestHighLevelClient client = null;
        try{
            RestClientBuilder builder = RestClient.builder(properties.getHpptHosts());
            // 异步httpclient连接延时配置
            builder.setRequestConfigCallback(new RequestConfigCallback() {
                /**
                 * 允许自定义将用于每个请求的{@link RequestConfig}。
                 * 通常可以通过此方法定制不同的超时值，而不会丢失任何其他有用的默认值
                 * 值，该值由{@link RestClientBuilder}内部设置。
                 */
                @Override
                public Builder customizeRequestConfig(Builder requestConfigBuilder) {
                    requestConfigBuilder.setConnectTimeout(properties.getConnectTimeOut());
                    requestConfigBuilder.setSocketTimeout(properties.getSocketTimeOut());
                    requestConfigBuilder.setConnectionRequestTimeout(properties.getConnectionRequestTimeOut());
                    return requestConfigBuilder;
                }
            });
            // 异步httpclient连接数配置
            builder.setHttpClientConfigCallback(new HttpClientConfigCallback() {
                @Override
                public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                    // 分配最大的连接总值。
                    httpClientBuilder.setMaxConnTotal(properties.getMaxConnectNum());
                    // 分配每个路由的最大连接值
                    httpClientBuilder.setMaxConnPerRoute(properties.getMaxConnectPerRoute());
                    return httpClientBuilder;
                }
            });
            // 连接失败监听配置
            builder.setFailureListener(new RestClient.FailureListener() {
                /**
                 * 每次尝试失败后调用。
                 * 作为参数接收用于失败尝试的主机。
                 */
                @Override
                public void onFailure(Node node) {
                    logger.error("elasticsearch异常失败，连接参数："+ JSONObject.toJSONString(node));
                }
            });
            // 设置{@link NodeSelector}用于所有请求。
            builder.setNodeSelector(NodeSelector.SKIP_DEDICATED_MASTERS);
            client = new RestHighLevelClient(builder);
        }catch (Exception e){
            e.printStackTrace();
        }
        return client;
    }
}
