package com.zaaibo.tolet.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.zaaibo.tolet.R;
import com.zaaibo.tolet.sharedprefs.SharedPrefManager;
import com.zaaibo.tolet.views.activities.NotificationActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MessagingService";

    private LocalBroadcastManager broadcaster;

    @Override
    public void onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "FCM Message Notification Title: " + remoteMessage.getNotification().getTitle());
            Log.d(TAG, "FCM Message Notification Body: " + remoteMessage.getNotification().getBody());
            //sendMyBroadCast(remoteMessage.getNotification().getTitle());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0 && !remoteMessage.getData().isEmpty()) {
            Log.d(TAG, "FCM Message data payload: " + remoteMessage.getData());

            Bundle bundle = new Bundle();
            //bundle.putString(ConstantKey.FINISH_MODEL, remoteMessage.getData().get(ConstantKey.FINISH_MODEL));
            //sendMyBroadCast(false, remoteMessage.getNotification().getTitle(), remoteMessage.getData().get(ConstantKey.FINISH_MODEL));
            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), bundle);
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    //====================================================| Sent Broadcast
    /*private void sendMyBroadCast(boolean isUser, String title, String data) {
        try {
            Intent intent = new Intent(ConstantKey.NOTIFICATION_BROADCAST_RECEIVER);
            intent.putExtra("title", title);
            intent.putExtra(ConstantKey.FINISH_MODEL, data);
            broadcaster.sendBroadcast(intent);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }*/

    //===============================================| Show Notification for All Data
    private void sendNotification(String title, String body, Bundle bundle) {

        Intent intent = new Intent(this, NotificationActivity.class);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        //https://developer.android.com/guide/topics/ui/notifiers/notifications#ManageChannels
        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_radio_button_checked_black_24dp)
                .setWhen(System.currentTimeMillis())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setChannelId(channelId)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo);
            notificationBuilder.setLargeIcon(icon);
        }*/

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,"Notification", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.enableVibration(true);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
        manager.notify(0, notificationBuilder.build());
    }

    //===============================================| New FirebaseInstanceIdService
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, token);
        SharedPrefManager.getInstance(getApplicationContext()).saveDeviceToken(token);
    }
}
