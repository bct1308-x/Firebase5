package com.example.firebase5;

import android.Manifest;

import androidx.annotation.RequiresPermission;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    @Override
    public void onMessageReceived(RemoteMessage message) {
        super.onMessageReceived(message);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, "channel")
                        .setContentTitle("Movie Reminder")
                        .setContentText(message.getNotification().getBody())
                        .setSmallIcon(R.mipmap.ic_launcher);

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(1, builder.build());
    }
}