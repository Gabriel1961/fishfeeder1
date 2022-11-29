package com.example.fishfeeder;

import static android.content.Context.ALARM_SERVICE;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class NotificationsService {
    private static String CHANNEL_ID;
    private Activity activity;
    public NotificationsService(Activity activity)
    {
        this.activity = activity;
        showNotification(new Intent(activity.getApplicationContext(), MainActivity.class),1);
    }


    public void showNotification(Intent intent, int reqCode) {


        PendingIntent pendingIntent = PendingIntent.getActivity(activity, reqCode, intent, PendingIntent.FLAG_IMMUTABLE);
        String CHANNEL_ID = "My Fishy";// The id of the channel.
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(activity, CHANNEL_ID)
                .setSmallIcon(R.drawable.fish)
                .setContentTitle("Dont forget to clean your fish tank.")
                .setContentText("One week as passed, it is time for you to replace at least 25% of the water in the aquarium.")
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Fishy";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(reqCode, notificationBuilder.build()); // 0 is the request code, it should be unique id
    }
    // TODO SCHEDULE NOTIFCATIONS
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void scheduleNotification(Calendar calendar) {
        Intent intent = new Intent(activity.getApplicationContext(), Notification.class);
        intent.putExtra("titleExtra", "Dynamic Title");
        intent.putExtra("textExtra", "Dynamic Text Body");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity.getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Toast.makeText(activity.getApplicationContext(), "Scheduled ", Toast.LENGTH_LONG).show();
    }
}
