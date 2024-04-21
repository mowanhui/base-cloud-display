package com.yamo.cdcommones.domain.geo;

import lombok.Data;

/**
 * @Author: 林文杰
 * @Description: 查询统计-Geo-多边形查参数类
 * @Date: 2020/6/24 11:29
 * @version 1.1
 * @ModifiedBy: 林文杰
 */
@Data
public class PolygonRequestBO extends GeoRequestBO {
    /**
     * 多边形坐标
     * 二维数组
     */
    private String coordinates;
}
