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
        String medicationTitle = (String) intent.getExtras().get("medicationTitle");

        NotificationManager notificationManager = (NotificationManager)context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationIntent.putExtra("notification", medicationTitle);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, key, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("iCare Alert")
                .setContentText("Eat " + medicationTitle)
                .setSound(alarmSound)
                .setOngoing(true)
                .setAutoCancel(true);

        notificationManager.notify(key, builder.build());
    }
}