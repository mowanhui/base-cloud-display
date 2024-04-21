package com.yamo.cdsysmng.controller;


import com.yamo.cdcommoncore.result.ResultVO;
import com.yamo.cdcommonlog.annotation.OptLog;
import com.yamo.cdcommonlog.annotation.OptLogTag;
import com.yamo.cdcommonlog.enums.LogSourceEnum;
import com.yamo.cdcommonlog.enums.OptActionEnum;
import com.yamo.cdcommonsupport.annotation.Auth;
import com.yamo.cdsysmng.domain.dto.DictDTO;
import com.yamo.cdsysmng.service.ISysDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 * @author mwh
 * @since 2023-08-14
 */
@Api(tags = "字典管理")
@OptLogTag(value = "字典管理",logSource = LogSourceEnum.SYS_MNG)
@RestController
@RequestMapping("/dict")
@RequiredArgsConstructor
public class SysDictController {
    private final ISysDictService iSysDictService;
    @ApiOperation("批量添加字典")
    @OptLog(value = "批量添加字典",optAction = OptActionEnum.INSERT)
    @Auth
    @PostMapping("addDictList")
    public ResultVO<Object> addDictList(@RequestBody @Validated DictDTO.AddDictList addDictList){
        iSysDictService.addDictList(addDictList);
        return ResultVO.ok();
    }
    @ApiOperation("添加字典")
    @OptLog(value = "添加字典",optAction = OptActionEnum.INSERT)
    @Auth
    @PostMapping("addDict")
    public ResultVO<Object> addDict(@RequestBody @Validated DictDTO.AddDict addDict){
        iSysDictService.addDict(addDict);
        return ResultVO.ok();
    }



    @ApiOperation("修改字典")
    @OptLog(value = "修改字典",optAction = OptActionEnum.UPDATE)
    @Auth
    @PostMapping("editDict")
    public ResultVO<Object> editDict(@RequestBody @Validated DictDTO.EditDict editDict){
        iSysDictService.editDict(editDict);
        return ResultVO.ok();
    }

    @ApiOperation("删除字典")
    @OptLog(value = "删除字典",optAction = OptActionEnum.DELETE)
    @Auth
    @PostMapping("delDict")
    public ResultVO<Object> delDict(@RequestBody @Validated DictDTO.DelDict delDict){
        iSysDictService.delDict(delDict);
        return ResultVO.ok();
    }

    @ApiOperation("获取字典树")
    @OptLog(value = "获取字典树",optAction = OptActionEnum.READ)
    @GetMapping("getDictTree")
    public ResultVO<Object> getDictTree(@Validated DictDTO.SearchDict searchDict){
        return ResultVO.ok(iSysDictService.getDictTree(searchDict));
    }

    @Auth
    @ApiOperation("获取字典列表")
    @OptLog(value = "获取字典列表",optAction = OptActionEnum.READ)
    @GetMapping("getDictList")
    public ResultVO<Object> getDictList(@Validated DictDTO.SearchDict searchDict){
        return ResultVO.ok(iSysDictService.getDictList(searchDict));
    }

    @ApiOperation("获取字典分页")
    @GetMapping("getDictPage")
    public ResultVO<Object> getDictPage(@Validated DictDTO.SearchDictPage searchDict){
        return ResultVO.ok(iSysDictService.getDictPage(searchDict));
    }
}

