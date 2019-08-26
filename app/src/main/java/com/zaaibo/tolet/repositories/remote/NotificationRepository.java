package com.zaaibo.tolet.repositories.remote;

import androidx.lifecycle.MutableLiveData;

import com.zaaibo.tolet.models.User;
import com.zaaibo.tolet.repositories.firebase.FirebaseRepository;

import java.util.ArrayList;

public class NotificationRepository {

    private static NotificationRepository mInstance;

    public static NotificationRepository getInstance() {
        if (mInstance == null) {
            mInstance = new NotificationRepository();
        }
        return mInstance;
    }

    //Pretend to get data from a webservice or online source
    public MutableLiveData<ArrayList<User>> getNotifications(String mAuthId) {
        MutableLiveData<ArrayList<User>> data = new MutableLiveData<>();
        new FirebaseRepository().getNotifications(new FirebaseRepository.NotificationCallback() {
            @Override
            public void onCallback(ArrayList<User> users) {
                //Log.d(TAG, ""+new Gson().toJson(model));
                data.setValue(users);
            }
        }, mAuthId);
        return data;
    }
}
