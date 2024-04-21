package com.yamo.cdsysmng.domain.dto;

import com.yamo.cdcommonmybatis.domain.dto.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

public interface LogDTO {
    @Data
    @ApiModel("搜索日志")
    class SearchLog extends PageDTO {
        @ApiModelProperty("日志类型，字典：log_type")
        private String logType;
        @ApiModelProperty("操作行为，字典：opt_action")
        private String optAction;
        @ApiModelProperty("日志标签")
        private String logTag;
        @ApiModelProperty("日志来源：log_source")
        private String logSource;
        @ApiModelProperty("日志种类：log_category")
        private String logCategory;
        @ApiModelProperty("标题")
        private String title;
    }
}
