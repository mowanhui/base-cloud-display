package com.yamo.cdcommoncore.utils;

import cn.hutool.core.util.StrUtil;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrToolUtils {

    //驼峰转下划线
    public static String camelToUnderline(String str){
        Pattern compile=Pattern.compile("[A-Z]");
        Matcher matcher=compile.matcher(str);
        StringBuffer sb=new StringBuffer();
        while (matcher.find()){
            matcher.appendReplacement(sb,"_"+matcher.group(0));
        }
        matcher.appendTail(sb);
        return sb.toString().toUpperCase();
    }

    public static void main(String[] args) {
        System.out.println(camelToUnderline("shipWarnTar"));
    }

    /**
     * 根据matches的先后顺序进行判断
     * @param originStr
     * @param matches
     * @return
     */
    public static String isContain(String originStr,String[] matches,String other){
        if(matches.length==0|| StrUtil.isBlank(originStr)){
            return other;
        }
        for(String str:matches){
            if(originStr.contains(str)){
                return str;
            }
        }
        return other;
    }

    /**
     * 根据matcheMap的先后顺序进行判断
     * @param originStr
     * @param matcheMap
     * @param other
     * @return
     */
    public static String isContain(String originStr, Map<String,String> matcheMap, String other){
        if(matcheMap.isEmpty()|| StrUtil.isBlank(originStr)){
            return other;
        }
        String val=null;
        for(Map.Entry<String,String> item:matcheMap.entrySet()){
            if(originStr.contains(item.getKey())){
                return item.getValue();
            }
        }
        return other;
    }

}
