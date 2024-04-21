package com.yamo.cdcommonredis.redisObject.base;


import com.yamo.cdcommonredis.util.RedisUtils;

/**
 * string数据类型存储类
 * 该抽象类，不可以自定义的key（不可变，会根据getKey()方法获取）
 * @Author mowanhui
 * @Date 2023/1/9 14:11
 * @Version 1.0
 */
public abstract class BaseStringRedis<T>{
    public boolean set(T value) {
        return getCacheHelper().set(getKey(), value, getExpireTime());
    }

    public T get() {
        return getCacheHelper().get(getKey() , getObjectClass());
    }

    public void del() {
        getCacheHelper().del(getKey());
    }

    public boolean hasKey() {
        return getCacheHelper().hasKey(getKey());
    }

    public abstract Class<T> getObjectClass();

    public abstract String getKey();

    public abstract RedisUtils getCacheHelper();

    public abstract long getExpireTime();

    public boolean refreshExpire() {
        return getCacheHelper().expire(getKey(), getExpireTime());
    }

}
