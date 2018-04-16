package ccn2279.a16031806a.nodrinknolife;

import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.race604.drawable.wave.WaveDrawable;

import java.util.Calendar;
import java.util.TimeZone;

import ccn2279.a16031806a.nodrinknolife.sync.AlarmReceiver;
import ccn2279.a16031806a.nodrinknolife.sync.ReminderUtilities;
import ccn2279.a16031806a.nodrinknolife.utilities.CalculationUtils;
import ccn2279.a16031806a.nodrinknolife.utilities.SharedPreferencesUtils;

/**
 * Updated by Kiros Choi on 2018/04/11.
 */
public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String TAG = "Debug_NoDrinkNoLife";

    private ImageView healthBar_iv;
    private TextView healthValue_tv;
    private WaveDrawable mWaveDrawable;
    private SharedPreferences mSharedPreferences, mPreferences;

    // For the loop counter
    private int i;
    public boolean dialogIsShowing;

    // Create the Handler object (on the main thread by default)
    Handler handler = new Handler();
    // Define the code block to be executed
    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            // Update character health on the main thread
            try {
                CalculationUtils.updateHealth(MainActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Repeat this the same runnable code block again in another 90 seconds
            handler.postDelayed(runnableCode, 90000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //      Initialize View object      //
        healthBar_iv = findViewById(R.id.healthBar);
        healthValue_tv = findViewById(R.id.healthValue);

        mWaveDrawable = new WaveDrawable(this, R.drawable.health_bar);
        healthBar_iv.setImageDrawable(mWaveDrawable);
        mWaveDrawable.setIndeterminate(false);
        mWaveDrawable.setWaveAmplitude(5);
        mWaveDrawable.setWaveSpeed(3);
        //      End of initialization       //

        ReminderUtilities.scheduleChargingReminder(this, 60);
        int value = SharedPreferencesUtils.initSharedPreferences(this);
        Log.d(TAG, String.valueOf(value));

        mSharedPreferences = getSharedPreferences(SharedPreferencesUtils.PREFERENCE_NAME, MODE_PRIVATE);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        updateHealthUI(mSharedPreferences.getFloat(getString(R.string.character_health), (float) 0));

        registerAlarm();
        handler.post(runnableCode);

        // Ensure the character has respawned if it died
        boolean notEnoughWater = mSharedPreferences.getBoolean(getString(R.string.not_enough_water), false);
        boolean tooMuchWater = mSharedPreferences.getBoolean(getString(R.string.too_much_water), false);
        if (notEnoughWater) {
            updateHealthUI((float) 0.0);
            if (!dialogIsShowing) {
                DialogFragment fragment = new NotEnoughWaterDialogFragment();
                fragment.setCancelable(false);
                fragment.show(getFragmentManager(), "Dialog_one");
            }
        }
        if (tooMuchWater) {
            updateHealthUI((float) 200.0);
            if (!dialogIsShowing) {
                DialogFragment fragment = new TooMuchWaterDialogFragment();
                fragment.setCancelable(false);
                fragment.show(getFragmentManager(), "Dialog_two");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO: Back to the app, health no. is not update
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        mPreferences.registerOnSharedPreferenceChangeListener(this);
        try {
            CalculationUtils.updateHealth(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        handler.post(runnableCode);

        if (mSharedPreferences.getBoolean(getString(R.string.not_enough_water), false)) {
            onSharedPreferenceChanged(mSharedPreferences, getString(R.string.not_enough_water));
        }
        if (mSharedPreferences.getBoolean(getString(R.string.too_much_water), false)) {
            onSharedPreferenceChanged(mSharedPreferences, getString(R.string.too_much_water));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.setting) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.stat) {
            Intent intent = new Intent(this, StatActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.character_health))) {
            boolean notEnoughWater = mSharedPreferences.getBoolean(getString(R.string.not_enough_water), false);
            boolean tooMuchWater = mSharedPreferences.getBoolean(getString(R.string.too_much_water), false);
            if (!notEnoughWater && !tooMuchWater) {
                updateHealthUI(sharedPreferences.getFloat(key, (float) 0));
            }
        } else if (key.equals(getString(R.string.not_enough_water))) {
            if (sharedPreferences.getBoolean(key, true)) {
                updateHealthUI((float) 0.0);
                if (!dialogIsShowing) {
                    DialogFragment fragment = new NotEnoughWaterDialogFragment();
                    fragment.setCancelable(false);
                    fragment.show(getFragmentManager(), "Dialog_one");
                }
            } else {
                updateHealthUI(sharedPreferences.getFloat(getString(R.string.character_health), (float) 0));
            }
        } else if (key.equals(getString(R.string.too_much_water))) {
            if (sharedPreferences.getBoolean(key, true)) {
                updateHealthUI((float) 200.0);
                if (!dialogIsShowing) {
                    DialogFragment fragment = new TooMuchWaterDialogFragment();
                    fragment.setCancelable(false);
                    fragment.show(getFragmentManager(), "Dialog_two");
                }
            } else {
                updateHealthUI(sharedPreferences.getFloat(getString(R.string.character_health), (float) 0));
            }
        }
    }

    public void drinkWater(View view) {
        try {
            CalculationUtils.increaseDrankWater(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Update the health UI with new health number.
     *
     * @param health The new health of the character.
     */
    public void updateHealthUI(float health) {
        healthValue_tv.setText(String.format("%.1f%%", health));
        // Min: 2570; Max: 9990
        final int newLevel = (int) (2570 + (7420 * health / 100));
        final int originalLevel = mWaveDrawable.getLevel();
        int difference = originalLevel - newLevel;
        if (difference < 0) {
            difference = -difference;
        }
        Log.d(TAG, "Level: " + originalLevel + " N Lv: " + newLevel + " Diff: " + difference);

        // If the difference is too large, update the UI bar gradually
        if (difference < 350 || originalLevel == (float) 0) {
            mWaveDrawable.setLevel(newLevel);
            // Reload the Drawable
            mWaveDrawable.invalidateSelf();
        } else {
            (new Thread() {
                @Override
                public void run() {
                    Log.d(TAG, "Level: " + originalLevel + " N Lv: " + newLevel);
                    if (newLevel > originalLevel) {
                        for (i = originalLevel; i < newLevel; i++) {
                            try {
                                sleep(1);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mWaveDrawable.setLevel(i);
                                        mWaveDrawable.invalidateSelf();
                                    }
                                });
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        for (i = originalLevel; i > newLevel; i--) {
                            try {
                                sleep(1);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mWaveDrawable.setLevel(i);
                                        mWaveDrawable.invalidateSelf();
                                    }
                                });
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
        }
    }

    /**
     * Set an alarm on every midnight
     */
    public void registerAlarm() {
        // Set a pending intent to be executed
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.setAction(AlarmReceiver.ACTION_SAVE_DAILY_DRINKS);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set the specific time
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);

        // Repeat the alarm everyday
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
    }
}