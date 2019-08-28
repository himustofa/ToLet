package com.zaaibo.tolet.utils.network;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Network {

    private static final String TAG = "Network";

    //===============================================| Check Internet Connection
    public static boolean haveNetwork(final Activity activity) {
        boolean have_Wifi = false;
        boolean have_MobileData = false;

        ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] infos = manager.getAllNetworkInfo();
        for (NetworkInfo info : infos) {
            if (info.getTypeName().equalsIgnoreCase("WIFI")) {
                if (info.isConnected()) {
                    have_Wifi = true;
                }
            }
            if (info.getTypeName().equalsIgnoreCase("MOBILE")) {
                if (info.isConnected()) {
                    have_MobileData = true;
                }
            }
        }
        return have_Wifi | have_MobileData;
    }

    //===============================================| Check Location Connection
    public static boolean isEnabledLocation(final Activity activity) {
        boolean gpsEnabled = false;
        boolean networkEnabled = false;
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        try {
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*if(!gpsEnabled && !networkEnabled) {
            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivity(myIntent);
        }*/
        return (!gpsEnabled && !networkEnabled) ? false : true;
    }

}
