package ccn2279.a16031806a.nodrinknolife.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import ccn2279.a16031806a.nodrinknolife.MainActivity;
import ccn2279.a16031806a.nodrinknolife.R;
import ccn2279.a16031806a.nodrinknolife.sync.ReminderTasks;
import ccn2279.a16031806a.nodrinknolife.sync.WaterReminderIntentService;

/**
 * Updated by Kiros Choi on 2018/04/11.
 */
public class NotificationUtils {
    private static final int REMINDER_NOTIFICATION_ID = 1001;

    private static final int REMINDER_PENDING_INTENT_ID = 2001;
    private static final int ACTION_IGNORE_PENDING_INTENT_ID = 2002;
    private static final int ACTION_DRINK_PENDING_INTENT_ID = 2003;

    private static final String REMINDER_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";

    /**
     * Generate a PendingIntent which can open the MainActivity.
     *
     * @param context The context of the activity.
     * @return PendingIntent that can open the MainActivity.
     */
    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context, REMINDER_PENDING_INTENT_ID, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Convert a Drawable to Bitmap
     *
     * @param context The context of the activity.
     * @return Bitmap
     */
    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();

        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.ic_drink);
        return largeIcon;
    }

    /**
     * Make a new notification to notify the user to drink water
     *
     * @param context The context of the activity.
     */
    public static void remindUserToDrink(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a new notification channel because it is required since Android Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(REMINDER_NOTIFICATION_CHANNEL_ID, context.getString(R.string.main_notification_channel_name), NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }

        // Build a Notification with specific setting
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, REMINDER_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_drink)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.charging_reminder_notification_title))
                .setContentText(context.getString(R.string.charging_reminder_notification_body))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.charging_reminder_notification_body)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .addAction(drinkWaterAction(context))
                .addAction(ignoreReminderAction(context))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        notificationManager.notify(REMINDER_NOTIFICATION_ID, notificationBuilder.build());
    }

    /**
     * Clear the notification on the screen
     *
     * @param context The context of the activity.
     */
    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(REMINDER_NOTIFICATION_ID);
    }

    /**
     * Generate an Action which ignores the Notification and dismisses it
     *
     * @param context The context of the activity.
     * @return NotificationCompat.Action
     */
    private static NotificationCompat.Action ignoreReminderAction(Context context) {
        Intent ignoreReminderIntent = new Intent(context, WaterReminderIntentService.class);
        // Set the Action ID
        ignoreReminderIntent.setAction(ReminderTasks.ACTION_DISMISS_NOTIFICATION);

        PendingIntent ignoreReminderPendingIntent = PendingIntent.getService(
                context,
                ACTION_IGNORE_PENDING_INTENT_ID,
                ignoreReminderIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action ignoreReminderAction = new NotificationCompat.Action(
                R.drawable.ic_cancel,
                context.getString(R.string.ignore_reminder),
                ignoreReminderPendingIntent);

        return ignoreReminderAction;
    }

    /**
     * Generate an Action for which the user has been drank the water and dismiss the Notification.
     *
     * @param context The context of the activity.
     * @return NotificationCompat.Action
     */
    private static NotificationCompat.Action drinkWaterAction(Context context) {
        Intent incrementWaterCountIntent = new Intent(context, WaterReminderIntentService.class);
        incrementWaterCountIntent.setAction(ReminderTasks.ACTION_INCREMENT_WATER_COUNT);

        PendingIntent incrementWaterPendingIntent = PendingIntent.getService(
                context,
                ACTION_DRINK_PENDING_INTENT_ID,
                incrementWaterCountIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action drinkWaterAction = new NotificationCompat.Action(
                R.drawable.ic_local_drink_black_24dp,
                context.getString(R.string.accept_reminder),
                incrementWaterPendingIntent);

        return drinkWaterAction;
    }
}