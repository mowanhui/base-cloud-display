package com.yamo.cdsysmng.controller;


import com.yamo.cdcommoncore.result.ResultVO;
import com.yamo.cdcommonlog.annotation.OptLogTag;
import com.yamo.cdcommonlog.enums.LogSourceEnum;
import com.yamo.cdcommonmybatis.domain.vo.PageVO;
import com.yamo.cdsysmng.domain.dto.LogDTO;
import com.yamo.cdsysmng.domain.vo.LogVO;
import com.yamo.cdsysmng.service.ISysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 系统日志 前端控制器
 * </p>
 *
 * @author mwh
 * @since 2023-08-14
 */
@Api(tags = "日志管理")
@OptLogTag(value = "日志管理",logSource = LogSourceEnum.SYS_MNG)
@RestController
@RequestMapping("/sys-log")
@RequiredArgsConstructor
public class SysLogController {
    private final ISysLogService iSysLogService;
    //@Auth
    @ApiOperation("获取日志分页")
    @GetMapping("getPage")
    public ResultVO<PageVO<LogVO>> getPage(@Validated LogDTO.SearchLog searchLog){
        return ResultVO.ok(iSysLogService.getPage(searchLog));
    }
}
