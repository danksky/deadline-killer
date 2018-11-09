package com.pherodev.killddl.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.pherodev.killddl.notifications.receivers.NotificationPublisher;

public class NotificationsHelper {

    public static final String TASK_DEADLINE = "TASK_DEADLINE";

    public static void scheduleTaskNotification(Context context, long delay, int notificationId) {
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
