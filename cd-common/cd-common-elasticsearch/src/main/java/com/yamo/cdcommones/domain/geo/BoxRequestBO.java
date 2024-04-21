package com.yamo.cdcommones.domain.geo;

import lombok.Data;

/**
 * @Author: 林文杰
 * @Description: 查询统计-Geo-框查参数类
 * @Date: 2020/6/24 11:29
 * @version 1.1
 * @ModifiedBy: 林文杰
 */
@Data
public class BoxRequestBO extends GeoRequestBO{
    /**
     * 左上角 x 坐标
     */
    private Double topLeftCornerX;
    /**
     * 左上角 y 坐标
     */
    private Double topLeftCornerY;
    /**
     * 右下角 x 坐标
     */
    private Double bottomRightCornerX;
    /**
     * 右下角 y 坐标
     */
    private Double bottomRightCornerY;
}
