package com.example.google_map_to_track_user;

import static android.Manifest.permission.ACCESS_BACKGROUND_LOCATION;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.security.Provider;
import java.util.ArrayList;

public class MyServices extends Service {
    static  Get_LatLong get_latLong;
    Intent intent = new Intent();
    boolean check = true;
    static Context context;
    static boolean isstarted = false;
    int PERMISSION_ID = 125;
    int requestcode;

    int count=0;
    private ArrayList<LatLagDO> arrlistpost = new ArrayList<>();
    FusedLocationProviderClient mFusedLocationClient;
    DBhelper DB;
    long time;
    Thread thread;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        check = true;

        DB = new DBhelper(context);


        time = DB.getSettings();
        Log.d("DB.getSettings", "onStartCommand: "+time);


        if (!isstarted) {
            isstarted = true;
            thread = new Thread(new Runnable() {
                @Override
                public void run() {


                    requestNewLocationData();


                }
            });
            thread.start();
        }



        //======================================================
        StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(gfgPolicy);
        Thread gfgThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (check) {

                    Cursor cursor = DB.PostDatalist();

                    if (cursor.equals(null)||cursor.getCount()==0) {

                    } else  {
                        while (cursor.moveToNext()) {
                            arrlistpost.add(new LatLagDO(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getDouble(4),
                                    cursor.getDouble(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10),
                                    cursor.getString(11), cursor.getInt(12)));
                        }
                        Log.d("arrlistpost", "onCreate: " + arrlistpost.size());
                        try {
                            BuildXMLRequest buildXMLRequest = new BuildXMLRequest();
                            requestcode = buildXMLRequest.HttpSendData(arrlistpost);



                            if (requestcode == 200 || requestcode ==409) {

                                Log.i("push push", "run: button push" + requestcode + "rajap" + Thread.currentThread());
                                DB.PostedDatatoserver(arrlistpost);
                            } else {
                                Log.i("push push", "run: button push" + requestcode + "elsse" + Thread.currentThread());

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        gfgThread.start();

        Log.i("push push", "run: button push" + Thread.currentThread());
        return START_STICKY;

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        check = false;
        Log.d("abcdefgh", "onDestroy: ");
        //thread.suspend();
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        stopForeground(true);
        stopSelf();

    }


    public void requestNewLocationData() {


        LocationRequest.Builder builder = new LocationRequest.Builder(5000);
        builder.setPriority(Priority.PRIORITY_HIGH_ACCURACY);

        LocationRequest mLocationRequest = builder.build();


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper());

    }

    public LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {

            Location mLastLocation = locationResult.getLastLocation();

            DB.insertData(Double.parseDouble(mLastLocation.getLatitude() + ""), Double.parseDouble(mLastLocation.getLongitude() + ""));

        }

    };

}
