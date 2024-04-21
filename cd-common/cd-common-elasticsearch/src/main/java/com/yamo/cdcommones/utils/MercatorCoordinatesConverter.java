package com.yamo.cdcommones.utils;


import com.yamo.cdcommones.domain.geo.Position;

/// <summary>
/// 墨卡托坐标转换（经纬度与米之间互转）
/// </summary>
public class MercatorCoordinatesConverter {

    private static double L = 6381372 * Math.PI * 2; // 地球周长

    /**
     * 经纬度转Web墨卡托（单位：米）
     * @param longitude 经度
     * @param latitude 纬度
     * @return 转换后的位置
     */
    public static Position Degree2WebMercatorMeter(double longitude, double latitude)
    {
        double x = (longitude * L) / 360;
        double y =
                ((Math.log(Math.tan(((90 + latitude) * Math.PI) / 360)) / (Math.PI / 180)) *L) / 360;
        return new Position(x, y);
    }

    /**
     * Web墨卡托转经纬度
     * @param x X坐标值（单位：米）
     * @param y Y坐标值（单位：米）
     * @return 转换后的位置
     */
    public static Position WebMercatorMeter2Degree(double x, double y)
    {
        double lon = (x * 360) / L;
        double lat =
                (180 / Math.PI) *
                        (2 * Math.atan(Math.exp((((y * 360) / L) * Math.PI) / 180)) -
                                Math.PI / 2);
        return new Position(lon, lat);
    }

    /**
     * 度转距离 （单位：米）
     * @param degree 度
     * @return
     */
    public static double Degree2Mile(double degree){
        double p = L/360;
        return  degree * p;
    }

    /**
     * 距离转度
     * @param mile 米
     * @return
     */
    public static double Mile2Degree(double mile){
        double p = 360/L;
        return mile * p;
    }
}
