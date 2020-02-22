package com.example.indoorapplication.ui.home;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.indoorapplication.DeviceScanner;
import com.example.indoorapplication.MapImageView;
import com.example.indoorapplication.MapLayout;
import com.example.indoorapplication.R;
import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import static android.content.Context.BIND_AUTO_CREATE;

import java.util.*;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class HomeFragment extends Fragment {

    private boolean isActive;
    private HomeViewModel homeViewModel;
    private DeviceScanner scanner;
    private ServiceConnection scannerConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            scanner = ((DeviceScanner.ScannerBinder) iBinder).getScanner();
            scanner.setDataCollectingMode(false);
            scanner.setScannerListener(new DeviceScanner.ScannerListener() {
                @Override
                public void updateScanResult(final int rssi, final int idx) {
                }

                @Override
                public void updatePosition(final int x, final int y) {
                    if (getActivity() == null || !isActive)
                        return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            displayPosition(x, y);
                            //Integer distance = Integer.parseInt(distanceEditText.getText().toString());
                            //scanner.getDatabase().add(distance, rssi);
                            //rssiTextView.setText("Device: " + idx + " RSSI: " + rssi);
                        }
                    });
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //((ImageView) root.findViewById(R.id.map_view)).setImageResource(R.drawable.zxc);
        MapLayout mapLayout = root.findViewById(R.id.map_layout);
        Bitmap bitmap = BitmapFactory.decodeResource(root.getResources(),R.drawable.zxc);
        mapLayout.setImgBg(bitmap.getWidth(),bitmap.getHeight(), R.drawable.zxc);
        //listenForUpdatingPosition();
        //getPosition();
        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        isActive = false;
        stopScan();
        //rssiChart.eraseChart();
        //getActivity().unbindService(scannerConn);
    }

    @Override
    public void onResume() {
        super.onResume();
        startScan();
        isActive = true;
    }

//    private void listenForUpdatingPosition() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    if (flag == 7) {
//                        //getPosition();
//                        System.out.println("new position");
//                        flag = 0;
//                    } else
//                        System.out.println("no new position");
//                }
//            }
//        }, 1000);
//    }

    private void displayPosition(int x, int y) {
        System.out.println("=====" + x + "," + y + "=============");
    }

    private void getPosition() {
        double[][] positions = new double[][]{{5.0, -6.0}, {13.0, -15.0}, {21.0, -3.0}, {12.4, -21.2}};
        double[] distances = new double[]{8.06, 13.97, 23.32, 15.31};

        NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(positions, distances), new LevenbergMarquardtOptimizer());
        LeastSquaresOptimizer.Optimum optimum = solver.solve();

        // the answer
        double[] centroid = optimum.getPoint().toArray();

        // error and geometry information; may throw SingularMatrixException depending the threshold argument provided
        RealVector standardDeviation = optimum.getSigma(0);
        RealMatrix covarianceMatrix = optimum.getCovariances(0);

        System.out.println("===================================================");
        for (double d : centroid)
            System.out.println(d);
        System.out.println("===================================================");
    }

    private void startScan() {
        isActive = true;
        Intent startIntent = new Intent(getActivity(), DeviceScanner.class);
        getActivity().bindService(startIntent, scannerConn, BIND_AUTO_CREATE);
    }

    private void stopScan() {
        isActive = false;
        getActivity().unbindService(scannerConn);
    }
}