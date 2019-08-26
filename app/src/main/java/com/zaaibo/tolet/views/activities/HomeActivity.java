package com.zaaibo.tolet.views.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;
import com.zaaibo.tolet.R;
import com.zaaibo.tolet.models.Filter;
import com.zaaibo.tolet.models.PostAd;
import com.zaaibo.tolet.models.User;
import com.zaaibo.tolet.services.MyLocationReceiver;
import com.zaaibo.tolet.services.MyNetworkReceiver;
import com.zaaibo.tolet.session.SharedPrefManager;
import com.zaaibo.tolet.utils.ConstantKey;
import com.zaaibo.tolet.utils.GpsUtility;
import com.zaaibo.tolet.utils.Utility;
import com.zaaibo.tolet.utils.language.LocaleHelper;
import com.zaaibo.tolet.viewmodels.PostAdViewModel;
import com.zaaibo.tolet.views.adapters.MarkerInfoAdapter;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.Fabric;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private static final String TAG = "HomeActivity";
    public static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 99;
    private int REQUEST_CHECK_SETTINGS = 0;

    private ActionBarDrawerToggle toggle;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private FusedLocationProviderClient mFusedLocationClient;

    private ArrayList<PostAd> mArrayList = new ArrayList<>();
    private MyNetworkReceiver mNetworkReceiver;
    private MyLocationReceiver mLocationReceiver;
    private LocationManager manager;
    private User mUser = null;

    //==========================================| Filter
    private Spinner filterLocation;
    private RangeSeekBar filterPriceRange;

    private Button[] propertyBtn = new Button[6];
    private Button propertyBtn_unfocused;
    private int[] propertyBtn_id = {R.id.propertyBtn0, R.id.propertyBtn1, R.id.propertyBtn2, R.id.propertyBtn3, R.id.propertyBtn4, R.id.propertyBtn5};

    private Button[] renterBtn = new Button[6];
    private Button renterBtn_unfocused;
    private int[] renterBtn_id = {R.id.renterBtn0, R.id.renterBtn1, R.id.renterBtn2, R.id.renterBtn3, R.id.renterBtn4, R.id.renterBtn5};

    private Button[] bedBtn = new Button[5];
    private Button bedBtn_unfocused;
    private int[] bedBtn_id = {R.id.bedBtn0, R.id.bedBtn1, R.id.bedBtn2, R.id.bedBtn3, R.id.bedBtn4};

    private Button filterBtn;
    private String propertyType;
    private String renterType;
    private String bedRooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_home);

        mNetworkReceiver = new MyNetworkReceiver(this);
        mLocationReceiver = new MyLocationReceiver(this);
        mUser = SharedPrefManager.getInstance(HomeActivity.this).getUser();

        //-----------------------------------------------| GPS/Location
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        checkGpsEnabled(manager);

        //==========================================| Receive the data and observe the data from view model
        PostAdViewModel mPostAdViewModel = ViewModelProviders.of(this).get(PostAdViewModel.class); //Initialize view model
        mPostAdViewModel.getAllPostAd().observe(this, new Observer<ArrayList<PostAd>>() {
            @Override
            public void onChanged(ArrayList<PostAd> postAds) {
                if (postAds != null) {
                    mArrayList.addAll(postAds);
                }
            }
        });

        //==========================================| findViewById
        ((Button) findViewById(R.id.list_id)).setOnClickListener(new ActionHandler());
        ((Button) findViewById(R.id.filter_id)).setOnClickListener(new ActionHandler());
        ((TextView) findViewById(R.id.log_out)).setOnLongClickListener(new ActionHandler());

        //====================================================| To Display Navigation Bar Icon and Back
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false); //Remove title
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            //toolbar.getBackground().setAlpha(200);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        if (mUser != null) {
            Picasso.get().load(mUser.getUserImageUrl()).into(((CircleImageView) hView.findViewById(R.id.nav_header_photo)));
            ((TextView) hView.findViewById(R.id.user_full_name)).setText(mUser.getUserFullName());
            ((TextView) hView.findViewById(R.id.user_email)).setText(mUser.getUserEmail());
        }

        //====================================| Google Maps
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    //===============================================| Language Change
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    //===============================================| onStart(), onPause(), onResume(), onStop()
    @Override
    protected void onResume() {
        super.onResume();
        try {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)); //After Oreo version this code must be used
            registerReceiver(mLocationReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));  //After Oreo version this code must be used
            LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver), new IntentFilter(ConstantKey.NOTIFICATION_BROADCAST_RECEIVER)); //After Oreo version this code must be used
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(mNetworkReceiver); //After Oreo version this code must be used
            unregisterReceiver(mLocationReceiver); //After Oreo version this code must be used
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver); //After Oreo version this code must be used
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //====================================================| Button events
    private class ActionHandler implements View.OnClickListener, View.OnLongClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.list_id) {
                Intent intent = new Intent(getApplicationContext(), PostsListActivity.class);
                startActivity(intent);
            }
            if (v.getId() == R.id.filter_id) {
                filterDialog();
            }
        }
        @Override
        public boolean onLongClick(View view) {
            signOut();
            return false;
        }
    }

    private void signOut() {
        new AlertDialog.Builder(HomeActivity.this)
                .setTitle(R.string.about_title)
                .setMessage(R.string.msg_sign_out)
                .setPositiveButton(R.string.sign_out, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        SharedPrefManager.getInstance(HomeActivity.this).deleteCurrentSession();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(HomeActivity.this, PhoneActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //For login to clear this screen for that did not back this screen
                        startActivity(intent);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.msg_neg, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    //====================================================| onBackPressed in Background and OptionsMenu
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        switch (item.getItemId()) {
            case R.id.standard:
                Utility.mapsStyle(HomeActivity.this, mMap, R.raw.style_standard);
                break;
            case R.id.silver:
                Utility.mapsStyle(HomeActivity.this, mMap, R.raw.style_silver);
                break;
            case R.id.retro:
                Utility.mapsStyle(HomeActivity.this, mMap, R.raw.style_retro);
                break;
            case R.id.dark:
                Utility.mapsStyle(HomeActivity.this, mMap, R.raw.style_dark);
                break;
            case R.id.night:
                Utility.mapsStyle(HomeActivity.this, mMap, R.raw.style_night);
                break;
            case R.id.aubergine:
                Utility.mapsStyle(HomeActivity.this, mMap, R.raw.style_aubergine);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.post_ad_id:
                startActivity(new Intent(HomeActivity.this, PostAdActivity.class));
                break;
            case R.id.notice_id:
                startActivity(new Intent(HomeActivity.this, NotificationActivity.class));
                break;
            case R.id.favorite_id:
                startActivity(new Intent(HomeActivity.this, FavoriteActivity.class));
                break;
            case R.id.profile_id:
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                break;
            case R.id.settings_id:
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                break;
            case R.id.about_id:
                startActivity(new Intent(HomeActivity.this, AboutActivity.class));
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //===============================================| onMapReady()
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //-------------------------------------------| Pass a JSON style object to your map
        Utility.mapsStyle(HomeActivity.this, mMap, R.raw.style_silver);
        //-------------------------------------------| If above the android version Marshmallow then call the location permission
        requestPermissions();

        /*mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);*/

        ((ToggleButton) findViewById(R.id.traffic_toggle_button)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (mMap != null)
                        mMap.setTrafficEnabled(true);
                } else {
                    if (mMap != null)
                        mMap.setTrafficEnabled(false);
                }
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (PostAd obj : mArrayList) {
                    LatLng latLng = new LatLng(Double.parseDouble(obj.getLatitude()), Double.parseDouble(obj.getLongitude()));
                    String[] arr = obj.getImageUrl().replaceAll("[\\[\\]]", "").split(",");
                    //mMap.addMarker(new MarkerOptions().position(latLng).title(obj.getAddress()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))); //Default Icon
                    //mMap.addMarker(new MarkerOptions().position(latLng).title(obj.getAddress())).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.admin)); //PNG Icon
                    //mMap.addMarker(new MarkerOptions().position(latLng).title(obj.getAddress()).icon(Utility.getMarkerIconFromDrawable( getApplicationContext().getResources().getDrawable(R.drawable.ic_icon_house) ))); //XML Icon

                    Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(obj.getAddress()).snippet(getResources().getString(R.string.app_name)).icon(Utility.getMarkerIconFromDrawable( getApplicationContext().getResources().getDrawable(R.drawable.ic_icon_house) ))); //XML Icon
                    //marker.showInfoWindow();
                    MarkerInfoAdapter mAdapter = new MarkerInfoAdapter(HomeActivity.this, arr, obj);
                    mMap.setInfoWindowAdapter(mAdapter);
                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            Toast.makeText(HomeActivity.this, ""+obj.getOwnerName(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }, 3000);

        //---------------------------------------------| MarkerDrag Listener for Origin
        /*Utility.markerDragListener(new Utility.MarkerDragCallback() {
            @Override
            public void onCallback(LatLng latLng) {
                if (latLng != null) {
                    origin = new LatLng(latLng.latitude, latLng.longitude);
                    currentLocationMarker = Utility.addMarkerPosition(mMap, currentLocationMarker, latLng.latitude, latLng.longitude,Utility.getAddress(HomeActivity.this, latLng), getResources().getDrawable(R.drawable.ic_marker_origin));
                }
            }
        }, HomeActivity.this, mMap);*/
        //---------------------------------------------| CameraMove Listener for Destination
        /*if (isSetLocationOnMap) {
            mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    LatLng latLng = mMap.getCameraPosition().target; //float mZoom = mMap.getCameraPosition().zoom; //double lat =  mMap.getCameraPosition().target.latitude; //double lng =  mMap.getCameraPosition().target.longitude;
                    destination = new LatLng(latLng.latitude, latLng.longitude);
                    if (isSetLocationOnMap) {searchEditText.setText(Utility.getAddress(HomeActivity.this, destination));}
                    if (searchButton.getVisibility() == View.GONE) {
                        searchButton.setVisibility(View.VISIBLE);
                    }
                }
            });
            *//*mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    LatLng latLng = mMap.getCameraPosition().target; //float mZoom = mMap.getCameraPosition().zoom; //double lat =  mMap.getCameraPosition().target.latitude; //double lng =  mMap.getCameraPosition().target.longitude;
                    destination = new LatLng(latLng.latitude, latLng.longitude);
                    if (isSetLocationOnMap) {searchEditText.setText(Utility.getAddress(HomeActivity.this, destination));}
                }
            });*//*
            mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
                @Override
                public void onCameraMoveStarted(int reason) {
                    if (reason ==REASON_GESTURE) {
                        if (searchButton.getVisibility() == View.VISIBLE) {
                            searchButton.setVisibility(View.GONE);
                        }
                    }
                }
            });
        }*/
    }

    //====================================| Explain why the app needs the request permissions
    //https://developers.google.com/maps/documentation/android-sdk/location
    private void requestPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_ACCESS_FINE_LOCATION); //if there is no permission allowed then, display permission request dialog
        } else {
            mMap.setMyLocationEnabled(true);
            Utility.changeCurrentLocationIcon(mapFragment);
            getDeviceLocation();
        }
    }

    //====================================| Handle the permissions request response
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSION_ACCESS_FINE_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //For allow button
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    Utility.changeCurrentLocationIcon(mapFragment);
                    getDeviceLocation();
                }
            } else {
                //For denied button
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                requestPermissions();
            }
        }
    }

    //===============================================| Get Device Location/LatLng
    private void getDeviceLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        LatLng origin = new LatLng(location.getLatitude(),location.getLongitude());
                        String mAddress = Utility.getAddress(HomeActivity.this, origin);
                        Utility.moveToLocation(mMap, new LatLng(origin.latitude, origin.longitude));
                        SharedPrefManager.getInstance(HomeActivity.this).saveCurrentLatLng(origin);
                    }
                }
            });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    //===============================================| GPS/Location
    private void checkGpsEnabled(LocationManager manager) {
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && GpsUtility.hasGPSDevice(this)) {
            GpsUtility.displayLocationSettingsRequest(new GpsUtility.GpsOnListenerCallBack() {
                @Override
                public void gpsResultCode(int resultCode) {
                    REQUEST_CHECK_SETTINGS = resultCode;
                }
            }, this);
        } else {
            //checkPermissions();
        }
    }

    //====================================================| BroadcastReceiver
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String title = intent.getExtras().getString("title");
            if (title.equals("Accepted")) {
                //
            }
        }
    };

    //====================================================| Filter Custom AlertDialog
    protected void filterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_post_filter, null);
        builder.setView(view);
        builder.setCancelable(true);
        builder.create();

        final AlertDialog dialog = builder.show();

        this.filterLocation = (Spinner) view.findViewById(R.id.filter_location);
        this.filterPriceRange = (RangeSeekBar) view.findViewById(R.id.filter_price_range);
        final EditText min = view.findViewById(R.id.range_min);
        final EditText max = view.findViewById(R.id.range_max);
        filterPriceRange.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                min.setText(minValue.toString());
                max.setText(maxValue.toString());
            }
        });

        //---------------------------------------------| Property Button
        for(int i = 0; i < propertyBtn.length; i++){
            propertyBtn[i] = (Button) view.findViewById(propertyBtn_id[i]);
            propertyBtn[i].setBackground(getResources().getDrawable(R.drawable.shape_button_border));
            propertyBtn[i].setOnClickListener(new ActionHandlerProperty());
        }
        propertyBtn_unfocused = propertyBtn[0];

        //---------------------------------------------| Renter Button
        for(int i = 0; i < renterBtn.length; i++){
            renterBtn[i] = (Button) view.findViewById(renterBtn_id[i]);
            renterBtn[i].setBackground(getResources().getDrawable(R.drawable.shape_button_border));
            renterBtn[i].setOnClickListener(new ActionHandlerRenter());
        }
        renterBtn_unfocused = renterBtn[0];

        //---------------------------------------------| Bedrooms Button
        for(int i = 0; i < bedBtn.length; i++){
            bedBtn[i] = (Button) view.findViewById(bedBtn_id[i]);
            bedBtn[i].setBackground(getResources().getDrawable(R.drawable.shape_button_border));
            bedBtn[i].setOnClickListener(new ActionHandlerBedrooms());
        }
        bedBtn_unfocused = bedBtn[0];

        //---------------------------------------------| Filter Button
        filterBtn = (Button) view.findViewById(R.id.filter_btn);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!filterLocation.getSelectedItem().toString().contains("Select Location")) {
                    if (bedRooms != null && bedRooms.equals("4+")) {
                        passFilterValues(filterLocation.getSelectedItem().toString(),min.getText().toString(),max.getText().toString(),propertyType,renterType,"Any");
                    } else {
                        passFilterValues(filterLocation.getSelectedItem().toString(),min.getText().toString(),max.getText().toString(),propertyType,renterType,bedRooms);
                    }
                } else {
                    Utility.alertDialog(HomeActivity.this, getResources().getString( R.string.msg_select_location));
                }
            }
        });

        ((TextView) view.findViewById(R.id.filter_dialog_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        ((TextView) view.findViewById(R.id.filter_dialog_reset)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "Reset", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //====================================================| Filter values pass into intent
    private void passFilterValues(String loc,String min,String max,String pro,String rent,String bed) {
        Intent intent = new Intent(getApplicationContext(), PostsListActivity.class);
        //intent.putExtra(ConstantKey.FILTER_KEY, loc+","+min+","+max+","+pro+","+rent+","+bed);
        intent.putExtra(ConstantKey.FILTER_KEY, new Filter(loc, min, max, pro, rent, bed));
        startActivity(intent);
    }

    //====================================================| Property Button Action
    private class ActionHandlerProperty implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.propertyBtn0 :
                    propertyType = propertyBtn[0].getText().toString();
                    setFocusProperty(propertyBtn_unfocused, propertyBtn[0]);
                    break;
                case R.id.propertyBtn1 :
                    propertyType = propertyBtn[1].getText().toString();
                    setFocusProperty(propertyBtn_unfocused, propertyBtn[1]);
                    break;
                case R.id.propertyBtn2 :
                    propertyType = propertyBtn[2].getText().toString();
                    setFocusProperty(propertyBtn_unfocused, propertyBtn[2]);
                    break;
                case R.id.propertyBtn3 :
                    propertyType = propertyBtn[3].getText().toString();
                    setFocusProperty(propertyBtn_unfocused, propertyBtn[3]);
                    break;
                case R.id.propertyBtn4 :
                    propertyType = propertyBtn[4].getText().toString();
                    setFocusProperty(propertyBtn_unfocused, propertyBtn[4]);
                    break;
                case R.id.propertyBtn5 :
                    propertyType = propertyBtn[5].getText().toString();
                    setFocusProperty(propertyBtn_unfocused, propertyBtn[5]);
                    break;
            }
        }
    }
    private void setFocusProperty(Button propertyBtn_unfocused, Button propertyBtn_focus){
        propertyBtn_unfocused.setTextColor(Color.rgb(49, 50, 51));
        propertyBtn_unfocused.setBackground(getResources().getDrawable(R.drawable.shape_button_border));
        propertyBtn_focus.setTextColor(Color.rgb(255, 255, 255));
        propertyBtn_focus.setBackgroundColor(Color.parseColor("#000000"));
        this.propertyBtn_unfocused = propertyBtn_focus;
    }

    //====================================================| Renter Button Action
    private class ActionHandlerRenter implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.renterBtn0 :
                    renterType = renterBtn[0].getText().toString();
                    setFocusRenter(renterBtn_unfocused, renterBtn[0]);
                    break;
                case R.id.renterBtn1 :
                    renterType = renterBtn[1].getText().toString();
                    setFocusRenter(renterBtn_unfocused, renterBtn[1]);
                    break;
                case R.id.renterBtn2 :
                    renterType = renterBtn[2].getText().toString();
                    setFocusRenter(renterBtn_unfocused, renterBtn[2]);
                    break;
                case R.id.renterBtn3 :
                    renterType = renterBtn[3].getText().toString();
                    setFocusRenter(renterBtn_unfocused, renterBtn[3]);
                    break;
                case R.id.renterBtn4 :
                    renterType = renterBtn[4].getText().toString();
                    setFocusRenter(renterBtn_unfocused, renterBtn[4]);
                    break;
                case R.id.renterBtn5 :
                    renterType = renterBtn[5].getText().toString();
                    setFocusRenter(renterBtn_unfocused, renterBtn[5]);
                    break;
            }
        }
    }
    private void setFocusRenter(Button btn_unfocused, Button btn_focus){
        btn_unfocused.setTextColor(Color.rgb(49, 50, 51));
        btn_unfocused.setBackground(getResources().getDrawable(R.drawable.shape_button_border));
        btn_focus.setTextColor(Color.rgb(255, 255, 255));
        btn_focus.setBackgroundColor(Color.parseColor("#000000"));
        this.renterBtn_unfocused = btn_focus;
    }

    //====================================================| Bedrooms Button Action
    private class ActionHandlerBedrooms implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.bedBtn0 :
                    bedRooms = bedBtn[0].getText().toString();
                    setFocusBed(bedBtn_unfocused, bedBtn[0]);
                    break;
                case R.id.bedBtn1 :
                    bedRooms = bedBtn[1].getText().toString();
                    setFocusBed(bedBtn_unfocused, bedBtn[1]);
                    break;
                case R.id.bedBtn2 :
                    bedRooms = bedBtn[2].getText().toString();
                    setFocusBed(bedBtn_unfocused, bedBtn[2]);
                    break;
                case R.id.bedBtn3 :
                    bedRooms = bedBtn[3].getText().toString();
                    setFocusBed(bedBtn_unfocused, bedBtn[3]);
                    break;
                case R.id.bedBtn4 :
                    bedRooms = bedBtn[4].getText().toString();
                    setFocusBed(bedBtn_unfocused, bedBtn[4]);
                    break;
            }
        }
    }

    private void setFocusBed(Button bedBtn_unfocused, Button bedBtn_focus){
        bedBtn_unfocused.setTextColor(Color.rgb(49, 50, 51));
        bedBtn_unfocused.setBackground(getResources().getDrawable(R.drawable.shape_button_border));
        bedBtn_focus.setTextColor(Color.rgb(255, 255, 255));
        bedBtn_focus.setBackgroundColor(Color.parseColor("#000000"));
        this.bedBtn_unfocused = bedBtn_focus;
    }
}
