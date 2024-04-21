package com.yamo.cdcommonredis.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 描述:基于RedisTemplate 封装的Redis泛型工具类
 * 基于spring和redis的redisTemplate工具类
 * 针对所有的hash 都是以h开头的方法
 * 针对所有的Set 都是以s开头的方法
 * 针对所有的List 都是以l开头的方法
 * @Author mowanhui
 * @Date 2022/12/12 15:07
 * @Version 1.0
 */
@Component
@AllArgsConstructor
public class RedisUtils {

    private RedisTemplate<String, String> redisTemplate;

    //=============================common============================

    /**
     * 序列化
     * @param serializer
     */
    public void setValueSerializer(RedisSerializer<?> serializer){
        redisTemplate.setValueSerializer(serializer);
    }
    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间do
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        }
    }

    public void del(List<String> keys) {
        if(keys!=null&&keys.size()>0){
            redisTemplate.delete(keys);
        }
    }

    //============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public <T> T get(String key, Class<T> cls) {
        try {
            if(StrUtil.isBlank(key)){
                return null;
            }

            if(cls== String.class){
                return (T)redisTemplate.opsForValue().get(key);
            }
            if(cls==JSONArray.class){
                return (T)JSONUtil.parseArray(redisTemplate.opsForValue().get(key));
            }
            return JSONUtil.toBean(redisTemplate.opsForValue().get(key), cls);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public String get(String key) {
        try {
            return key == null ? null : JSONUtil.toBean(redisTemplate.opsForValue().get(key), String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public <T> boolean set(String key, T value) {
        try {
            String valueJson = JSONUtil.toJsonStr(value);
            redisTemplate.opsForValue().set(key, valueJson);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public <T> boolean set(String key, T value, long time) {
        try {
            if (time > 0) {
                String valueJson = JSONUtil.toJsonStr(value);
                redisTemplate.opsForValue().set(key, valueJson, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    //================================Map=================================

    public Set<String> hgetKeys(String key){
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        Set<String> keys=operations.keys(key);
        return keys;
    }

    public <T> List<T> hgetValus(String key, Class<T> cls){
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        List<String> values=operations.values(key);
        if(values.isEmpty()){
            return null;
        }
        List<T> resList=new ArrayList<>();
        for(String item:values){
            if(cls==String.class){
                resList.add((T)item);
            }
            else if(cls== JSONObject.class){
                resList.add((T)JSONUtil.parseObj(item));
            }
            else if(cls== JSONArray.class){
                resList.add((T)JSONUtil.parseArray(item));
            }
            else {
                resList.add(JSONUtil.toBean(item, cls));
            }
        }
        return resList;
    }

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public <T> T hget(String key, String item, Class<T> cls) {
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        if(cls==String.class){
            return (T)operations.get(key, item);
        }
        if(cls== JSONObject.class){
            return (T) JSONUtil.parseObj(operations.get(key, item));
        }
        if(cls== JSONArray.class){
            return (T) JSONUtil.parseArray(operations.get(key, item));
        }
        return JSONUtil.toBean(operations.get(key, item), cls);
    }


    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public <T> Map<String, T> hmget(String key, Class<T> cls) {
        Map<String, T> valueMap = new HashMap<>();
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        Map<String, String> stringMap = operations.entries(key);
        for (Map.Entry<String, String> entry : stringMap.entrySet()) {
            T value =null;
            if(cls== JSONObject.class){
                value= (T) JSONUtil.parseObj(entry.getValue());
            }else {
                value = JSONUtil.toBean(entry.getValue(), cls);
            }
            valueMap.put(entry.getKey(), value);
        }
        return valueMap;
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public <T> boolean hmset(String key, Map<String, T> map) {
        try {
            HashOperations<String, String, String> operations = redisTemplate.opsForHash();
            Map<String, String> jsonMap = new HashMap<>();
            for (Map.Entry<String, T> entry : map.entrySet()) {
                String value = JSONUtil.toJsonStr(entry.getValue());
                jsonMap.put(entry.getKey(), value);
            }
            operations.putAll(key, jsonMap);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public <T> boolean hmset(String key, Map<String, T> map, long time) {
        try {
            hmset(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public <T> boolean hset(String key, String item, T value) {
        try {
            HashOperations<String, String, String> operations = redisTemplate.opsForHash();
            String valueJson = JSONUtil.toJsonStr(value);
            operations.put(key, item, valueJson);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public <T> boolean hset(String key, String item, T value, long time) {
        try {
            hset(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public <T> void hdel(String key, String... item) {
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        Object[] values = new Object[item.length];
        for (int i = 0; i < item.length; i++) {
            values[i] = item[i];
        }
        operations.delete(key, values);
    }

    public <T> void hdel(String key, List<String> items) {
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        Object[] values = new Object[items.size()];
        for (int i = 0; i < items.size(); i++) {
            values[i] = items.get(i);
        }
        operations.delete(key, values);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    //============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public <T> Set<T> sGet(String key, Class<T> cls) {
        try {
            SetOperations<String, String> setOperations = redisTemplate.opsForSet();
            Set<T> set = new HashSet<>();
            Set<String> setStr = setOperations.members(key);
            Iterator<String> iterator = setStr.iterator();
            while (iterator.hasNext()) {
                set.add(JSONUtil.toBean(iterator.next(), cls));
            }
            return set;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public <T> boolean sHasKey(String key, T value) {
        try {
            SetOperations<String, String> setOperations = redisTemplate.opsForSet();
            String valueJson = JSONUtil.toJsonStr(value);
            return setOperations.isMember(key, valueJson);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public <T> long sSet(String key, T... values) {
        try {
            SetOperations<String, String> setOperations = redisTemplate.opsForSet();
            String[] valueJsons = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                valueJsons[i] = JSONUtil.toJsonStr(values[i]);
            }
            return setOperations.add(key, valueJsons);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public <T> long sSetAndTime(String key, long time, T... values) {
        try {
            Long count = sSet(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key) {
        try {
            SetOperations<String, String> setOperations = redisTemplate.opsForSet();
            return setOperations.size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public <T> long setRemove(String key, T... values) {
        try {
            SetOperations<String, String> setOperations = redisTemplate.opsForSet();
            String[] valueJsons = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                valueJsons[i] = JSONUtil.toJsonStr(values[i]);
            }
            Long count = setOperations.remove(key, valueJsons);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    //===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return
     */
    public <T> List<T> lGet(String key, long start, long end, Class<T> cls) {
        try {
            ListOperations<String, String> listOperations = redisTemplate.opsForList();
            List<String> stringList = listOperations.range(key, start, end);
            List<T> list = new ArrayList<>();
            for (String str : stringList) {
                T t = JSONUtil.toBean(str, cls);
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public long lGetListSize(String key) {
        try {
            ListOperations<String, String> listOperations = redisTemplate.opsForList();
            return listOperations.size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public <T> T lGetIndex(String key, long index, Class<T> cls) {
        try {
            ListOperations<String, String> listOperations = redisTemplate.opsForList();
            String value = listOperations.index(key, index);
            return JSONUtil.toBean(value, cls);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public <T> boolean lSet(String key, T value) {
        try {
            ListOperations<String, String> listOperations = redisTemplate.opsForList();
            String valueJson = JSONUtil.toJsonStr(value);
            listOperations.rightPush(key, valueJson);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public <T> boolean lSet(String key, T value, long time) {
        try {
            lSet(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public <T> boolean lSet(String key, List<T> value) {
        try {
            ListOperations<String, String> listOperations = redisTemplate.opsForList();
            List<String> valueStrList = new ArrayList<>();
            for (T t : value) {
                String valueStr = JSONUtil.toJsonStr(t);
                valueStrList.add(valueStr);
            }
            listOperations.rightPushAll(key, valueStrList);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            lSet(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public <T> boolean lUpdateIndex(String key, long index, T value) {
        try {
            ListOperations<String, String> listOperations = redisTemplate.opsForList();
            String valueJson = JSONUtil.toJsonStr(value);
            listOperations.set(key, index, valueJson);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public <T> long lRemove(String key, long count, T value) {
        try {
            ListOperations<String, String> listOperations = redisTemplate.opsForList();
            String valueJson = JSONUtil.toJsonStr(value);
            Long remove = listOperations.remove(key, count, valueJson);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
