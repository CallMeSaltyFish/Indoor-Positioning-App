package com.example.indoorapplication;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import androidx.annotation.RequiresApi;
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
            map.put("name", getString(R.string.device)+idx);
            map.put("addr", MainActivity.getDeviceAddr(idx));
            map.put("position", MainActivity.getDevicePositionString(idx));
            listItem.add(map);
        }
        SimpleAdapter deviceViewAdapter = new SimpleAdapter(this, listItem, R.layout.device_view_item,
                new String[]{"name", "addr", "position"},
                new int[]{R.id.device_view_item_name, R.id.device_view_item_addr, R.id.device_view_item_position});
        deviceView.setAdapter(deviceViewAdapter);
        //deviceView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, Arrays.asList("1","2","3","4")));
    }
}
