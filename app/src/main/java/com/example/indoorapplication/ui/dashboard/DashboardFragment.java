package com.example.indoorapplication.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.indoorapplication.R;
import com.example.indoorapplication.RSSIChart;
import lecho.lib.hellocharts.view.LineChartView;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private RSSIChart rssiChart;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        rssiChart = new RSSIChart((LineChartView) root.findViewById(R.id.rssi_line_chart));
        return root;
    }

    public void updateChart(int rssi){
        rssiChart.updateChart(rssi);
    }
}