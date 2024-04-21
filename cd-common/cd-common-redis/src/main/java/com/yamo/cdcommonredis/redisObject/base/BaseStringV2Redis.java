package com.yamo.cdcommonredis.redisObject.base;

import com.yamo.cdcommonredis.util.RedisUtils;

/**
 * string数据类型存储类
 * 该类是可以可变的key，但它会在getKey()后面加上自定义的值
 * @param <T>
 */
public abstract class BaseStringV2Redis<T> {

    public boolean set(String key,T value) {
        return getCacheHelper().set(getFinalKey(key), value, getExpireTime());
    }

    public T get(String key) {
        return getCacheHelper().get(getFinalKey(key) , getObjectClass());
    }

    public void del(String key) {
        getCacheHelper().del(getFinalKey(key));
    }

    public boolean hasKey(String key) {
        return getCacheHelper().hasKey(getFinalKey(key));
    }

    public abstract Class<T> getObjectClass();

    public abstract String getKey();

    public abstract RedisUtils getCacheHelper();

    public abstract long getExpireTime();

    private String getFinalKey(String key){
        return getKey()+"_"+key;
    }

    public boolean refreshExpire(String key) {
        return getCacheHelper().expire(getFinalKey(key), getExpireTime());
    }
}
