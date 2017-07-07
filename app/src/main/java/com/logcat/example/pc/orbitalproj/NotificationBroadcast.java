package com.logcat.example.pc.orbitalproj;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

public class NotificationBroadcast extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        int key = (int) intent.getExtras().get("Key");

        NotificationManager notificationManager = (NotificationManager)context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 123, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Notification title")
                .setContentText("Content Text")
                .setSound(alarmSound)
                .setOngoing(true)
                .setAutoCancel(true);

        notificationManager.notify(123, builder.build());
    }
}