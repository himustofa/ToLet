package com.zaaibo.tolet.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.zaaibo.tolet.R;
import com.zaaibo.tolet.services.MyNetworkReceiver;
import com.zaaibo.tolet.utils.language.LocaleHelper;

import io.fabric.sdk.android.Fabric;

public class FeedbackActivity extends AppCompatActivity {

    private MyNetworkReceiver mNetworkReceiver;
    private ProgressDialog mProgress = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_feedback);

        mNetworkReceiver = new MyNetworkReceiver(this);
        //mProgress = Utility.showProgressDialog(FeedbackActivity.this, getResources().getString( R.string.rotate_progress), false);
        //Utility.dismissProgressDialog(mProgress);
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
        registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
