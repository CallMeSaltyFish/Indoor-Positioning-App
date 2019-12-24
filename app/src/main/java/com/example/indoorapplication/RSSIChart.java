package com.example.indoorapplication;

import android.graphics.Color;
import android.view.View;
import lecho.lib.hellocharts.gesture.*;
import lecho.lib.hellocharts.model.*;
import lecho.lib.hellocharts.view.*;

import java.util.*;

public class RSSIChart {
    private List<AxisValue> chartAxisXValues;
    private List<PointValue> chartPointValues;
    private LineChartView chartView;

    public RSSIChart(LineChartView lineChartView) {
        chartView = lineChartView;
        chartAxisXValues = new ArrayList<>();
        chartPointValues = new ArrayList<>();
        for (Integer i = 0; i <= 100; ++i)
            chartAxisXValues.add(new AxisValue(i).setLabel(i.toString()));
//        for (Integer i = 0; i <= 100; ++i)
//            chartPointValues.add(new PointValue(i, rand.nextInt()));
        createLines();
        /**注：下面的7，10只是代表一个数字去类比而已
         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         */
        Viewport v = new Viewport(chartView.getMaximumViewport());
        v.left = 0;
        v.right = 100;
        chartView.setCurrentViewport(v);
    }

    private void createLines() {
        Line line = new Line(chartPointValues).setColor(Color.parseColor("#FFCD41"));  //折线的颜色（橙色）
        List<Line> lines = new ArrayList<>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(true);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(false);//曲线的数据坐标是否加上备注
        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.WHITE);  //设置字体颜色
        //axisX.setName("date");  //表格名称
        axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(8); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(chartAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setName("");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边

        //设置行为属性，支持缩放、滑动以及平移
        chartView.setInteractive(true);
        chartView.setZoomType(ZoomType.HORIZONTAL);
        chartView.setMaxZoom((float) 2);//最大方法比例
        chartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        chartView.setLineChartData(data);
        chartView.setVisibility(View.VISIBLE);
    }

    public void updateChart(int rssi) {
        for (PointValue p : chartPointValues)
            p.set(p.getX() + 1, p.getY());
        chartPointValues.add(new PointValue(0, rssi));
        createLines();
    }

    public void eraseChart() {
        chartView.setLineChartData(null);
        chartView.setVisibility(View.INVISIBLE);
    }

}
