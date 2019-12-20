package com.example.indoorapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;
import lecho.lib.hellocharts.view.LineChartView;

@SuppressLint("NewApi")
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MainActivity extends AppCompatActivity {
    private static Integer currentRSSI;
    private RSSIChart rssiChart;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private TextView rssiTextView;
    private RecyclerView deviceView;
    private DeviceScanner scanner;
    private ServiceConnection scannerConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            scanner = ((DeviceScanner.ScannerBinder) iBinder).getScanner();
            scanner.setScannerListener(new DeviceScanner.ScannerListener() {
                @Override
                public void showScanResult(final int rssi, final int idx) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            currentRSSI = rssi;
                            rssiChart.updateChart(rssi);
                            rssiTextView.setText("Device: " + idx + " RSSI: " + rssi);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        rssiChart = new RSSIChart((LineChartView) findViewById(R.id.rssi_line_chart));

        rssiTextView = (TextView) findViewById(R.id.device_rssi);

        if (!((BluetoothManager) getSystemService(BLUETOOTH_SERVICE)).getAdapter().isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, Activity.RESULT_OK);
        }

        startScan();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(scannerConn);
    }

    private void startScan() {
        Intent startIntent = new Intent(this, DeviceScanner.class);
        bindService(startIntent, scannerConn, BIND_AUTO_CREATE);
    }
}
