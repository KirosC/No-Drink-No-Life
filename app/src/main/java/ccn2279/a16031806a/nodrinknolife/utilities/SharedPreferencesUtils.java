package ccn2279.a16031806a.nodrinknolife.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.TimeZone;

import ccn2279.a16031806a.nodrinknolife.R;

public class SharedPreferencesUtils {
    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;
    public static final int SUNDAY = 7;
    public static final String PREFERENCE_NAME = "NoDrinkNoLifePreferences";

    private static boolean initialized = false;

    private static SharedPreferences sPreferences;

    private SharedPreferencesUtils(@NonNull Context context) {
        sPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static boolean getInstance(@NonNull Context context) {
        sPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        initialized = true;
        return true;
    }

    public static int initSharedPreferences(Context context){
        if (!initialized) {
            return -1;
        }
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        dayOfWeek = (dayOfWeek == 1) ? SUNDAY : --dayOfWeek;

        SharedPreferences.Editor editor = sPreferences.edit();
        editor.putInt(context.getString(R.string.today_drinks), 0);
        editor.putInt(context.getString(R.string.today_day), dayOfWeek);
        editor.putLong(context.getString(R.string.last_update_time), 0);
        editor.putFloat(context.getString(R.string.character_health), (float) 100);

        for (int i = MONDAY; i <= SUNDAY; i++) {
            editor.putInt(context.getString(R.string.day_prefix) + i, 0);
        }
        boolean flag = editor.commit();
        return flag ? 1 : 0;
    }

    public static boolean setValue(Context context, String key, int value) {
        SharedPreferences.Editor editor = sPreferences.edit();
        editor.putInt(key, value);

        editor.commit();
        return true;
    }

    public static boolean setValue(Context context, String key, float value) {
        SharedPreferences.Editor editor = sPreferences.edit();
        editor.putFloat(key, value);

        editor.commit();
        return true;
    }
}