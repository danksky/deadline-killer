package com.pherodev.killddl.notifications.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.VibrationEffect;
import android.support.v4.app.NotificationCompat;

import com.pherodev.killddl.R;
import com.pherodev.killddl.activities.CategoryActivity;
import com.pherodev.killddl.database.DatabaseHelper;
import com.pherodev.killddl.models.Task;


public class NotificationPublisher extends BroadcastReceiver {

    public static String NOTIFICATION_TYPE = "notification_type";
    public static String NOTIFICATION_ID = "notification_id";

    @Override
    public void onReceive(final Context context, Intent receivedIntent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = receivedIntent.getExtras().getInt(NOTIFICATION_ID, 0);

        Intent launchIntent = new Intent(context, CategoryActivity.class);
        launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingLaunchIntent = PendingIntent.getActivity(context, notificationId, launchIntent, 0);

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        Task task = dbHelper.getTaskById(notificationId);

        NotificationCompat.Builder notificationbuilder = new NotificationCompat.Builder(context)
                .setContentTitle(task.getTitle())
                .setContentText(task.getDescription())
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setChannelId("CHANNEL_ID")
//                .setLargeIcon(((BitmapDrawable) getApplicationContext().getResources().getDrawable(R.drawable.ic_launcher_foreground)).getBitmap())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[5])
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(pendingLaunchIntent);

        Notification notification = notificationbuilder.build();

        notificationManager.notify(notificationId, notification);
    }
}
