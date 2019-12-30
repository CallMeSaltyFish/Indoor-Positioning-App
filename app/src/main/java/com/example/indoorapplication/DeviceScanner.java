package com.example.indoorapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import androidx.annotation.RequiresApi;

import com.example.indoorapplication.util.Database;
import com.example.indoorapplication.util.ScanRecordParser;

import java.util.*;

@SuppressLint("NewApi")
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class DeviceScanner extends Service {

    private static final String[] DEVICE_UUIDS = {"0112233445566778899AABBCCDDEEFF0"};
    private static final String[] DEVICE_ADDRS = {"F9:C2:6E:7D:8A:7F", "C4:CE:DA:A2:25:61"};
    // Stops scanning after given seconds.
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
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            int idx = getDeviceIndex(result);
            if (idx != -1) {
                System.out.println("Device: " + idx + " RSSI: " + result.getRssi());
                scannerListener.updateScanResult(result.getRssi(), idx);
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
        System.out.println(database.get());
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
        for (String addr : DEVICE_ADDRS)
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
                    bluetoothScanner.startScan(scanFilters,scanSettings,scanCallback);
                }
            }, SCAN_PERIOD);

            //mScanning = true;
            bluetoothScanner.startScan(scanFilters,scanSettings,scanCallback);
        } else {
            //mScanning = false;
            bluetoothScanner.stopScan(scanCallback);
        }
    }

    private int getDeviceIndex(ScanResult result) {
        //String uuid = ScanRecordParser.parseUUID(result.getScanRecord().getBytes());
        //return Arrays.asList(DEVICE_UUIDS).indexOf(uuid);
        String addr = result.getDevice().getAddress();
        return Arrays.asList(DEVICE_ADDRS).indexOf(addr);
    }

    public Database getDatabase(){
        return database;
    }

}
