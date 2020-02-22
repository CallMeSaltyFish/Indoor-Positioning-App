package com.example.indoorapplication.util;

/**
 * Created by lining on 2016/7/14.
 */
public class PointSimple {
    public PointSimple(double x, double y) {
        width_scale = x;
        height_scale = y;
    }

    // 标记点相对于横向的宽度的比例
    public double width_scale;
    // 标记点相对于横向的高度的比例
    public double height_scale;

}
