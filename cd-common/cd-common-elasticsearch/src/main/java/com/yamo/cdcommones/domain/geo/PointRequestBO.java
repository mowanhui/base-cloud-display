package com.yamo.cdcommones.domain.geo;

import lombok.Data;

/**
 * @Author: 林文杰
 * @Description: 查询统计-Geo-点查参数类
 * @Date: 2020/6/24 11:29
 * @version 1.1
 * @ModifiedBy: 林文杰
 */
@Data
public class PointRequestBO extends GeoRequestBO {
    /**
     * 纬度
     */
    private Double x;
    /**
     * 经度
     */
    private Double y;
}
