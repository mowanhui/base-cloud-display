package com.yamo.cdsysmng.service;

import com.yamo.cdcommonmybatis.domain.vo.PageVO;
import com.yamo.cdsysmng.domain.dto.LogDTO;
import com.yamo.cdsysmng.domain.entity.SysLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yamo.cdsysmng.domain.vo.LogVO;

/**
 * <p>
 * 系统日志 服务类
 * </p>
 *
 * @author mwh
 * @since 2023-08-14
 */
public interface ISysLogService extends IService<SysLog> {
    PageVO<LogVO> getPage(LogDTO.SearchLog searchLog);

}
