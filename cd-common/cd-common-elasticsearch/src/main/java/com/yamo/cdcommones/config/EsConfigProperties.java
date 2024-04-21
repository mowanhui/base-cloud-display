package com.yamo.cdcommones.config;

import lombok.Data;
import org.apache.http.HttpHost;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: Es连接属性 配置类
 * @Date: 2020/01/01 17:00
 * @version 1.0
 */
@Component
@Data
@ConfigurationProperties(prefix = "elasticsearch")
public class EsConfigProperties {
    /** 服务ip地址 */
    private String hosts;
    /** 服务的端口号 */
    private int port;
    /** 服务的请求协议 */
    private String schema;
    /** 连接超时时间 */
    private int connectTimeOut;
    /** socket连接超时时间 */
    private int socketTimeOut;
    /** 请求超时超时时间 */
    private int connectionRequestTimeOut;
    /** 最大连接数 */
    private int  maxConnectNum;
    /** 服务每次能并行接收的最大请求数量 */
    private int maxConnectPerRoute;

    /**
     * 获取集群地址
     * @return
     */
    public HttpHost[] getHpptHosts(){
        Assert.hasLength(hosts,"Es地址为空");
        List<HttpHost> hostList = new ArrayList<HttpHost>();
        String[] hostArray = hosts.split(",");
        for (String host : hostArray) {
            hostList.add(new HttpHost(host, port, schema));
        }

        HttpHost[] httphostArray = new HttpHost[hostList.size()];
        hostList.toArray(httphostArray);

        return httphostArray;
    }
}
