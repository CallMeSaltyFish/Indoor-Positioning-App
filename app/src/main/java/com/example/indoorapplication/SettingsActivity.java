package com.example.indoorapplication;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.*;

@SuppressLint("NewApi")
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class SettingsActivity extends AppCompatActivity {
    private ListView deviceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        deviceView = findViewById(R.id.device_view);
        //定义一个HashMap构成的列表以键值对的方式存放数据
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        //循环填充数据
        for (int idx = 0; idx < 3; ++idx) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("name", getString(R.string.device) + idx);
            map.put("addr", MainActivity.getDeviceAddr(idx));
            map.put("position", MainActivity.getDevicePositionString(idx));
            listItem.add(map);
        }
        SimpleAdapter deviceViewAdapter = new SimpleAdapter(this, listItem, R.layout.view_device_item,
                new String[]{"name", "addr", "position"},
                new int[]{R.id.device_view_item_name, R.id.device_view_item_addr, R.id.device_view_item_position});
        deviceView.setAdapter(deviceViewAdapter);
        deviceView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDeviceSettingDialog(position);
            }
        });
        //deviceView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, Arrays.asList("1","2","3","4")));
    }

    private void showDeviceSettingDialog(int idx) {
        AlertDialog.Builder deviceSettingDialog = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_device_setting, null);
        deviceSettingDialog.setView(dialogView);
        deviceSettingDialog.setTitle("ffffff");
        ((TextView) dialogView.findViewById(R.id.dialog_device_setting_name)).setText(R.string.device + idx);
        ((TextView) dialogView.findViewById(R.id.dialog_device_setting_addr)).setText(MainActivity.getDeviceAddr(idx));
        ((TextView) dialogView.findViewById(R.id.dialog_device_setting_position)).setText(MainActivity.getDevicePositionString(idx));
        deviceSettingDialog.show();
    }
}
