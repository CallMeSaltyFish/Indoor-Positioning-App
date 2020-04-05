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
import com.example.indoorapplication.util.Regression;
import lecho.lib.hellocharts.view.LineChartView;

import java.io.*;
import java.util.*;


public class NotificationsFragment extends Fragment {

    private final static String DATA_FILE = "/data.txt";
    private NotificationsViewModel notificationsViewModel;
    private Button dataProcessingButton;
    private Button dataExportButton;
    private Button dataImportButton;
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
        dataExportButton = root.findViewById(R.id.data_export_button);
        dataExportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportData();
            }
        });
        dataImportButton = root.findViewById(R.id.data_import_button);
        dataImportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importData();
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
        updatePointValue(new Database(getContext()).getRSSI());
    }

    private void updatePointValue(HashMap<Integer, Integer> map) {
        points = map;
        //        HashMap<Integer, ArrayList<Integer>> map = new Database(getContext()).get();
//        for (Map.Entry entry : map.entrySet()) {
//            int distance = (int) entry.getKey();
//            List<Integer> rssiList = (ArrayList<Integer>) entry.getValue();
//            for (int rssi : rssiList)
//                pointValues.add(new PointValue(rssi, distance));
//        }
        dataChart.updateChart(points);
    }

    private void processData() {
        List<Integer> rssiList = new ArrayList(points.keySet());
        Collections.sort(rssiList);
        for (Integer rssi : rssiList)
            System.out.println("(" + rssi + ", " + points.get(rssi) + ")");
        System.out.println("process data");
        Double[] list = {0.0012, -0.2036, 11.5325, -211.5911};
        Regression.updateCoeffient(list);
        System.out.println(new Database(getContext()).getRegressionCoefficient());
    }

    private void exportData() {
        File baseDirectory = getContext().getExternalFilesDir(null);
        if (!baseDirectory.exists())
            baseDirectory.mkdir();
        BufferedWriter wout = null;
        try {
            wout = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(baseDirectory.getPath() + DATA_FILE)));
            for (Integer distance : points.keySet()) {
                int rssi = points.get(distance);
                wout.write(distance + "=" + rssi);
                wout.newLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (wout != null) {
                try {
                    wout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void importData() {
        File dataFile = new File(getContext().getExternalFilesDir(null), DATA_FILE);
        if (!dataFile.exists())
            return;
        BufferedReader rin = null;
        HashMap<Integer, Integer> result = new HashMap<>();
        try {
            rin = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile)));
            for (String line = rin.readLine(); line != null; line = rin.readLine()) {
                String[] tokens = line.split("=");
                Integer distance = Integer.parseInt(tokens[0]);
                Integer rssi = Integer.parseInt(tokens[1]);
                result.put(distance, rssi);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (rin != null) {
                try {
                    rin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        updatePointValue(result);
    }
}