package com.yamo.cdcommonmybatis.utils;

import cn.hutool.json.JSONObject;
import com.yamo.cdcommonmybatis.domain.entity.BaseEntity;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;


/**
 * @author mowanhui
 * @version 1.0
 * @date 2023/8/17 11:46
 */
public class EntityUtils {
    public static void setCreateProperties(BaseEntity baseEntity, String userId){
        LocalDateTime now = LocalDateTime.now();
        baseEntity.setCreateTime(now);
        baseEntity.setCreateBy(userId);
        baseEntity.setUpdateTime(now);
        baseEntity.setUpdateBy(userId);
        baseEntity.setIsDeleted("0");
    }
    public static void setUpdateProperties(BaseEntity baseEntity,String userId){
        LocalDateTime now = LocalDateTime.now();
        baseEntity.setUpdateTime(now);
        baseEntity.setUpdateBy(userId);
    }

    public static void setDeleteProperties(BaseEntity baseEntity,String userId){
        LocalDateTime now = LocalDateTime.now();
        baseEntity.setDeleteTime(now);
        baseEntity.setDeleteBy(userId);
        baseEntity.setIsDeleted("1");
    }
    /**
     * 用于json存入数据库或es
     *将source的json格式化为target的json，并把source的值赋值给target
     * sourceConnect为source的连接符，targetConnect为target的连接符
     *
     * */
    public static JSONObject formatJson(JSONObject source,String sourceConnect,Object target,String targetConnect){
        JSONObject result = new JSONObject();
        for (Map.Entry<String, Object> sourceEntry : source) {
            String key = sourceEntry.getKey().toUpperCase().replaceAll(sourceConnect, "");
            Class<?> targetClass = target.getClass();
            Field[] fields = targetClass.getDeclaredFields();
            for (Field field : fields) {
            String tmp = field.getName().toUpperCase().replaceAll(targetConnect, "");
                if(key.equals(tmp)){
                    result.set(field.getName(),sourceEntry.getValue());
                    break;
                }
            }
        }
        return result;
    }
}
