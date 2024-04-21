package com.yamo.cdsysmng.service;

import com.yamo.cdcommonmybatis.domain.vo.PageVO;
import com.yamo.cdcommonmybatis.service.IBaseService;
import com.yamo.cdsysmng.domain.dto.DictDTO;
import com.yamo.cdsysmng.domain.entity.SysDict;
import com.yamo.cdsysmng.domain.vo.DictVO;

import java.util.List;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author mwh
 * @since 2023-08-14
 */
public interface ISysDictService extends IBaseService<SysDict> {
    void addDictList(DictDTO.AddDictList addDictList);
    void addDict(DictDTO.AddDict addDict);
    void editDict(DictDTO.EditDict editDict);
    void delDict(DictDTO.DelDict delDict);

    List<DictVO> getDictTree(DictDTO.SearchDict searchDict);

    List<DictVO> getDictList(DictDTO.SearchDict searchDict);
    PageVO<DictVO> getDictPage(DictDTO.SearchDictPage searchDict);
}
