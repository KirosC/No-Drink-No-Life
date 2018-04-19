package ccn2279.a16031806a.nodrinknolife.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

import ccn2279.a16031806a.nodrinknolife.R;

import static ccn2279.a16031806a.nodrinknolife.utilities.SharedPreferencesUtils.MONDAY;
import static ccn2279.a16031806a.nodrinknolife.utilities.SharedPreferencesUtils.PREFERENCE_NAME;
import static ccn2279.a16031806a.nodrinknolife.utilities.SharedPreferencesUtils.SUNDAY;

/**
 * Created by ownere on 7/4/2018.
 */

public class CalculationUtils {
    private static final float DECREASE_PER_SEC = (float) 0.00231481482;
    private static final float INCREASE_PER_DRINK = (float) 25;
    private static float health;

    public static final String TAG = "Debug_NoDrinkNoLife";

    private static SharedPreferences sPreferences;
    private static Toast toast;

    public static void updateHealth(Context context) throws Exception {
        sPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        health = sPreferences.getFloat(context.getString(R.string.character_health), (float) 0);
        long lastUpdate = sPreferences.getLong(context.getString(R.string.last_update_time), (long) 0);
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        health -= (cal.getTimeInMillis() - lastUpdate) / 1000 * DECREASE_PER_SEC;
        if (health < (float) 0) {
            health = (float) 0;
        }
        if (health <= (float) 0.04) {
            makeCharacterDead(context);
        } else if (health > (float) 199.95) {
            makeCharacterDead(context);
        }
        lastUpdate = cal.getTimeInMillis();
        SharedPreferencesUtils.setValue(context, context.getString(R.string.character_health), health);
        SharedPreferencesUtils.setValue(context, context.getString(R.string.last_update_time), lastUpdate);
        if (health <= (float) 0.04) {
            makeCharacterDead(context);
        }
    }

    public static void increaseDrankWater(Context context) throws Exception {
        updateHealth(context);
        sPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        health = sPreferences.getFloat(context.getString(R.string.character_health), 0);
        int todayDrink = sPreferences.getInt(context.getString(R.string.today_drinks), 0);
        health += INCREASE_PER_DRINK;
        todayDrink++;

        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, context.getString(R.string.water_chug_toast), Toast.LENGTH_SHORT);
        toast.show();
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(50);

        Log.d(TAG, "Health: " + health);
        if (health > (float) 199.95) {
            health = 200;
            makeCharacterDead(context);
        }
        SharedPreferencesUtils.setValue(context, context.getString(R.string.character_health), health);
        SharedPreferencesUtils.setValue(context, context.getString(R.string.today_drinks), todayDrink);
    }

    /**
     * Reset the character to respawn
     *
     * @param context The context of the activity.
     */
    public static void respawnCharacter(Context context) throws Exception {
        long lastUpdate;
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        lastUpdate = cal.getTimeInMillis();
        SharedPreferencesUtils.setValue(context, context.getString(R.string.character_health), (float) 100);
        SharedPreferencesUtils.setValue(context, context.getString(R.string.last_update_time), lastUpdate);
        SharedPreferencesUtils.setValue(context, context.getString(R.string.not_enough_water), false);
        SharedPreferencesUtils.setValue(context, context.getString(R.string.too_much_water), false);
    }

    public static void makeCharacterDead(Context context) throws Exception {
        if (health < (float) 0.04) {
            SharedPreferencesUtils.setValue(context, context.getString(R.string.not_enough_water), true);
        } else {
            SharedPreferencesUtils.setValue(context, context.getString(R.string.too_much_water), true);
        }
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

        Log.d(TAG, "Today day: " + todayDay + " P Day: " + preferencesDay);

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

        Log.d(TAG, prefix + preferencesDay);
        SharedPreferencesUtils.setValue(context, prefix + preferencesDay, todayDrinks);

        // Set new day and number of drink
        SharedPreferencesUtils.setValue(context, context.getString(R.string.today_day), todayDay);
        SharedPreferencesUtils.setValue(context, context.getString(R.string.today_drinks), 0);

        // Change the using week
        if (todayDay == MONDAY) {
            SharedPreferencesUtils.setValue(context, context.getString(R.string.is_using_weekA), !isUsingWeekA);
        }
    }
}
