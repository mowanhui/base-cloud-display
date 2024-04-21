package com.yamo.cdsysmng.service.impl;

import cn.hutool.json.JSONObject;
import com.yamo.cdcommonmybatis.domain.vo.PageVO;
import com.yamo.cdcommonmybatis.utils.PageUtils;
import com.yamo.cdsysmng.domain.dto.LogDTO;
import com.yamo.cdsysmng.domain.entity.SysLog;
import com.yamo.cdsysmng.domain.vo.LogVO;
import com.yamo.cdsysmng.mapper.SysLogMapper;
import com.yamo.cdsysmng.service.ISysDictService;
import com.yamo.cdsysmng.service.ISysLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yamo.cdsysmng.utils.DictPlusUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 系统日志 服务实现类
 * </p>
 *
 * @author mwh
 * @since 2023-08-14
 */
@RequiredArgsConstructor
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {
    private final ISysDictService dictService;
    @Override
    public PageVO<LogVO> getPage(LogDTO.SearchLog searchLog) {
        PageVO<LogVO> pageInfo= PageUtils.doPage(searchLog,()->this.baseMapper.getList(searchLog));
        //字典转化
        new DictPlusUtils<LogVO>().build(dictService)
                .setDataList(pageInfo.getList())
                .buildDictConvert()
                .setDictType("log_type")
                .setSource(LogVO::getLogType)
                .setTarget(LogVO::getLogTypeName)
                .next()
                .setDictType("opt_action")
                .setSource(LogVO::getOptAction)
                .setTarget(LogVO::getOptActionName)
                .next()
                .setDictType("log_source")
                .setSource(LogVO::getLogSource)
                .setTarget(LogVO::getLogSourceName)
                .next()
                .setDictType("log_category")
                .setSource(LogVO::getLogCategory)
                .setTarget(LogVO::getLogCategoryName)
                .toName();
        return pageInfo;
    }
    public void demo(){
        List<JSONObject> list=new ArrayList<>();
        new DictPlusUtils<JSONObject>().build(dictService)
                .setDataList(list)
                .buildDictConvertOfJson()
                .setDictType("bj_type")
                .setSource("BJ_TYPE")
                .next()
                .setDictType("jq_status")
                .setSource("JQ_STATUS")
                .next()
                .setDictType("jq_level")
                .setSource("JQ_LEVEL")
                .next()
                .setDictType("jq_level")
                .setSource("JQ_LEVEL")
                .toName();
    }
}
