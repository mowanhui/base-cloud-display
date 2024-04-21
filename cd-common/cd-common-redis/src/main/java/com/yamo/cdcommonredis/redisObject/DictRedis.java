package com.yamo.cdcommonredis.redisObject;

import cn.hutool.json.JSONArray;
import com.yamo.cdcommoncore.constants.CommonConstants;
import com.yamo.cdcommonredis.util.RedisUtils;
import com.yamo.cdcommonredis.redisObject.base.BaseHashRedis;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DictRedis extends BaseHashRedis<JSONArray> {
    private final RedisUtils cacheHelper;
    private final static String GROUP_KEY= CommonConstants.COMMON_DICT;
    /**
     * 默认过期时间24小时
     */
    private final static long DEFAULT_EXPIRE_TIME=24*60*60;
    @Override
    public Class<JSONArray> getObjectClass() {
        return JSONArray.class;
    }

    @Override
    public String getKey() {
        return GROUP_KEY;
    }

    @Override
    public RedisUtils getCacheHelper() {
        return cacheHelper;
    }

    @Override
    public long getExpireTime() {
        return DEFAULT_EXPIRE_TIME;
    }
}
