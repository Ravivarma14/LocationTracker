package com.example.locationtracker;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

public class MyForegroundService extends Service {

    private double latitude = 0, longitude = 0;
    static NotificationCompat.Builder notification;
    static Context context;
    static NotificationManager notificationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context=this;
        if (intent.getAction().equals("STOP")) {
            // stop the notification service
            stopForeground(true);
            stopSelf();
        }
        else {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                //Creating notification
                Log.d("MyForegroundService", "creating notification");

                NotificationChannel channel = new NotificationChannel("location", "Location", NotificationManager.IMPORTANCE_LOW);
                notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(channel);

                notification = new NotificationCompat.Builder(context, "location")
                        .setContentTitle("Tracking location...")
                        .setContentText("Location:null")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setOngoing(true);

                //start notification service
                startForeground(1, notification.build());
            }

        }

        return super.onStartCommand(intent, flags, startId);
    }

    //method to update notification content
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateContent(double lat, double lang){

        //update notification content with new langitude and lattitude
        Notification updated = new NotificationCompat.Builder(context, "location")
                .setContentTitle("Tracking location...")
                .setContentText("Location:" + lat + "," + lang)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setOngoing(true).build();

        notificationManager.notify(1,updated);
    }

}
