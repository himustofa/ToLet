package com.zaaibo.tolet;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.OnDisconnect;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.zaaibo.tolet.utils.language.LocaleHelper;

public class MainApplication extends Application {

    private static final String TAG = "MainApplication";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true); //Cached data is available while offline
        //setOfflineConnection();
        saveOnDisconnectState();
        loadImageOfflineByPicasso();
    }

    /**********************************************************************************
     * Start: Enabling Offline Capabilities on Android
     * Link: https://firebase.google.com/docs/database/android/offline-capabilities
     ***********************************************************************************/

    //===============================================| Connection State
    public void saveOnDisconnectState() {
        DatabaseReference presenceRef = FirebaseDatabase.getInstance().getReference("disconnectmessage");
        presenceRef.onDisconnect().setValue("I disconnected!"); // Write a string when this client loses connection
    }

    public void cancelOnDisconnectState() {
        DatabaseReference presenceRef = FirebaseDatabase.getInstance().getReference("disconnectmessage");
        OnDisconnect onDisconnectRef = presenceRef.onDisconnect();
        onDisconnectRef.setValue("I disconnected");
        onDisconnectRef.cancel();
    }

    public void deleteOnDisconnectState() {
        DatabaseReference presenceRef = FirebaseDatabase.getInstance().getReference("disconnectmessage");
        presenceRef.onDisconnect().removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, @NonNull DatabaseReference reference) {
                if (error != null) {
                    Log.d(TAG, "could not establish onDisconnect event:" + error.getMessage());
                }
            }
        });
    }

    //===============================================| Checking Connection State
    public void getConnectionState() {
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    Log.d(TAG, "connected");
                } else {
                    Log.d(TAG, "not connected");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Listener was cancelled");
            }
        });
    }

    //===============================================| Set server time when client is disconnected
    public void disconnectionTimestamp() {
        DatabaseReference userLastOnlineRef = FirebaseDatabase.getInstance().getReference("users/joe/lastOnline");
        userLastOnlineRef.onDisconnect().setValue(ServerValue.TIMESTAMP);
    }

    //Clients set this location to true when they come online and a timestamp when they disconnect.
    //Note that your app should queue the disconnect operations before a user is marked online, to avoid any race conditions in the event that the client's network connection is lost before both commands can be sent to the server.
    public void getServerTimeOffset() {
        DatabaseReference offsetRef = FirebaseDatabase.getInstance().getReference(".info/serverTimeOffset");
        offsetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double offset = snapshot.getValue(Double.class);
                double estimatedServerTimeMs = System.currentTimeMillis() + offset;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Listener was cancelled");
            }
        });
    }


    //Need to google maps offline

    public void setOfflineConnection() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            DatabaseReference mUserDatabase = FirebaseDatabase.getInstance().getReference().child("connection").child(mAuth.getCurrentUser().getUid());
            mUserDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mUserDatabase.child("online").onDisconnect().setValue("offline");
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    /**********************************************************************************
     * End: Enabling Offline Capabilities on Android
     ***********************************************************************************/

    public void loadImageOfflineByPicasso() {
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
    }
}
