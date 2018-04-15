package ccn2279.a16031806a.nodrinknolife.sync;

import android.content.Context;

import ccn2279.a16031806a.nodrinknolife.utilities.CalculationUtils;
import ccn2279.a16031806a.nodrinknolife.utilities.SharedPreferencesUtils;

/**
 * Updated by Kiros Choi on 2018/04/11.
 */
public class ReminderTasks {
    public static final String ACTION_INCREMENT_WATER_COUNT = "increment-water-count";
    public static final String ACTION_DISMISS_NOTIFICATION = "dismiss-notification";
    public static final String ACTION_CHARGING_REMINDER = "charging-reminder";

    public static void executeTask(Context context, String action) {
        if (ACTION_INCREMENT_WATER_COUNT.equals(action)) {
            incrementWaterCount(context);
        } else if (ACTION_DISMISS_NOTIFICATION.equals(action)) {
            ccn2279.a16031806a.nodrinknolife.utilities.NotificationUtils.clearAllNotifications(context);
        } else if (ACTION_CHARGING_REMINDER.equals(action)) {
            issueChargingReminder(context);
        }
    }

    private static void issueChargingReminder(Context context) {
        ccn2279.a16031806a.nodrinknolife.utilities.NotificationUtils.remindUserToDrink(context);
    }

    private static void incrementWaterCount(Context context) {
        try {
            // TODO: Character die?
            SharedPreferencesUtils.initSharedPreferences(context);
            CalculationUtils.increaseDrankWater(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ccn2279.a16031806a.nodrinknolife.utilities.NotificationUtils.clearAllNotifications(context);
    }
}
