package com.example.indoorapplication.util;

import android.content.Context;

import java.util.ArrayList;

public class Regression {
    public static Double[] regressionCoefficient = {0.0, 0.0, 0.0, 0.0};
    private static Context context;
    private static Database database;

    public static void initialize(final Context applicationContext) {
        context = applicationContext;
        database = new Database(context);
        ArrayList<Double> list = database.getRegressionCoefficient();
        if (!list.isEmpty())
            regressionCoefficient = list.toArray(regressionCoefficient);
    }

    public static void updateCoeffient(Double[] list) {
        database.addRegressionCoefficient(list);
    }

    public static double calculateDistance(int rssi) {
        double distance = 0, x = 1;
        for (int i = 0; i < 4; ++i) {
            distance += (regressionCoefficient[3 - i] * x);
            x *= rssi;
        }
        return distance;
    }
}
