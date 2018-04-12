package ccn2279.a16031806a.nodrinknolife.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import ccn2279.a16031806a.nodrinknolife.utilities.CalculateionUtils;

/**
 * Updated by Kiros Choi on 2018/04/11.
 */
public class AlarmReceiver extends BroadcastReceiver {
    public static final String ACTION_SAVE_DAILY_DRINKS = "save-daily-drinks";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ACTION_SAVE_DAILY_DRINKS.equals(action)) {
            try {
                CalculateionUtils.saveDailyDrinks(context);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("Debug_", "ACTION_SAVE_DAILY_DRINKS");
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);
            Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show();
        } else if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            try {
                CalculateionUtils.saveDailyDrinks(context);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
