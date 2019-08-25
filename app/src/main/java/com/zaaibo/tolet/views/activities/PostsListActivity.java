package com.zaaibo.tolet.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.zaaibo.tolet.R;
import com.zaaibo.tolet.models.Filter;
import com.zaaibo.tolet.models.PostAd;
import com.zaaibo.tolet.services.MyNetworkReceiver;
import com.zaaibo.tolet.utils.ConstantKey;
import com.zaaibo.tolet.utils.Utility;
import com.zaaibo.tolet.utils.language.LocaleHelper;
import com.zaaibo.tolet.viewmodels.PostAdViewModel;
import com.zaaibo.tolet.views.adapters.PostsAdapter;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

public class PostsListActivity extends AppCompatActivity {

    private static final String TAG = "PostsListActivity";
    private MyNetworkReceiver mNetworkReceiver;
    private ProgressDialog mProgress = null;
    private RecyclerView mRecyclerView;
    private PostsAdapter mAdapter;
    //private ArrayList<PostAd> mArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_posts_list);

        mNetworkReceiver = new MyNetworkReceiver(this);
        mProgress = Utility.showProgressDialog(PostsListActivity.this, getResources().getString( R.string.progress), true);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        //Receive the data and observe the data from view model
        PostAdViewModel mPostAdViewModel = ViewModelProviders.of(this).get(PostAdViewModel.class); //Initialize view model

        if (getIntent().getExtras() != null) {
            Filter model = (Filter) getIntent().getSerializableExtra(ConstantKey.FILTER_KEY);
            mPostAdViewModel.getPostAdByFilter(model).observe(this, new Observer<ArrayList<PostAd>>() {
                @Override
                public void onChanged(ArrayList<PostAd> postAds) {
                    getAllPostData(postAds);
                }
            });
        } else {
            mPostAdViewModel.getAllPostAd().observe(this, new Observer<ArrayList<PostAd>>() {
                @Override
                public void onChanged(ArrayList<PostAd> postAds) {
                    getAllPostData(postAds);
                }
            });
        }

        //initRecyclerView();
    }

    private void getAllPostData(ArrayList<PostAd> postAds) {
        if (postAds != null) {
            Log.d(TAG, new Gson().toJson(postAds));
            //mArrayList.addAll(postAds);
            initRecyclerView(postAds);
            mAdapter.notifyDataSetChanged();
            Utility.dismissProgressDialog(mProgress);
        } else {
            Utility.dismissProgressDialog(mProgress);
            Utility.alertDialog(PostsListActivity.this, getResources().getString(R.string.msg_no_data));
        }
    }

    private void initRecyclerView(ArrayList<PostAd> mArrayList) {
        mAdapter = new PostsAdapter(this, mArrayList); //mUserViewModel.getUsers().getValue()
        //mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        //mAdapter.notifyDataSetChanged();
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
