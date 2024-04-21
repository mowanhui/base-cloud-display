package com.yamo.cdcommonsupport.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yamo.cdcommonapi.feign.auth.AuthFeignClient;
import com.yamo.cdcommoncore.domain.pojo.LoginUser;
import com.yamo.cdcommoncore.exception.BizException;
import com.yamo.cdcommoncore.utils.RequestUtils;
import com.yamo.cdcommonredis.redisObject.LoginUserRedis;
import com.yamo.cdcommonredis.redisObject.TokenRedis;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 认证切面
 * 对接口加以认证，如果token或userId不正确的话，则不可调用接口
 * 在改切面通过认证的接口，会将对应的用户信息加入到请求属性域中，在service层可以通过RequestUtils.getLoginUser()获取用户信息
 *
 * @author mowanhui
 * @version 1.0
 * @date 2023/8/15 16:57
 */
@RequiredArgsConstructor
@Aspect
@Component
@Order(1)
public class AuthAspect {
    private final LoginUserRedis loginUserRedis;
    private final TokenRedis tokenRedis;
    private final AuthFeignClient authFeignClient;

    @Pointcut("@annotation(com.yamo.cdcommonsupport.annotation.Auth) " +
            "|| execution(* *(@com.yamo.cdcommonsupport.annotation.Auth (*),..))")
    public void login() {
    }

    @Around(value = "login()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        LoginUser login = null;
        String token = RequestUtils.getToken();
        String userId = RequestUtils.getUserId();
        if (tokenRedis.hasKey(token)) {
            login = loginUserRedis.get(userId);
        } else {
            //获取用户信息
            login = getUserInfo(userId);
            loginUserRedis.set(userId, login);
            tokenRedis.set(token,userId);
        }
        if (login == null) {
            throw new BizException("用户信息不存在");
        }
        loginUserRedis.refreshExpire(userId);
        tokenRedis.refreshExpire(token);
        //将用户信息存入请求属性中
        RequestUtils.setLoginUser(login);
        return joinPoint.proceed();
    }

    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    private LoginUser getUserInfo(String userId) {
        String data = authFeignClient.getUserInfo(userId);
        if (StrUtil.isBlank(data)) {
            throw new BizException("用户不存在");
        }
        JSONObject userInfo = JSONUtil.parseObj(data);
        if (!userInfo.containsKey("account")) {
            throw new BizException("用户信息不存在");
        }
        JSONObject account = userInfo.getJSONObject("account");
        if (account == null) {
            throw new BizException("用户信息不存在");
        }
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(account.getStr("id"));
        loginUser.setPoliceId(account.getStr("policeId"));
        loginUser.setUserName(account.getStr("accountName"));
        loginUser.setRealName(account.getStr("realName"));
        loginUser.setIdCard(account.getStr("idCard"));
        loginUser.setPhone(account.getStr("phone"));
        if (account.containsKey("otherField")) {
            JSONObject otherField = account.getJSONObject("otherField");
            if (otherField != null) {
                String groupId = otherField.getStr("groupId");
                if (StrUtil.isNotBlank(groupId)) {
                    //获取用户组信息
                    String groupData = authFeignClient.getGroupInfo(groupId);
                    if (StrUtil.isNotBlank(groupData)) {
                        JSONObject groupInfo = JSONUtil.parseObj(groupData);
                        loginUser.setUnitCode(groupInfo.getStr("groupCode"));
                        loginUser.setAllUnitCode(groupInfo.getStr("groupAllCode"));
                        loginUser.setUnitName(groupInfo.getStr("groupName"));
                    }
                }
            }
        }
        return loginUser;
    }
}
