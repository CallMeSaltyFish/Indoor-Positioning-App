package com.example.indoorapplication;

import lecho.lib.hellocharts.model.ChartData;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.view.LineChartView;

public class DataChart {
    private LineChartData chartData;
    private LineChartView chartView;
    public DataChart(LineChartView lineChartView){
        chartView=lineChartView;
        chartData = new LineChartData();
    }
//    private ChartData query(){
//        ChartData data=new LineChartData();
//
//    }
}
