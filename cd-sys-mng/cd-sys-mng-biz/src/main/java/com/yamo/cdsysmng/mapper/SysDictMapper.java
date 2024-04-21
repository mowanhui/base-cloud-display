package com.yamo.cdsysmng.mapper;

import com.yamo.cdsysmng.domain.dto.DictDTO;
import com.yamo.cdsysmng.domain.entity.SysDict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yamo.cdsysmng.domain.vo.DictVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 字典表 Mapper 接口
 * </p>
 *
 * @author mwh
 * @since 2023-08-14
 */
public interface SysDictMapper extends BaseMapper<SysDict> {
    List<DictVO> getDictList(@Param("searchDict") DictDTO.SearchDict searchDict);
}
