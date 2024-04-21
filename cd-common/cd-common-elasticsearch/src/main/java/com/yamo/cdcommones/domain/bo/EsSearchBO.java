package com.yamo.cdcommones.domain.bo;

import cn.hutool.json.JSONObject;
import lombok.Data;

import java.util.List;

@Data
public class EsSearchBO {
    private Integer total;
    private List<JSONObject> dataList;
}
