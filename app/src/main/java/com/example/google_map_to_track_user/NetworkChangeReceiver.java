package com.example.google_map_to_track_user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class NetworkChangeReceiver extends BroadcastReceiver {
    private static AlertDialog dialog;
    @Override
    public void onReceive(final Context context, final Intent intent) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo == null) {
            // No internet connection
            Toast.makeText(context, "No internet connection \n please turn on your mobile data", Toast.LENGTH_SHORT).show();
        }
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // GPS is enabled
            //Toast.makeText(context, "Please enable GPS", Toast.LENGTH_LONG).show();

            alertDialog.setTitle("GPS Disabled");
            alertDialog.setMessage("Please enable GPS to continue");
            alertDialog.setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    dialog.cancel();
                }
            });
            dialog = alertDialog.create();
            dialog.show();
            try {

            }catch (WindowManager.BadTokenException ex){
                ex.printStackTrace();
                //dialog.show();
                Log.d("WindowManager", "onReceive: "+ex.toString());
            }

            dialog.setCancelable(false);
        }else {
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }
    public static class LocationProvidersChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("alertdialog", "onReceive: ");
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }


}
