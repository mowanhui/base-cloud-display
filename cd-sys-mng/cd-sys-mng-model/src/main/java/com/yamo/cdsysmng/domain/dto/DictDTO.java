package com.yamo.cdsysmng.domain.dto;

import com.yamo.cdcommonmybatis.domain.dto.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author mowanhui
 * @version 1.0
 * @date 2023/6/5 21:15
 */
public interface DictDTO {
    @Data
    @ApiModel(description = "添加字典")
    class AddDict {
        /**
         * 父Id
         */
        @ApiModelProperty("父Id")
        private String parentId;

        /**
         * 字典代码
         */
        @ApiModelProperty("字典代码")
        @NotBlank(message = "字典代码不能为空")
        private String dictCode;

        /**
         * 字典名称
         */
        @ApiModelProperty("字典名称")
        @NotBlank(message = "字典名称不能为空")
        private String dictName;

        /**
         * 备注
         */
        @ApiModelProperty("备注")
        private String remark;

        /**
         * 排序
         */
        @ApiModelProperty("排序")
        @NotNull(message = "排序不能为空")
        private Integer sort;

        /**
         * 是否启用
         */
        @ApiModelProperty("是否启用")
        @NotNull(message = "是否启用不能为空")
        private String isEnable;
        @ApiModelProperty("值1")
        private String value1;
        @ApiModelProperty("值2")
        private String value2;
        @ApiModelProperty("值3")
        private String value3;

    }

    @Data
    @ApiModel(description = "批量添加字典")
    class AddDictList{
        /**
         * 父Id
         */
        @ApiModelProperty("父Id")
        private String parentId;
        @ApiModelProperty("字典名称")
        @NotNull(message = "字典名称不能为空")
        private List<String> dictNameList;
    }

    @Data
    @ApiModel("修改字典")
    class EditDict {
        @ApiModelProperty("dictId")
        @NotBlank(message = "dictId不能为空")
        private String dictId;

        /**
         * 父Id
         */
        @ApiModelProperty("父Id")
        private String parentId;

        /**
         * 字典代码
         */
        @ApiModelProperty("字典代码")
        @NotBlank(message = "字典代码不能为空")
        private String dictCode;

        /**
         * 字典名称
         */
        @ApiModelProperty("字典名称")
        @NotBlank(message = "字典名称不能为空")
        private String dictName;

        /**
         * 备注
         */
        @ApiModelProperty("备注")
        private String remark;

        /**
         * 排序
         */
        @ApiModelProperty("排序")
        @NotNull(message = "排序不能为空")
        private Integer sort;

        /**
         * 是否启用
         */
        @ApiModelProperty("是否启用")
        @NotNull(message = "是否启用不能为空")
        private String isEnable;

        @ApiModelProperty("值1")
        private String value1;
        @ApiModelProperty("值2")
        private String value2;
        @ApiModelProperty("值3")
        private String value3;

    }

    @Data
    @ApiModel("删除字典")
    class DelDict {
        @ApiModelProperty("dictId")
        @NotBlank(message = "dictId不能为空")
        private String dictId;
    }

    @Data
    @ApiModel("搜索字典")
    class SearchDict{
        @ApiModelProperty("关键词")
        private String keyword;
        @ApiModelProperty("字典类型代码")
        private String dictTypeCode;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @ApiModel("搜索字典分页")
    class SearchDictPage extends PageDTO {
        @ApiModelProperty("关键词")
        private String keyword;
        @ApiModelProperty("字典类型代码")
        private String dictTypeCode;
    }
}
