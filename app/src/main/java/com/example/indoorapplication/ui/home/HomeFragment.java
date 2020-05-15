package com.example.indoorapplication.ui.home;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.example.indoorapplication.DeviceScanner;
import com.example.indoorapplication.R;
import indoormaps.ninepatch.com.library.IndoorMapsView;
import indoormaps.ninepatch.com.library.Marker;
import indoormaps.ninepatch.com.library.Style;
import indoormaps.ninepatch.com.library.callback.OnMapViewInizializate;
import indoormaps.ninepatch.com.library.zoom.ZOOM;

import static android.content.Context.BIND_AUTO_CREATE;


@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class HomeFragment extends Fragment {
    private Marker marker;
    private boolean isActive;
    private ImageButton refreshButton;
    private IndoorMapsView indoorMapsView;
    // private MapLayout mapLayout;
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
                public void updatePosition(final double x, final double y) {
                    if (getActivity() == null || !isActive)
                        return;
                    Handler handler = new Handler();
                    getView().post(new Runnable() {
                        @Override
                        public void run() {
                            displayPosition(x, y);
                        }
                    });
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            displayPosition(0.1,0.4);//displayPosition(x, y);
//                            //Integer distance = Integer.parseInt(distanceEditText.getText().toString());
//                            //scanner.getDatabase().add(distance, rssi);
//                            //rssiTextView.setText("Device: " + idx + " RSSI: " + rssi);
//                        }
//                    });
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
        indoorMapsView = (IndoorMapsView) root.findViewById(R.id.map_view);
        indoorMapsView.getIndoorViewListener().setOnMapViewInizializate(new OnMapViewInizializate() {
            @Override
            public void onMapLoading() {

            }

            @Override
            public void onMapinizializate() {
                indoorMapsView.init("indoormap.jpg", ZOOM.LEVEL4); //image from assets or put link
                indoorMapsView.setBackgroundColorRes(R.color.colorPrimary);

            }
        });

//        mapLayout = root.findViewById(R.id.map_layout);
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.zxc);
//        mapLayout.setImgBg(bitmap.getWidth(), bitmap.getHeight(), R.drawable.zxc);
        //listenForUpdatingPosition();
        refreshButton = root.findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("refresh");
                stopScan();
                startScan();
            }
        });
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

    private void displayPosition(double x, double y) {
        Marker prevMarker = marker;
        marker = new Marker(getContext());
        //marker.setId(0);
        marker.setLat(x);
        marker.setLon(y);
        marker.setName("pos");
        marker.setImageLink("pointer.png");//from assets or put link
        //set Marker Style
        Style style = new Style(getContext());
        style.setLabelColor(R.color.colorAccent);
        style.setLabelPx(22);
        style.setShowLabel(true);
        marker.setStyle(style);
        if (prevMarker != null)
            indoorMapsView.removeMarker(prevMarker);
        indoorMapsView.addMarker(marker);
    }

//    private double[] getPosition(double[] distances) {
//        double[][] positions = new double[][]{{0, 0}, {0, 5}, {12, 0}};
//
//        NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(positions, distances), new LevenbergMarquardtOptimizer());
//        LeastSquaresOptimizer.Optimum optimum = solver.solve();
//
//        // the answer
//        double[] centroid = optimum.getPoint().toArray();
//
//        // error and geometry information; may throw SingularMatrixException depending the threshold argument provided
//        RealVector standardDeviation = optimum.getSigma(0);
//        RealMatrix covarianceMatrix = optimum.getCovariances(0);
//        return centroid;
//    }

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