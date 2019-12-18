package com.example.indoorapplication;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Build;
import android.os.Handler;
import android.os.ParcelUuid;

import androidx.annotation.RequiresApi;

import java.util.*;

@SuppressLint("NewApi")
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class DeviceScanActivity extends ListActivity {

    private final String[] DEVICE_UUIDS = {"b8eb94fd-3679-3527-a762-e685a7b603ac"};
    private final String[] DEVICE_ADDRS = {"F9:C2:6E:7D:8A:7F"};
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bluetoothScanner;
    //private boolean mScanning;
    private Handler handler;
    private List<ScanFilter> scanFilters;
    private ScanSettings scanSettings;
    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            System.out.println("============scan=============");
            System.out.println(getUUID(result));
            System.out.println(result.getDevice().getAddress());
        }
    };

    // Stops scanning after given seconds.
    private static final long SCAN_PERIOD = 100000;

    public DeviceScanActivity(BluetoothAdapter adapter) {
        bluetoothAdapter = adapter;
        bluetoothScanner = bluetoothAdapter.getBluetoothLeScanner();
        handler = new Handler();
        scanSettings = new ScanSettings.Builder().build();
        scanFilters = new ArrayList<>();
//        for (String uuid : DEVICE_UUIDS)
//            scanFilters.add(new ScanFilter.Builder().setServiceUuid(new ParcelUuid(UUID.fromString(uuid))).build());
        for (String addr : DEVICE_ADDRS)
            scanFilters.add(new ScanFilter.Builder().setDeviceAddress(addr).build());
        scanLeDevice(true);
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            handler.postDelayed(new Runnable() {
                @SuppressLint("NewApi")
                @Override
                public void run() {
                    // mScanning = false;
                    bluetoothScanner.startScan(scanFilters, scanSettings, scanCallback);
                    //bluetoothScanner.startScan(scanCallback);
                }
            }, SCAN_PERIOD);

            //mScanning = true;
            //bluetoothScanner.startScan(scanCallback);
            bluetoothScanner.startScan(scanFilters, scanSettings, scanCallback);
        } else {
            // mScanning = false;
            bluetoothScanner.stopScan(scanCallback);
        }
    }

    public String getUUID(ScanResult result) {
        String UUIDx = UUID
                .nameUUIDFromBytes(result.getScanRecord().getBytes()).toString();
        return UUIDx;
    }
}
