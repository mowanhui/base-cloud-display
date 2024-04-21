package com.yamo.cdsysmng.mapper;

import com.yamo.cdsysmng.domain.dto.LogDTO;
import com.yamo.cdsysmng.domain.entity.SysLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yamo.cdsysmng.domain.vo.LogVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统日志 Mapper 接口
 * </p>
 *
 * @author mwh
 * @since 2023-08-14
 */
public interface SysLogMapper extends BaseMapper<SysLog> {
    List<LogVO> getList(@Param("search") LogDTO.SearchLog searchLog);
}
