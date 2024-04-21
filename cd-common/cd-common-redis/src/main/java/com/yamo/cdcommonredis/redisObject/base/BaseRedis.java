package com.yamo.cdcommonredis.redisObject.base;


/**
 * redis基础接口
 * @Author mowanhui
 * @Date 2023/1/9 14:11
 * @Version 1.0
 */
public interface BaseRedis<T> {
    boolean hSet(String key, T value);
    T hGet(String key);
    void hDel(String key);
    boolean hasKey(String key);
    Class<T> getObjectClass();
    String getKey();
}
