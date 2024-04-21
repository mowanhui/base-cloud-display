package com.yamo.cdcommoncore.domain.dto;

import cn.hutool.json.JSONObject;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface MeiYaDataDTO {
    @Data
    class SaveMeiYaDataDTO{
        @NotBlank
        private String tableIndex;
        @NotNull
        private JSONObject entity;
    }
    @Data
    class DeleteMeiYaDataDTO{
        @NotBlank
        private String tableIndex;
        @NotEmpty
        private List<String> ids;
    }
}
