package com.zaaibo.tolet.repositories.remote;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import com.zaaibo.tolet.models.User;

import java.util.ArrayList;

//Singleton pattern
public class UserRepository {

    private static UserRepository mInstance;

    public static UserRepository getInstance() {
        if (mInstance == null) {
            mInstance = new UserRepository();
        }
        return mInstance;
    }

    //Pretend to get data from a webservice or online source
    public MutableLiveData<User> getUser(String mAuthId) {
        MutableLiveData<User> data = new MutableLiveData<>();
        new FirebaseRepository().getUserData(new FirebaseRepository.UserCallback() {
            @Override
            public void onCallback(User model) {
                //Log.d(TAG, ""+new Gson().toJson(model));
                data.setValue(model);
            }
        }, mAuthId);
        return data;
    }

    public MutableLiveData<String> storeUser(User user) {
        MutableLiveData<String> data = new MutableLiveData<>();
        new FirebaseRepository().storeUserData(new FirebaseRepository.UserStoreCallback() {
            @Override
            public void onCallback(String result) {
                if (result != null) {
                    data.setValue(result);
                }
            }
        }, user);
        return data;
    }

    public MutableLiveData<String> storeUserImage(Uri uri, String directory, String mAuthId) {
        MutableLiveData<String> data = new MutableLiveData<>();
        new FirebaseRepository().uploadImageToStorage(new FirebaseRepository.ImageStorageCallback() {
            @Override
            public void onCallback(String result) {
                data.setValue(result);
            }
        }, uri, directory, mAuthId);
        return data;
    }
}
