package com.zaaibo.tolet.viewmodels;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zaaibo.tolet.models.Filter;
import com.zaaibo.tolet.models.PostAd;
import com.zaaibo.tolet.repositories.remote.PostAdRepository;

import java.util.ArrayList;

public class PostAdViewModel extends ViewModel {

    private static final String TAG = "PostAdViewModel";
    private PostAdRepository mPostAdRepository;

    public PostAdViewModel() {
        this.mPostAdRepository = PostAdRepository.getInstance();
    }

    public LiveData<ArrayList<PostAd>> getAllPostAd() {
        MutableLiveData<ArrayList<PostAd>> postAds = mPostAdRepository.getAllPostAd();
        return postAds;
    }

    public LiveData<PostAd> getPostAdById(String mAuthId) {
        return mPostAdRepository.getPostAdById(mAuthId);
    }

    public LiveData<ArrayList<PostAd>> getPostAdByFilter(Filter model) {
        MutableLiveData<ArrayList<PostAd>> postAdList = mPostAdRepository.getAllPostAdByLocation(model);
        //Log.d(TAG, ""+new Gson().toJson(postAdList));
        return postAdList;
    }

    public LiveData<String> storePostAd(PostAd postAd) {
        return mPostAdRepository.storePostAd(postAd);
    }

    public LiveData<String> storeImage(Uri uri, String directory, String mAuthId) {
        return mPostAdRepository.storeImage(uri, directory, mAuthId);
    }
}
