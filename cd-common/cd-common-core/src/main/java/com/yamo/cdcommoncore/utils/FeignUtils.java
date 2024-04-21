package com.yamo.cdcommoncore.utils;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yamo.cdcommoncore.exception.BizException;
import com.yamo.cdcommoncore.result.ResultVO;
import com.yamo.cdcommoncore.result.ResultCode;
import lombok.experimental.UtilityClass;

/**
 * 远程服务工具
 * @author mowanhui
 * @version 1.0
 * @date 2023/8/23 14:48
 */
@UtilityClass
public class FeignUtils {
    public JSONArray toJSONArray(ResultVO<Object> result){
        if(result.getCode()!= ResultCode.SUCCESS.getCode()){
            throw new BizException(ResultCode.BUSY);
        }
        return JSONUtil.parseArray(result.getData());
    }

    public JSONObject toJSONObject(ResultVO<Object> result){
        if(result.getCode()!= ResultCode.SUCCESS.getCode()){
            throw new BizException(ResultCode.BUSY);
        }
        return JSONUtil.parseObj(result.getData());
    }
}
