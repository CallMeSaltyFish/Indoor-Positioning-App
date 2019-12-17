package com.example.indoorapplication;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.RequiresApi;

import java.util.*;

@SuppressLint("NewApi")
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class DeviceScanActivity extends ListActivity {

    private final UUID[] DEVICE_UUIDS = {UUID.fromString("01122334-4556-6778-899a-abbccddeeff0"),
                                         UUID.fromString("5e9917bd-f3ac-41e6-8226-3fd79f340dc5") };
    private BluetoothAdapter bluetoothAdapter;
    private boolean mScanning;
    private Handler handler;
    //private LeDeviceListAdapter leDeviceListAdapter;
    private BluetoothAdapter.LeScanCallback leScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("==================scan=========");
                            System.out.println(device);
//                            leDeviceListAdapter.addDevice(device);
//                            leDeviceListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            };

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 100000;

    public DeviceScanActivity(BluetoothAdapter btAdapter) {
        bluetoothAdapter = btAdapter;
        handler = new Handler();
        scanLeDevice(true);
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            handler.postDelayed(new Runnable() {
                @SuppressLint("NewApi")
                @Override
                public void run() {
                    mScanning = false;
                    bluetoothAdapter.stopLeScan(leScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            bluetoothAdapter.startLeScan(leScanCallback);
        } else {
            mScanning = false;
            bluetoothAdapter.stopLeScan(leScanCallback);
        }
    }
}
