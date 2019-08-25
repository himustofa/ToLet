package com.zaaibo.tolet.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.zaaibo.tolet.R;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class Utility {

    private static final String TAG = "Utility";
    private static boolean traffic = true;

    //===============================================| Get Address
    //https://stackoverflow.com/questions/9409195/how-to-get-complete-address-from-latitude-and-longitude
    public static String getAddress(Context context, LatLng latLng) {
        String address = null;
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                address = addressList.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    //====================================================| TrafficEnabled Selector
    public static void trafficEnabledButton(GoogleMap mMap) {
        if(traffic){
            mMap.setTrafficEnabled(true);
            traffic = false;
        } else {
            mMap.setTrafficEnabled(false);
            traffic = true;
        }
    }

    //===============================================| Pass a JSON style object to your map
    public static void mapsStyle(Context context, GoogleMap googleMap, int res) {
        try {
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, res));
            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
    }

    //===============================================| Add Marker
    public static Marker addMarkerPosition (GoogleMap mMap, Marker originMarker, double latitude, double longitude, String locality, Drawable drawable){
        if (originMarker != null) {
            originMarker.remove();
        }
        MarkerOptions options = new MarkerOptions();
        options.position(new LatLng(latitude,longitude));
        options.draggable(true);
        options.title(locality);
        //options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        //options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_origin)); //PNG Icon
        options.icon(getMarkerIconFromDrawable(drawable)); //PNG Icon
        return mMap.addMarker(options);
    }

    //===============================================| Move Map Camera
    public static void goToLocation(GoogleMap mMap, double latitude, double longitude, int zoom) {
        LatLng latLng = new LatLng(latitude,longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
    }

    public static Marker moveToCurrentLocation(Context context, GoogleMap mMap, Marker currentLocationMarker, LatLng latLng, Drawable drawable) {
        if (currentLocationMarker != null) {
            currentLocationMarker.remove();
        }
        MarkerOptions options = new MarkerOptions().position(latLng).draggable(true).title(getAddress(context, latLng)).icon(getMarkerIconFromDrawable(drawable));
        currentLocationMarker = mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
        mMap.animateCamera(CameraUpdateFactory.zoomIn()); // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null); // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        return currentLocationMarker;
    }

    public static void moveToLocation(GoogleMap mMap, LatLng latLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
        mMap.animateCamera(CameraUpdateFactory.zoomIn()); // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null); // Zoom out to zoom level 10, animating with a duration of 2 seconds.
    }

    //===============================================| Marker Icon from XML
    public static BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    //===============================================| Change position of current location icon
    public static void changeCurrentLocationIcon(SupportMapFragment mapFragment) {
        if (mapFragment != null) {
            ImageView btnMyLocation = (ImageView) ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            btnMyLocation.setImageResource(R.drawable.selector_my_location);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) btnMyLocation.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            //layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 30, 30);
            btnMyLocation.setLayoutParams(layoutParams);
        }
    }

    //====================================================| Double Value Round
    public static double getRound(double value) {
        DecimalFormat df = new DecimalFormat("#.00000");
        return Double.parseDouble(df.format(value));
    }

    //====================================================| Remove first character zero from phone number
    public static String removeZero(String str) {
        //int n = Character.getNumericValue(str.charAt(0)
        if(Character.getNumericValue(str.charAt(0)) == 0) {
            return str.substring(1); //remove first character
        } else {
            return str;
        }
    }

    //====================================================| Get Time from Timestamp
    public static String getTimeFromTimestamp(String input) {
        java.sql.Timestamp ts = java.sql.Timestamp.valueOf(input);
        return String.valueOf(java.text.DateFormat.getTimeInstance().format(ts.getTime())); //java.text.DateFormat.getDateTimeInstance().format(ts.getTime())
    }

    public static String getDateFromTimestamp(String input) {
        java.sql.Timestamp ts = java.sql.Timestamp.valueOf(input);
        return String.valueOf(new java.text.SimpleDateFormat("dd-MM-yyyy").format(ts)); //java.text.DateFormat.getDateTimeInstance().format(ts.getTime())
    }

    //====================================================| Round a double to 2 decimal
    public static double roundTwoDecimal(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    //====================================================| BottomNavigationView - How to get selected menu item?
    public static String getSelectedItem(BottomNavigationView bottomNavigationView) {
        Menu menu = bottomNavigationView.getMenu();
        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            if (menuItem.isChecked()) {
                //return menuItem.getItemId();
                return String.valueOf(menuItem.getTitle());
            }
        }
        return null;
    }

    //====================================================| MarkerDrag Listener
    public static void markerDragListener(final MarkerDragCallback myCallback, final Context context, GoogleMap mMap) {
        if (mMap != null) {
            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {
                }
                @Override
                public void onMarkerDrag(Marker marker) {
                }
                @Override
                public void onMarkerDragEnd(Marker marker) {
                    LatLng latLng = marker.getPosition();
                    myCallback.onCallback(latLng);
                }
            });
        }
    }

    public interface MarkerDragCallback {
        void onCallback(LatLng latLng);
    }

    //====================================================| Close/hide the Android Soft Keyboard
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //====================================================| Alert Dialog
    public static void alertDialog(final Context context, String msg) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.alert_title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(R.string.alert_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                }).show();
    }

    //====================================================| Alert Dialog About
    public static void aboutMe(Context context) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.about_title)
                .setMessage(context.getString(R.string.apps_details))
                .setPositiveButton(R.string.alert_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                }).show();
    }



    //===============================================| Check if this device has a camera
    public static boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            return true;
        } else {
            return false;
        }
    }

    //===============================================| To ask request permission dialog
    public static boolean askForPermission(Context context, String permission, Integer requestCode) {
        boolean result = false;
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            //if there is no permission allowed then, display permission request dialog
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, requestCode);
            }
        } else {
            //Permission is already granted.
            result = true;
        }
        return result;
    }


    //===============================================| ProgressDialog
    public static ProgressDialog showProgressDialog(Context mActivity, final String message, boolean isCancelable) {
        ProgressDialog mProgress = new ProgressDialog(mActivity);
        mProgress.show();
        mProgress.setCancelable(isCancelable); //setCancelable(false); = invisible clicking the outside
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.setMessage(message);
        return mProgress;
    }

    public static void dismissProgressDialog(ProgressDialog mProgress) {
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

}
