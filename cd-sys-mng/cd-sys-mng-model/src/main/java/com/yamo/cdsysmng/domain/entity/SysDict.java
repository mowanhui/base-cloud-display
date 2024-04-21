package com.yamo.cdsysmng.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.yamo.cdcommonmybatis.domain.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 字典表
 * </p>
 *
 * @author mwh
 * @since 2023-08-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_sys_dict")
public class SysDict extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

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

    /**
     * 字典类型代码：-00-00-
     */
    private String dictTypeCode;

    /**
     * 字典类型名称：-状态-
     */
    private String dictTypeName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 排序
     */
    private Integer sort;
    private String isEnable;
    private String value1;
    private String value2;
    private String value3;
}
