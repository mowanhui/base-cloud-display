package com.yamo.cdcommones.domain.geo;

import lombok.Data;

/**
 * @Author: 林文杰
 * @Description: 查询统计-Geo-空间查询基类
 * @Date: 2020/6/24 11:29
 * @version 1.1
 * @ModifiedBy: 林文杰
 */
@Data
public class GeoRequestBO {
    /**
     * 形状关系
     */
    private String shapeRelation;
}
