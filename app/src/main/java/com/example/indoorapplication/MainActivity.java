package com.example.indoorapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.*;

@SuppressLint("NewApi")
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MainActivity extends AppCompatActivity {
    private static final String[] DEFAULT_DEVICE_ADDRS = {"F9:C2:6E:7D:8A:7F", "C4:CE:DA:A2:25:61", "C4:CE:DA:A2:25:62"};
    private static final Double[][] DEFAULT_DEVICE_POSITIONS = {{0.0, 0.0}, {1.0, 0.0}, {0.0, 1.0}};

    private static String[] deviceAddrs;
    private static Double[][] devicePositions;

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

        deviceAddrs = Arrays.copyOf(DEFAULT_DEVICE_ADDRS, DEFAULT_DEVICE_ADDRS.length);
        devicePositions = Arrays.copyOf(DEFAULT_DEVICE_POSITIONS, DEFAULT_DEVICE_POSITIONS.length);

        //statFragment =(DashboardFragment) getSupportFragmentManager().f
        //rssiTextView = (TextView) findViewById(R.id.device_rssi);

        if (!((BluetoothManager) getSystemService(BLUETOOTH_SERVICE)).getAdapter().isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, Activity.RESULT_OK);
        }


        //startScan();
    }

    //这里是在登录界面label上右上角添加三个点，里面可添加其他功能
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);//这里是调用menu文件夹中的main.xml，在登陆界面label右上角的三角里显示其他功能
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                break;
            default:
                break;
        }

        return true;
    }

    public static void setDeviceAddr(int idx, String addr) {
        deviceAddrs[idx] = addr;
    }

    public static void setDevicePosition(int idx, double x, double y) {
        devicePositions[idx] = new Double[]{x, y};
    }

    public static String[] getDeviceAddrs(){
        return deviceAddrs;
    }

    public static String getDeviceAddr(int idx) {
        return deviceAddrs[idx];
    }

    public static Double[] getDevicePosition(int idx) {
        return devicePositions[idx];
    }

    public static String getDevicePositionString(int idx) {
        return "X: " + devicePositions[idx][0] + " Y: " + devicePositions[idx][1];
    }

}
