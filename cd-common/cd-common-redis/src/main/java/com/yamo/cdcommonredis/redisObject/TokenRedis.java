package com.yamo.cdcommonredis.redisObject;

import com.yamo.cdcommoncore.constants.CommonConstants;
import com.yamo.cdcommonredis.util.RedisUtils;
import com.yamo.cdcommonredis.redisObject.base.BaseStringV2Redis;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TokenRedis extends BaseStringV2Redis<String> {
    /**
     * 组键，真正的键=GROUP_KEY+输入的key
     */
    private final static String GROUP_KEY= CommonConstants.LOGIN_TOKEN;
    /**
     * 默认过期时间两个小时
     */
    private final static long DEFAULT_EXPIRE_TIME=24*60*60;
    /**
     * redis操作工具类
     */
    private final RedisUtils cacheHelper;

    @Override
    public Class<String> getObjectClass() {
        return String.class;
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
