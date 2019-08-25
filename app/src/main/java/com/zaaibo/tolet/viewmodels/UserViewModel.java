package com.zaaibo.tolet.viewmodels;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zaaibo.tolet.models.User;
import com.zaaibo.tolet.repositories.remote.UserRepository;

import java.util.ArrayList;

public class UserViewModel extends ViewModel {

    private UserRepository mUserRepository;

    public UserViewModel() {
        this.mUserRepository = UserRepository.getInstance();
    }

    public LiveData<User> getUser(String mAuthId) {
        MutableLiveData<User> mUser = mUserRepository.getUser(mAuthId);
        return mUser;
    }

    public LiveData<String> storeUser(User user) {
        return mUserRepository.storeUser(user);
    }

    public LiveData<String> storeUserImage(Uri uri, String directory, String mAuthId) {
        return mUserRepository.storeUserImage(uri, directory, mAuthId);
    }
}
