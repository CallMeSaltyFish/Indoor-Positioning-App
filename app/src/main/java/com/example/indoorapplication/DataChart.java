package com.example.indoorapplication;

import android.graphics.Color;
import android.view.View;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.*;
import lecho.lib.hellocharts.view.LineChartView;

import java.util.*;

public class DataChart {
    private LineChartData chartData;
    private LineChartView chartView;
    private Line dataPoints;

    public DataChart(LineChartView lineChartView) {
        chartView = lineChartView;
        chartData = new LineChartData();
        dataPoints = new Line();
        dataPoints.setColor(Color.parseColor("#18B2C6"));
        dataPoints.setShape(ValueShape.CIRCLE);
        dataPoints.setCubic(true);
        dataPoints.setFilled(false);
        dataPoints.setHasLabels(false);
        dataPoints.setHasLabelsOnlyForSelected(true);
        dataPoints.setHasLines(false);//是否用线显示。如果为false 则没有曲线只有点显示
        dataPoints.setHasPoints(true);
    }

    public void updateChart(HashMap<Integer, Integer> points) {
        List<PointValue> chartPointValues = new ArrayList<>();
        for (Integer distance : points.keySet()) {
            int rssi = points.get(distance);
            chartPointValues.add(new PointValue(rssi, distance));
        }
        updateChart(chartPointValues);
    }

    public void updateChart(List<PointValue> chartPointValues) {
        dataPoints.setValues(chartPointValues);
        chartData.setLines(Arrays.asList(dataPoints));

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.WHITE);  //设置字体颜色
        //axisX.setName("date");  //表格名称
        axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(8); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        //axisX.setValues(chartAxisXValues);  //填充X轴的坐标名称
        chartData.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setName("");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        chartData.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边

        //设置行为属性，支持缩放、滑动以及平移
        chartView.setInteractive(true);
        chartView.setZoomType(ZoomType.HORIZONTAL);
        chartView.setMaxZoom((float) 2);//最大方法比例
        chartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        chartView.setLineChartData(chartData);
        chartView.setVisibility(View.VISIBLE);
    }
}
