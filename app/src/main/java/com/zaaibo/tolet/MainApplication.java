package com.zaaibo.tolet;

import android.app.Application;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zaaibo.tolet.utils.language.LocaleHelper;

public class MainApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setOfflineConnection();
    }

    //https://firebase.google.com/docs/database/android/offline-capabilities?fbclid=IwAR0WHI6iYKPRlyW8wdn7bd5_ISU8zAP6NMEGpm4F64BYMdSc2mdg9nq_y1A#section-presence
    public void setOfflineConnection() {
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            DatabaseReference mUserDatabase = FirebaseDatabase.getInstance().getReference().child("connection").child(mAuth.getCurrentUser().getUid());
            mUserDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null) {
                        mUserDatabase.child("online").onDisconnect().setValue("offline");
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }
}
