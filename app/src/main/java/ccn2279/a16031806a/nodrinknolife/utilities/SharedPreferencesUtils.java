package ccn2279.a16031806a.nodrinknolife.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;
import java.util.TimeZone;

import ccn2279.a16031806a.nodrinknolife.R;

/**
 * Updated by Kiros Choi on 2018/04/11.
 */
public class SharedPreferencesUtils {
    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;
    public static final int SUNDAY = 7;
    public static final String PREFERENCE_NAME = "NoDrinkNoLifePreferences";

    private static SharedPreferences sPreferences;

    /**
     * Initialize the SharedPreferences or get reference to the SharedPreferences
     * if it is already initialized.
     *
     * @param context The context of the activity.
     * @return Status indicator.
     * -1 = Unsuccessfully initialize SharedPreferences
     * 0 = SharedPreferences is already initialized
     * 1 = Successfully initialize SharedPreferences
     */
    public static int initSharedPreferences(Context context) {
        sPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);

        // Check if SharedPreferences is initialized
        if (sPreferences.contains(context.getString(R.string.today_drinks))) {
            return 0;
        }

        //      Initialize SharedPreferences value      //
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        dayOfWeek = (dayOfWeek == 1) ? SUNDAY : --dayOfWeek;

        SharedPreferences.Editor editor = sPreferences.edit();
        editor.putInt(context.getString(R.string.today_drinks), 0);
        editor.putInt(context.getString(R.string.today_day), dayOfWeek);
        editor.putLong(context.getString(R.string.last_update_time), Calendar.getInstance().getTimeInMillis());
        editor.putFloat(context.getString(R.string.character_health), (float) 100);
        editor.putBoolean(context.getString(R.string.first_week), true);
        editor.putBoolean(context.getString(R.string.is_using_weekA), true);
        for (int i = MONDAY; i <= SUNDAY; i++) {
            editor.putInt(context.getString(R.string.weekA_prefix) + i, 0);
            editor.putInt(context.getString(R.string.weekB_prefix) + i, 0);
        }
        //              End of initialization           //

        // Write the SharedPreferences to the device storage
        boolean flag = editor.commit();
        return flag ? 1 : -1;
    }

    /**
     * Set the value of a key in the SharedPreferences.
     *
     * @param context The context of the activity.
     * @param key     The String value of the editing key.
     * @param value   The new value of the editing key.
     *
     * @exception IllegalArgumentException if key is not found.
     * @exception Exception if fail to save.
     */
    public static void setValue(Context context, String key, int value) throws Exception {
        boolean success;
        if (!sPreferences.contains(key)) {
            throw new IllegalArgumentException("Input key is not found");
        }
        SharedPreferences.Editor editor = sPreferences.edit();
        editor.putInt(key, value);

        success = editor.commit();
        if (!success) {
            throw new Exception("Fail to save");
        }
    }

    public static void setValue(Context context, String key, float value) throws Exception {
        boolean success;
        if (!sPreferences.contains(key)) {
            throw new IllegalArgumentException("Input key is not found");
        }
        SharedPreferences.Editor editor = sPreferences.edit();
        editor.putFloat(key, value);

        success = editor.commit();
        if (!success) {
            throw new Exception("Fail to save");
        }
    }

    public static void setValue(Context context, String key, boolean value) throws Exception {
        boolean success;
        if (!sPreferences.contains(key)) {
            throw new IllegalArgumentException("Input key is not found");
        }
        SharedPreferences.Editor editor = sPreferences.edit();
        editor.putBoolean(key, value);

        success = editor.commit();
        if (!success) {
            throw new Exception("Fail to save");
        }
    }

    public static void setValue(Context context, String key, long value) throws Exception {
        boolean success;
        if (!sPreferences.contains(key)) {
            throw new IllegalArgumentException("Input key is not found");
        }
        SharedPreferences.Editor editor = sPreferences.edit();
        editor.putLong(key, value);

        success = editor.commit();
        if (!success) {
            throw new Exception("Fail to save");
        }
    }
}