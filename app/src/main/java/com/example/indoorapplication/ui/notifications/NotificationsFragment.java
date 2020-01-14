package com.example.indoorapplication.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.indoorapplication.DataChart;
import com.example.indoorapplication.R;
import com.example.indoorapplication.util.Database;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

import java.util.*;


public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private Button dataProcessingButton;
    private DataChart dataChart;
    private LineChartView chartView;
    private HashMap<Integer, Integer> points;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        chartView = root.findViewById(R.id.data_line_chart);
        dataChart = new DataChart(chartView);
        updatePointValue();
        dataProcessingButton = root.findViewById(R.id.data_processing_button);
        dataProcessingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processData();
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        updatePointValue();
    }

    private void updatePointValue() {
        points = new Database(getContext()).get();
        List<PointValue> pointValues = new ArrayList<>();
//        HashMap<Integer, ArrayList<Integer>> map = new Database(getContext()).get();
//        for (Map.Entry entry : map.entrySet()) {
//            int distance = (int) entry.getKey();
//            List<Integer> rssiList = (ArrayList<Integer>) entry.getValue();
//            for (int rssi : rssiList)
//                pointValues.add(new PointValue(rssi, distance));
//        }
        for (Integer distance : points.keySet()) {
            int rssi = points.get(distance);
            pointValues.add(new PointValue(rssi, distance));
        }
        dataChart.updateChart(pointValues);
    }

    private void processData() {
        List<Integer> rssiList = new ArrayList(points.keySet());
        Collections.sort(rssiList);
        for (Integer rssi : rssiList)
            System.out.println("(" + rssi + ", " + points.get(rssi) + ")");
        System.out.println("process data");
    }
}