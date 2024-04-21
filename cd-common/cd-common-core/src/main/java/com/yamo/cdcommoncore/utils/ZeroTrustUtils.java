package com.yamo.cdcommoncore.utils;

import cn.hutool.core.util.StrUtil;
import com.yamo.cdcommoncore.domain.dto.ZeroTrustAppSignDTO;
import com.yamo.cdcommoncore.domain.dto.ZeroTrustUserSignDTO;
import com.yamo.cdcommoncore.exception.BizException;
import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 关于零信任的工具类
 */
@UtilityClass
public class ZeroTrustUtils {
    /**
     * 认证平台线下分配的签名秘钥
     */
    private final static String SIGN_SECRET = "2FnGHUT6sZh8q4WLnsrjc9rklZWguDn7PEFxgQrg";
    /**
     * 接口调用方ID，用于检查接口调用的合法性，由认证中心分配并线下告知应用厂商
     */
    private final static String CALLER_ID = "liantong";

    /**
     * 获取appToken的base64编码
     *
     * @param appToken
     * @return
     */
    public String getTokenOfBase64(String appToken) {
        if (StrUtil.isBlank(appToken)) {
            return null;
        }
        String tokenJson = "{\"tokenId\":\"" + appToken + "\",\"authType\":\"1\",\"taskId\":\"1\"}";
        return Base64.getEncoder().encodeToString(tokenJson.getBytes());
    }

    /**
     * 获取user签名参数值
     *
     * @param userToken
     * @return
     */
    public ZeroTrustUserSignDTO getZeroTrustUserSign(String userToken) {
        if (StrUtil.isBlank(userToken)) {
            throw new BizException("零信任AppToken不存在！");
        }
        ZeroTrustUserSignDTO zeroTrustUserSignDTO = new ZeroTrustUserSignDTO();
        zeroTrustUserSignDTO.setUserToken(userToken);
        zeroTrustUserSignDTO.setCallerId(CALLER_ID);
        zeroTrustUserSignDTO.setCallerTimestamp(System.currentTimeMillis());
        zeroTrustUserSignDTO.setCallerNounce(UUIDUtils.getUUID());

        Map<String, String> params = new HashMap<>();
        params.put("userToken", zeroTrustUserSignDTO.getUserToken());
        params.put("callerId", zeroTrustUserSignDTO.getCallerId());
        params.put("callerTimestamp", String.valueOf(zeroTrustUserSignDTO.getCallerTimestamp()));
        params.put("callerNounce", zeroTrustUserSignDTO.getCallerNounce());

        zeroTrustUserSignDTO.setCallerSign(getSign(params));
        return zeroTrustUserSignDTO;
    }

    /**
     * 获取app签名参数值
     *
     * @param appToken
     * @return
     */
    public ZeroTrustAppSignDTO getZeroTrustAppSign(String appToken) {
        if (StrUtil.isBlank(appToken)) {
            throw new BizException("零信任AppToken不存在！");
        }
        ZeroTrustAppSignDTO zeroTrustAppSignDTO = new ZeroTrustAppSignDTO();
        zeroTrustAppSignDTO.setAppToken(appToken);
        zeroTrustAppSignDTO.setCallerId(CALLER_ID);
        zeroTrustAppSignDTO.setCallerTimestamp(System.currentTimeMillis());
        zeroTrustAppSignDTO.setCallerNounce(UUIDUtils.getUUID());

        Map<String, String> params = new HashMap<>();
        params.put("appToken", zeroTrustAppSignDTO.getAppToken());
        params.put("callerId", zeroTrustAppSignDTO.getCallerId());
        params.put("callerTimestamp", String.valueOf(zeroTrustAppSignDTO.getCallerTimestamp()));
        params.put("callerNounce", zeroTrustAppSignDTO.getCallerNounce());

        zeroTrustAppSignDTO.setCallerSign(getSign(params));
        return zeroTrustAppSignDTO;
    }

    public String getSign(Map<String, String> params) {
        //字段排序
        List<String> keys = new ArrayList<>(params.keySet());
        keys.sort(null);
        String signStr = keys.stream().map(key -> {
            String temp = key + "=";
            String val = params.get(key);
            if (StrUtil.isNotBlank(val)) {
                temp += val;
            }
            return temp;
        }).collect(Collectors.joining("&"));
        return getSignOfEncrypt(signStr + SIGN_SECRET);
    }

    /**
     * 获取续签的加密签名
     *
     * @param sign
     * @return
     */
    public String getSignOfEncrypt(String sign) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new BizException("签名加密出错！");
        }
        byte[] hash = messageDigest.digest(sign.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    /**
     * 将字符串排序
     *
     * @param str
     * @return
     */
    public String sortFor(String str) {
        StringBuilder sortStr = new StringBuilder(str);
        for (int i = 0; i < sortStr.length(); i++) {
            int targetIndex = i;
            for (int j = i + 1; j < sortStr.length(); j++) {
                if (sortStr.charAt(targetIndex) > sortStr.charAt(j)) {
                    targetIndex = j;
                }
            }
            if (targetIndex != i) {
                char targetChar = sortStr.charAt(targetIndex);
                sortStr.replace(targetIndex, targetIndex + 1, String.valueOf(sortStr.charAt(i)));
                sortStr.replace(i, i + 1, String.valueOf(targetChar));
            }
        }
        return sortStr.toString();
    }


    public static void main(String[] args) {
        System.out.println(UUIDUtils.getUUID());
    }
}
