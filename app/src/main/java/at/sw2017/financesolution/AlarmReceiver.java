package at.sw2017.financesolution;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by joe on 06.06.17.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setSmallIcon(R.drawable.ic_alarm_add_black_24dp);
        notificationBuilder.setContentTitle(intent.getStringExtra("REMINDER_TITLE"));
        notificationBuilder.setContentText("Don't forget your reminder.");

        Intent resultIntent = new Intent(context, ReminderActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) intent.getLongExtra("REMINDER_ID", 0), resultIntent, PendingIntent.FLAG_ONE_SHOT);

        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify((int) intent.getLongExtra("REMINDER_ID", 0), notificationBuilder.build());
    }
}
