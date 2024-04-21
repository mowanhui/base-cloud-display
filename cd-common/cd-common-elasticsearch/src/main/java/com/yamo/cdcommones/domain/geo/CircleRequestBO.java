package com.yamo.cdcommones.domain.geo;

import lombok.Data;

/**
 * @Author: 林文杰
 * @Description: 查询统计-Geo-圈查参数类
 * @Date: 2020/6/24 11:29
 * @version 1.1
 * @ModifiedBy: 林文杰
 */
@Data
public class CircleRequestBO extends GeoRequestBO {
    /**
     * 圈查的圆心 x 坐标
     */
    private Double circleCenterX;
    /**
     * 圈查的圆心 y 坐标
     */
    private Double circleCenterY;
    /**
     * 圈查的圆半径(米)
     */
    private Double meters;
    /**
     * 圈查的圆精度(多少个点组成的圆)
     */
    private Integer numPoints;
}
