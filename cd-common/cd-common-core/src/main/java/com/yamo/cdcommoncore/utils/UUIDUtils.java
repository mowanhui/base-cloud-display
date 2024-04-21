package com.yamo.cdcommoncore.utils;

import java.util.UUID;

/**
 * uuid工具过滤
 * @author mowanhui
 * @version 1.0
 * @date 2023/5/13 14:11
 */
public class UUIDUtils {
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
