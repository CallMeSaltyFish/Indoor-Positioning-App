package com.example.indoorapplication;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.os.*;
import androidx.annotation.RequiresApi;

import com.example.indoorapplication.util.Database;
import com.example.indoorapplication.util.Regression;
import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;

import java.util.*;

@SuppressLint("NewApi")
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class DeviceScanner extends Service {

    //    private static final String[] DEVICE_UUIDS = {"0112233445566778899AABBCCDDEEFF0"};
    // Stops scanning after given seconds.
    private double[][] devicePositions = {{0, 0}, {10, 0}, {0, 10}};
    private boolean dataCollectingMode = false;
    private static final long SCAN_PERIOD = 100000;
    //private boolean mScanning;
    private Database database;
    private Handler handler;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bluetoothScanner;
    private List<ScanFilter> scanFilters;
    private ScanSettings scanSettings;
    private ScanCallback scanCallback = new ScanCallback() {
        int flag;
        int[] rssiList;
        double[] distances;

        {
            flag = 0;
            //rssiList = new int[]{0, 0, 0};
            distances = new double[]{0, 0, 0};
        }

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            int idx = getDeviceIndex(result);
            if (idx != -1) {
                System.out.println("Device: " + idx + " RSSI: " + result.getRssi());
                int rssi = result.getRssi();
                if (dataCollectingMode)
                    scannerListener.updateScanResult(rssi, idx);
                else {
                    flag |= (1 << idx);
                    distances[idx] = Regression.calculateDistance(rssi);
                    //if (true) {
                    if (flag == 7) {
                        NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(devicePositions, distances), new LevenbergMarquardtOptimizer());
                        LeastSquaresOptimizer.Optimum optimum = solver.solve();
                        // the answer
                        double[] position = optimum.getPoint().toArray();
                        System.out.println("new position " + position[0] + "," + position[1]);
                        scannerListener.updatePosition(position[0], position[1]);
                        flag = 0;
                    } else
                        System.out.println("no new position");
                }
            }
        }
    };

    public class ScannerBinder extends Binder {
        public DeviceScanner getScanner() {
            return DeviceScanner.this;
        }
    }

    private ScannerListener scannerListener;

    public interface ScannerListener {
        void updateScanResult(final int rssi, final int idx);

        void updatePosition(final double x, final double y);
    }

    public void setScannerListener(ScannerListener listener) {
        scannerListener = listener;
    }

    @Override
    public IBinder onBind(Intent intent) {
        scanLeDevice(true);
        return new ScannerBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        bluetoothScanner.stopScan(scanCallback);
        System.out.println(database.getRSSI());
        return true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        bluetoothScanner = bluetoothAdapter.getBluetoothLeScanner();
        handler = new Handler();
        scanSettings = new ScanSettings.Builder().build();
        scanFilters = new ArrayList<>();
        database = new Database(getApplicationContext());
//        for (String uuid : DEVICE_UUIDS)
//            scanFilters.add(new ScanFilter.Builder().setServiceUuid(new ParcelUuid(UUID.fromString(uuid))).build());
        for (String addr : MainActivity.getDeviceAddrs())
            scanFilters.add(new ScanFilter.Builder().setDeviceAddress(addr).build());
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            handler.postDelayed(new Runnable() {
                @SuppressLint("NewApi")
                @Override
                public void run() {
                    //mScanning = false;
                    bluetoothScanner.startScan(scanFilters, scanSettings, scanCallback);
                }
            }, SCAN_PERIOD);

            //mScanning = true;
            bluetoothScanner.startScan(scanFilters, scanSettings, scanCallback);
        } else {
            //mScanning = false;
            bluetoothScanner.stopScan(scanCallback);
        }
    }

    private int getDeviceIndex(ScanResult result) {
        //String uuid = ScanRecordParser.parseUUID(result.getScanRecord().getBytes());
        //return Arrays.asList(DEVICE_UUIDS).indexOf(uuid);
        String addr = result.getDevice().getAddress();
        return Arrays.asList(MainActivity.getDeviceAddrs()).indexOf(addr);
    }

    public Database getDatabase() {
        return database;
    }

    public void setDataCollectingMode(boolean dataCollectingMode) {
        this.dataCollectingMode = dataCollectingMode;
    }
}
