package com.example.fishfeeder;

import static android.content.Context.ALARM_SERVICE;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.SystemClock;
import android.security.identity.PersonalizationData;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class NotificationsService {
    private final Activity activity;
    final int requestCode = 1;
    @RequiresApi(api = Build.VERSION_CODES.S)
    public NotificationsService(Activity activity)
    {
        this.activity = activity;
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    public void scheduleAlarm()
    {
        Intent intent = new Intent(activity,AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(
                activity,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        AlarmManager manager = (AlarmManager) activity.getSystemService(ALARM_SERVICE);
        boolean alarmEnabled = (PendingIntent.getBroadcast(activity.getApplicationContext(), requestCode,
                intent,
                PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE) != null);
        if(!alarmEnabled)
        {
            manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+3_000,AlarmManager.INTERVAL_DAY*7,pi);
            Log.d("xxx","scheduled notification");
        }
        else
            Log.d("xxx","notification already scheduled");

    }


    public static class AlarmReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("xxx","creating notification");
            String CHANNEL_ID = "Channel1";
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.fish)
                    .setContentTitle("Dont forget to clean your fish tank.")
                    .setContentText("One week as passed, it is time for you to replace at least 25% of the water in the aquarium.")
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "My Fishy";// The user-visible name of the channel.
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                notificationManager.createNotificationChannel(mChannel);
                notificationManager.notify(1,notificationBuilder.build());
            }
        }

    }
}
