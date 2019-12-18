package com.example.indoorapplication;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Build;
import android.os.Handler;
import android.os.ParcelUuid;

import androidx.annotation.RequiresApi;

import com.example.indoorapplication.util.ScanRecordParser;

import java.util.*;

@SuppressLint("NewApi")
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class DeviceScanActivity extends ListActivity {

    private final String[] DEVICE_UUIDS = {"0112233445566778899AABBCCDDEEFF0"};
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
            int idx = getDeviceIndex(result);
            if (idx != -1) {
                System.out.println("Device: " + idx + " RSSI: " + result.getRssi());
            }
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
//        for (String addr : DEVICE_ADDRS)
//            scanFilters.add(new ScanFilter.Builder().setDeviceAddress(addr).build());
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
                    bluetoothScanner.startScan(scanCallback);
                }
            }, SCAN_PERIOD);

            //mScanning = true;
            bluetoothScanner.startScan(scanCallback);
        } else {
            // mScanning = false;
            bluetoothScanner.stopScan(scanCallback);
        }
    }

    private int getDeviceIndex(ScanResult result) {
        String uuid = ScanRecordParser.parseUUID(result.getScanRecord().getBytes());
        return Arrays.asList(DEVICE_UUIDS).indexOf(uuid);
    }
}
