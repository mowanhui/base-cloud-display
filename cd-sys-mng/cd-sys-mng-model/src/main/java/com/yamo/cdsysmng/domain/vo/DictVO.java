package com.yamo.cdsysmng.domain.vo;

import com.yamo.cdcommoncore.domain.vo.TreeVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author mowanhui
 * @version 1.0
 * @date 2023/6/5 21:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DictVO extends TreeVO<DictVO> {
    /**
     * 字典Id
     */
    private String dictId;

    /**
     * 父Id
     */
    private String parentId;

    /**
     * 字典代码
     */
    private String dictCode;

    /**
     * 字典名称
     */
    private String dictName;

    private String dictTypeCode;

    private String dictTypeName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否启用
     */
    private String isEnable;

    private String value1;
    private String value2;
    private String value3;

}
