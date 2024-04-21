package com.yamo.cdcommones.domain.geo;
/**
 * @Author: 潘粤华
 * @Description: 点位坐标对象
 * @Date: 2020/6/24 17:07
 * @version
 * @ModifiedBy:
 */
public class Position {
    /**
     * 纬度
     */
    private double x;
    /**
     * 经度
     */
    private double y;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public Position(String point){
        if(point.indexOf(",")>0){
            String x_ = point.split(",")[0];
            String y_ = point.split(",")[1];
            this.x = Double.parseDouble(x_);
            this.y = Double.parseDouble(y_);
        }else {

        }
    }

    public Position(){};
}
