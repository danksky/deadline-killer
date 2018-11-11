package com.pherodev.killddl.notifications;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;

import com.pherodev.killddl.notifications.receivers.NotificationPublisher;

public class NotificationsHelper {

    public static final String TASK_DEADLINE = "TASK_DEADLINE";

    private static void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "CHANNEL_NAME";
            String description = "CHANNEL_DESCRIPTION";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void scheduleTaskNotification(Context context, long delay, int notificationId) {
        createNotificationChannel(context);
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        // TYPE and ID act as the notification distinguishing factors.
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_TYPE, TASK_DEADLINE);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, notificationId);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingNotificationIntent = PendingIntent.getBroadcast(
                context,
                notificationId,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // Clear any preexisting instances of this alarm
        alarmManager.cancel(pendingNotificationIntent);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingNotificationIntent);
    }
}
