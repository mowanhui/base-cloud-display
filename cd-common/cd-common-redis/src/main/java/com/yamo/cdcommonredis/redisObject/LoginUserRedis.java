package com.yamo.cdcommonredis.redisObject;

import com.yamo.cdcommoncore.constants.CommonConstants;
import com.yamo.cdcommoncore.domain.pojo.LoginUser;
import com.yamo.cdcommonredis.util.RedisUtils;
import com.yamo.cdcommonredis.redisObject.base.BaseStringV2Redis;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author mowanhui
 * @version 1.0
 * @date 2023/8/15 15:30
 */
@RequiredArgsConstructor
@Component
public class LoginUserRedis extends BaseStringV2Redis<LoginUser> {
    private final static String GROUP_KEY= CommonConstants.LOGIN_USER;
    /**
     * 默认过期时间两个小时
     */
    private final static long DEFAULT_EXPIRE_TIME=24*60*60;
    private final RedisUtils cacheHelper;

    @Override
    public Class<LoginUser> getObjectClass() {
        return LoginUser.class;
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
