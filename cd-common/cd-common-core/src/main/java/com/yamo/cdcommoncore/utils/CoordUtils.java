package com.yamo.cdcommoncore.utils;

import lombok.experimental.UtilityClass;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * 坐标工具
 */
@UtilityClass
public class CoordUtils {
    /**
     * 判断点是否在多边形内（包括顶点与边）
     * @param point
     * @param pts
     * @return
     */
    public boolean isPtInPoly(Point2D.Double point, List<Point2D.Double> pts){
        int N = pts.size();
        boolean boundOrVertex = true; //如果点位于多边形的顶点或边上，也算做点在多边形内，直接返回true
        int intersectCount = 0;//cross points count of x
        double precision = 2e-10; //浮点类型计算时候与0比较时候的容差
        Point2D.Double p1, p2;//neighbour bound vertices
        Point2D.Double p = point; //当前点

        p1 = pts.get(0);//left vertex
        for(int i = 1; i <= N; ++i){//check all rays
            if(p.equals(p1)){
                return boundOrVertex;//p is an vertex
            }

            p2 = pts.get(i % N);//right vertex
            if(p.x < Math.min(p1.x, p2.x) || p.x > Math.max(p1.x, p2.x)){//ray is outside of our interests
                p1 = p2;
                continue;//next ray left point
            }

            if(p.x > Math.min(p1.x, p2.x) && p.x < Math.max(p1.x, p2.x)){//ray is crossing over by the algorithm (common part of)
                if(p.y <= Math.max(p1.y, p2.y)){//x is before of ray
                    if(p1.x == p2.x && p.y >= Math.min(p1.y, p2.y)){//overlies on a horizontal ray
                        return boundOrVertex;
                    }

                    if(p1.y == p2.y){//ray is vertical
                        if(p1.y == p.y){//overlies on a vertical ray
                            return boundOrVertex;
                        }else{//before ray
                            ++intersectCount;
                        }
                    }else{//cross point on the left side
                        double xinters = (p.x - p1.x) * (p2.y - p1.y) / (p2.x - p1.x) + p1.y;//cross point of y
                        if(Math.abs(p.y - xinters) < precision){//overlies on a ray
                            return boundOrVertex;
                        }

                        if(p.y < xinters){//before ray
                            ++intersectCount;
                        }
                    }
                }
            }else{//special case when ray is crossing through the vertex
                if(p.x == p2.x && p.y <= p2.y){//p crossing over p2
                    Point2D.Double p3 = pts.get((i+1) % N); //next vertex
                    if(p.x >= Math.min(p1.x, p3.x) && p.x <= Math.max(p1.x, p3.x)){//p.x lies between p1.x & p3.x
                        ++intersectCount;
                    }else{
                        intersectCount += 2;
                    }
                }
            }
            p1 = p2;//next ray left point
        }

        //偶数在多边形外
        //奇数在多边形内
        return intersectCount % 2 != 0;
    }

    public static void main(String[] args) {
        List<Point2D.Double> pts=new ArrayList<>();
        Point2D.Double p1=new Point2D.Double(1.0,1.0);
        Point2D.Double p2=new Point2D.Double(4.0,1.0);
        Point2D.Double p3=new Point2D.Double(1.0,4.0);
        Point2D.Double p4=new Point2D.Double(4.0,4.0);
        pts.add(p1);
        pts.add(p2);
        pts.add(p3);
        pts.add(p4);
        Point2D.Double p=new Point2D.Double(2.0,5.0);
        System.out.println(isPtInPoly(p,pts));
    }
}
