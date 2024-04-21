package com.yamo.cdcommoncore.support;


import cn.hutool.json.JSONArray;

/**
 * 字典处理接口
 * @author mowanhui
 * @version 1.0
 * @date 2023/9/7 11:15
 */
public interface DictHandler {
    JSONArray getDictList(String dictType);
}
