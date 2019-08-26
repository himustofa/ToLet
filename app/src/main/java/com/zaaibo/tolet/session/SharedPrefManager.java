package com.zaaibo.tolet.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zaaibo.tolet.models.User;

import java.util.ArrayList;
import java.util.Objects;

public class SharedPrefManager {

    private static final String USER_PREF = "shared_pref_user";

    private static final String SHARED_PREF_NAME = "shared_pref";
    private static final String AUTH_ID = "authid_key";
    private static final String PHONE_NUMBER = "phone_key";
    private static final String IS_LOGGED = "login_key";
    private static final String LAT_KEY = "lat_key";
    private static final String LNG_KEY = "lng_key";
    private static final String FAV_KEY = "favorite_key";

    private static final String SHARED_PREF_TOKEN = "shared_pref_app";
    private static final String TAG_TOKEN = "token_key";

    //Singleton Design Pattern
    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //===============================================| Token
    public void saveDeviceToken(String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_TOKEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_TOKEN, token);
        editor.apply();
        editor.commit();
    }

    public String getDeviceToken(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_TOKEN, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_TOKEN, null);
    }

    //===============================================| Save
    public void saveUser(User model){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("UserModel", new Gson().toJson(model));
        editor.apply();
        editor.commit(); //for old version
    }

    public void savePhoneAndLogInStatus(String phone, boolean isLogged){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PHONE_NUMBER, phone);
        editor.putBoolean(IS_LOGGED, isLogged);
        editor.apply();
        editor.commit(); //for old version
    }

    public void saveUserAuthId(String authId){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AUTH_ID, authId);
        editor.apply();
        editor.commit(); //for old version
    }

    public void saveCurrentLatLng(LatLng latLng){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LAT_KEY, String.valueOf(latLng.latitude));
        editor.putString(LNG_KEY, String.valueOf(latLng.longitude));
        editor.apply();
        editor.commit(); //for old version
    }

    public void saveFavoriteItem(String mAuthId){
        SharedPreferences ref = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = ref.edit();

        ArrayList<String> arrayList = new ArrayList<>();
        ArrayList<String> keys = getFavoriteItems();
        if (keys != null) {
            if (!keys.contains(mAuthId)) {
                keys.add(mAuthId);
                editor.putString(FAV_KEY, new Gson().toJson(keys));
            }
        } else {
            arrayList.add(mAuthId);
            editor.putString(FAV_KEY, new Gson().toJson(arrayList));
        }
        editor.apply();
        editor.commit(); //for old version
    }

    //===============================================| Fetch/Get
    public User getUser(){
        SharedPreferences pref = mCtx.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        return new Gson().fromJson(pref.getString("UserModel", null), User.class);
    }

    public boolean getUserIsLoggedIn(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getBoolean(IS_LOGGED, false);
    }

    public String getPhoneNumber(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(PHONE_NUMBER, null);
    }

    public String getUserAuthId(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(AUTH_ID, null);
    }

    public LatLng getCurrentLatLng(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        LatLng latLng = null;
        double lat = 0.0, lng = 0.0;
        if (sharedPreferences.getString(LAT_KEY, null) != null) {
            lat = Double.parseDouble(Objects.requireNonNull(sharedPreferences.getString(LAT_KEY, null)));
        }
        if (sharedPreferences.getString(LNG_KEY, null) != null) {
            lng = Double.parseDouble(Objects.requireNonNull(sharedPreferences.getString(LNG_KEY, null)));
        }
        if (lat > 0.0 && lng > 0.0) {
            latLng = new LatLng(lat, lng);
        }
        return latLng;
    }

    public ArrayList<String> getFavoriteItems(){
        SharedPreferences ref = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Gson().fromJson(ref.getString(FAV_KEY, null), new TypeToken<ArrayList<String>>(){}.getType());
    }

    //===============================================| Delete
    public void deleteCurrentSession(){
        SharedPreferences pre = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.clear(); //Remove from login.xml file
        editor.apply();
        editor.commit(); //for old version
    }

    public void deleteFavoriteItems(){
        SharedPreferences pre = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.remove(FAV_KEY);
        editor.apply();
        editor.commit();
    }

}
