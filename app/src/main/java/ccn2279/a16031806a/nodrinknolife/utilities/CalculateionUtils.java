package ccn2279.a16031806a.nodrinknolife.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Calendar;
import java.util.TimeZone;

import ccn2279.a16031806a.nodrinknolife.R;

import static ccn2279.a16031806a.nodrinknolife.utilities.SharedPreferencesUtils.*;

/**
 * Created by ownere on 7/4/2018.
 */

public class CalculateionUtils {
    private static final float DECREASE_PER_SEC = (float) 0.00115740741;
    private static final float INCREASE_PER_DRINK = (float) 12.5;
    private static float health;

    public static final String TAG = "Debug_NoDrinkNoLife8888";


    private static SharedPreferences sPreferences;

    public static void updateHealth(Context context) throws Exception {
        sPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        health = sPreferences.getFloat(context.getString(R.string.character_health), (float) 0);
        long lastUpdate = sPreferences.getLong(context.getString(R.string.last_update_time), (long) 0);
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        health -= (cal.getTimeInMillis() - lastUpdate) / 1000 * DECREASE_PER_SEC;
        Log.d(TAG, String.valueOf(lastUpdate));
        if (health < 0) {
            health = 0;
        }
        lastUpdate = cal.getTimeInMillis();
        Log.d(TAG, String.valueOf(lastUpdate));
        SharedPreferencesUtils.setValue(context, context.getString(R.string.character_health), health);
        SharedPreferencesUtils.setValue(context, context.getString(R.string.last_update_time), lastUpdate);
    }

    public static void increaseDrankWater(Context context) throws Exception {
        updateHealth(context);
        sPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        health = sPreferences.getFloat(context.getString(R.string.character_health), 0);
        int todayDrink = sPreferences.getInt(context.getString(R.string.today_drinks), 0);
        health += INCREASE_PER_DRINK;
        if (health > 150) {
            health = 150;
        }
        todayDrink++;
        SharedPreferencesUtils.setValue(context, context.getString(R.string.character_health), health);
        SharedPreferencesUtils.setValue(context, context.getString(R.string.today_drinks), todayDrink);
    }

    /**
     * Save daily drank water to SharedPreferences
     *
     * @param context The context of the activity.
     * @throws Exception if fail to save
     */
    public static void saveDailyDrinks(Context context) throws Exception {
        String prefix;

        sPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        boolean isUsingWeekA = sPreferences.getBoolean(context.getString(R.string.is_using_weekA), false);
        int todayDrinks = sPreferences.getInt(context.getString(R.string.today_drinks), -1);
        int preferencesDay = sPreferences.getInt(context.getString(R.string.today_day), -1);
        int todayDay = cal.get(Calendar.DAY_OF_WEEK);
        todayDay = (todayDay == 1) ? SUNDAY : --todayDay;

        // Return if it is the same day
        if (todayDay == preferencesDay) {
            Log.d(TAG, "Cannot save on same day");
            return;
        }

        // Determine which week is using
        if (isUsingWeekA) {
            Log.d(TAG, "Use week A prefix");
            prefix = context.getString(R.string.weekA_prefix);
        } else {
            Log.d(TAG, "Use week B prefix");
            prefix = context.getString(R.string.weekB_prefix);
        }

        SharedPreferencesUtils.setValue(context, prefix + context.getString(R.string.today_day), todayDrinks);

        // Set new day and number of drink
        SharedPreferencesUtils.setValue(context, context.getString(R.string.today_day), todayDay);
        SharedPreferencesUtils.setValue(context, context.getString(R.string.today_drinks), 0);

        // Change the using week
        if (todayDay == MONDAY) {
            SharedPreferencesUtils.setValue(context, context.getString(R.string.is_using_weekA), !isUsingWeekA);
        }
    }
}
