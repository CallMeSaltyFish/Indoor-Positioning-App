package com.example.indoorapplication;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
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
    private ArrayList<HashMap<String, Object>> deviceInfoItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        deviceView = findViewById(R.id.device_view);
        //定义一个HashMap构成的列表以键值对的方式存放数据
        deviceInfoItems = new ArrayList<>();
        //循环填充数据
        for (int idx = 0; idx < 3; ++idx) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", getString(R.string.device) + idx);
            map.put("addr", MainActivity.getDeviceAddr(idx));
            map.put("position", MainActivity.getDevicePositionString(idx));
            deviceInfoItems.add(map);
        }
        SimpleAdapter deviceViewAdapter = new SimpleAdapter(this, deviceInfoItems, R.layout.view_device_item,
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

    private void showDeviceSettingDialog(final int idx) {
        AlertDialog.Builder deviceSettingDialog = new AlertDialog.Builder(this);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_device_setting, null);
        deviceSettingDialog.setView(dialogView);
        deviceSettingDialog.setTitle(getString(R.string.settings) + ' ' + getString(R.string.device) + idx);
        final EditText deviceAddrEditText = dialogView.findViewById(R.id.dialog_device_setting_addr_edit_text),
                devicePositionXEditText = dialogView.findViewById(R.id.dialog_device_setting_position_x_edit_text),
                devicePositionYEditText = dialogView.findViewById(R.id.dialog_device_setting_position_y_edit_text);
        deviceAddrEditText.setText(MainActivity.getDeviceAddr(idx));
        devicePositionXEditText.setText(MainActivity.getDevicePosition(idx)[0].toString());
        devicePositionYEditText.setText(MainActivity.getDevicePosition(idx)[1].toString());
        deviceSettingDialog.setPositiveButton(getString(R.string.confirm_settings),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String addr = deviceAddrEditText.getText().toString();
                        double x = Double.parseDouble(devicePositionXEditText.getText().toString());
                        double y = Double.parseDouble(devicePositionYEditText.getText().toString());
                        updateDeviceInfo(idx, addr, x, y);
                    }
                });
        deviceSettingDialog.show();
    }

    private void updateDeviceInfo(int idx, String addr, double x, double y) {
        View deviceItemView = deviceView.getChildAt(idx);
        ((TextView) deviceItemView.findViewById(R.id.device_view_item_addr)).setText(addr);
        ((TextView) deviceItemView.findViewById(R.id.device_view_item_position)).setText("X: " + x + " Y: " + y);
        MainActivity.setDeviceAddr(idx, addr);
        MainActivity.setDevicePosition(idx, x, y);
    }
}
