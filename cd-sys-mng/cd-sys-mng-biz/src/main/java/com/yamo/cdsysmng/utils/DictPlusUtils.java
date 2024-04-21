package com.yamo.cdsysmng.utils;

import cn.hutool.json.JSONUtil;
import com.yamo.cdcommoncore.exception.BizException;
import com.yamo.cdcommoncore.utils.DictUtils;
import com.yamo.cdsysmng.domain.dto.DictDTO;
import com.yamo.cdsysmng.service.ISysDictService;

/**
 * 字典加强版
 * @author mowanhui
 * @version 1.0
 * @date 2023/9/7 11:39
 */
public class DictPlusUtils<T>{
    public DictUtils<T> build(ISysDictService dictService){
        return new DictUtils<>(dictType->{
            if (dictService == null) {
                throw new BizException("字典服务不能为空");
            }
            DictDTO.SearchDict searchDict = new DictDTO.SearchDict();
            searchDict.setDictTypeCode(dictType);
            return JSONUtil.parseArray(dictService.getDictList(searchDict));
        });
    }
}
