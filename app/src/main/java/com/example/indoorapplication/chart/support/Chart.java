package com.example.indoorapplication.chart.support;

import com.example.indoorapplication.chart.bean.Axis;

/**
 * Created by chenliu on 2016/7/15.<br/>
 * 描述：
 * </br>
 */
public interface Chart {

    void setAxisX(Axis axisX);

    void setAxisY(Axis axisY);

    Axis getAxisX();

    Axis getAxisY();
}
