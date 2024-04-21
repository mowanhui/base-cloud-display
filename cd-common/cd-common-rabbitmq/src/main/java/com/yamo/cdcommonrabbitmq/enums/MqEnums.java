package com.yamo.cdcommonrabbitmq.enums;

/**
 * @author mowanhui
 * @version 1.0
 * @date 2023/5/28 0:28
 */
public enum MqEnums {
    SYS_LOG("sys.log","sys.log","sys.log"),
    ;
    private final String exchange;
    private final String queue;
    private final String routKey;

    public String getExchange() {
        return exchange;
    }

    public String getQueue() {
        return queue;
    }

    public String getRoutKey() {
        return routKey;
    }

    MqEnums(String exchange, String queue, String routKey) {
        this.exchange = exchange;
        this.queue = queue;
        this.routKey = routKey;
    }
}
