package com.yamo.cdcommonredis.redisObject;

import com.yamo.cdcommoncore.constants.CommonConstants;
import com.yamo.cdcommonredis.redisObject.base.BaseStringRedis;
import com.yamo.cdcommonredis.util.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author mowanhui
 * @version 1.0
 * @date 2024/3/25 15:33
 */
@Component
@RequiredArgsConstructor
public class DemoStringRedis extends BaseStringRedis<String> {
    /**
     * 键
     */
    private final static String KEY= CommonConstants.DEMO_STRING;
    /**
     * 默认过期时间24个小时
     */
    private final static long DEFAULT_EXPIRE_TIME=24*60*60;
    private final RedisUtils cacheHelper;

    @Override
    public Class<String> getObjectClass() {
        return String.class;
    }

    @Override
    public String getKey() {
        return KEY;
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
