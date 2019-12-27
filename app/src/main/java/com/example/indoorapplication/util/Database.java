package com.example.indoorapplication.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class Database {

    public final static String RSSI_TABLE_NAME = "RSSI";
    public final static Integer VERSION = 1;
    private SQLiteDatabase database;
    private SQLiteOpenHelper databaseHelper;

    public Database(final Context context) {
        databaseHelper = new SQLiteOpenHelper(context, RSSI_TABLE_NAME, null, VERSION) {

            @Override
            public void onCreate(SQLiteDatabase db) {
                // 创建数据库1张表
                // 通过execSQL（）执行SQL语句（此处创建了1个名为person的表
                String sql = "CREATE TABLE " + RSSI_TABLE_NAME + "(distance integer ,RSSI integer)";
                db.execSQL(sql);
                // 注：数据库实际上是没被创建 / 打开的（因该方法还没调用）
                // 直到getWritableDatabase() / getReadableDatabase() 第一次被调用时才会进行创建 / 打开
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                // 参数说明：
                // db ： 数据库
                // oldVersion ： 旧版本数据库
                // newVersion ： 新版本数据库

                // 使用 SQL的ALTER语句
//        String sql = "alter table person add sex varchar(8)";
//        db.execSQL(sql);
            }
        };
        database = databaseHelper.getWritableDatabase();
    }

    public void add(int distance, int rssi) {
        ContentValues pair = new ContentValues();
        pair.put("distance", distance);
        pair.put("RSSI", rssi);
        database.insert(RSSI_TABLE_NAME, null, pair);
        //database.execSQL("INSERT INTO " + RSSI_TABLE_NAME + " (distance, RSSI) VALUES (" + distance + ", " + rssi + ");");
    }

    public HashMap<Integer, ArrayList<Integer>> get() {
        HashMap<Integer, ArrayList<Integer>> map = new HashMap<>();
        Cursor result = database.rawQuery("SELECT * FROM " + RSSI_TABLE_NAME, null);
        result.moveToFirst();
        while (!result.isAfterLast()) {
            Integer distance = result.getInt(0), rssi = result.getInt(1);
            if (!map.containsKey(distance))
                map.put(distance, new ArrayList<Integer>());
            map.get(distance).add(rssi);
            // do something useful with these
            result.moveToNext();
        }
        result.close();
        return map;
    }
}
