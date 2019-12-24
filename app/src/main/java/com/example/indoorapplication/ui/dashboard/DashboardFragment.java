package com.example.indoorapplication.ui.dashboard;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.indoorapplication.DeviceScanner;
import com.example.indoorapplication.R;
import com.example.indoorapplication.RSSIChart;
import lecho.lib.hellocharts.view.LineChartView;

import static android.content.Context.BIND_AUTO_CREATE;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class DashboardFragment extends Fragment {

    private boolean isActive;
    private boolean isScanning;
    private DashboardViewModel dashboardViewModel;
    private Button startScanButton;
    private Button stopScanButton;
    private RSSIChart rssiChart;
    private DeviceScanner scanner;
    private ServiceConnection scannerConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            scanner = ((DeviceScanner.ScannerBinder) iBinder).getScanner();
            scanner.setScannerListener(new DeviceScanner.ScannerListener() {
                @Override
                public void showScanResult(final int rssi, final int idx) {
                    if (getActivity() == null || !isActive)
                        return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //currentRSSI = rssi;
                            updateChart(rssi);
                            //rssiTextView.setText("Device: " + idx + " RSSI: " + rssi);
                        }
                    });
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        isActive = false;
        isScanning =false;
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        startScanButton=root.findViewById(R.id.start_scan_button);
        startScanButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!isScanning){
                    startScan();
                    isScanning=true;
                }
            }
        });
        stopScanButton=root.findViewById(R.id.stop_scan_button);
        stopScanButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                    stopScan();
                    isScanning=false;
            }
        });
        rssiChart = new RSSIChart((LineChartView) root.findViewById(R.id.rssi_line_chart));
        return root;
    }

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (hidden) {  //不在最前端界面显示
//            stopScan();
//        } else {  //重新显示到最前端
//            startScan();
//        }
//    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//       getActivity().unbindService(scannerConn);
//    }

    @Override
    public void onPause() {
        super.onPause();
        isActive = false;
        //rssiChart.eraseChart();
        //getActivity().unbindService(scannerConn);
    }

    @Override
    public void onResume() {
        super.onResume();
        isActive = true;
    }

    private void updateChart(int rssi) {
        rssiChart.updateChart(rssi);
    }

    private void startScan() {
        Intent startIntent = new Intent(getActivity(), DeviceScanner.class);
        getActivity().bindService(startIntent, scannerConn, BIND_AUTO_CREATE);
    }

    private void stopScan() {
        getActivity().unbindService(scannerConn);
    }
}