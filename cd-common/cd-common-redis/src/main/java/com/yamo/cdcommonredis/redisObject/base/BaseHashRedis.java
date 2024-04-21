package com.yamo.cdcommonredis.redisObject.base;

import com.yamo.cdcommonredis.util.RedisUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * group代表的是redis的指定键
 * key代表item
 * 哈希存储的抽象类
 * @param <T>
 */
public abstract class BaseHashRedis<T> {
    /**
     * 设置值
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key, T value) {
        return getCacheHelper().hset(getKey() ,key, value, getExpireTime());
    }

    /**
     * 根据key获取值
     * @param key
     * @return
     */
    public T get(String key) {
        return getCacheHelper().hget(getKey() , key, getObjectClass());
    }

    /**
     * 删除键，可以多个键值删除
     * @param keys
     */
    public void del(List<String> keys) {
        getCacheHelper().hdel(getKey(),keys);
    }

    /**
     * 获取所有key
     * @return
     */
    public Set<String> getKeys(){
        return getCacheHelper().hgetKeys(getKey());
    }

    /**
     * 删除组
     */
    public void delGroup() {
        getCacheHelper().del(getKey());
    }

    /**
     * 是否存在组键
     * @return
     */
    public boolean hasGroupKey(){
        return getCacheHelper().hasKey(getKey());
    }

    /**
     * 是否存在键
     * @param key
     * @return
     */
    public boolean hasKey(String key) {
        return getCacheHelper().hHasKey(getKey(), key);
    }

    /**
     * 指定对象类的抽象方法
     * @return
     */
    public abstract Class<T> getObjectClass();

    /**
     * 指定键的抽象方法
     * @return
     */
    public abstract String getKey();

    /**
     * redis的通用工具类
     * @return
     */
    public abstract RedisUtils getCacheHelper();

    /**
     * 获取过期时间
     * @return
     */
    public abstract long getExpireTime();

    /**
     * 获取所有值
     * @return
     */
    public List<T> hgetValus(){
        return getCacheHelper().hgetValus(getKey(),getObjectClass());
    }

    /**
     * 获取所有键值对
     * @return
     */
    public Map<String,T> hmget(){return getCacheHelper().hmget(getKey(),getObjectClass());}

    /**
     * 批量设置键值对
     * @param map
     * @return
     */
    public boolean hmset(Map<String, T> map){
        return getCacheHelper().hmset(getKey(),map);
    }

    /**
     * 刷新过期时间
     * @return
     */
    public boolean refreshExpire() {
        return getCacheHelper().expire(getKey(), getExpireTime());
    }

}
