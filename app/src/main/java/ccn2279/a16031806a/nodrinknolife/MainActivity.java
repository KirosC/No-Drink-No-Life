package ccn2279.a16031806a.nodrinknolife;

import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.race604.drawable.wave.WaveDrawable;

import java.util.Calendar;
import java.util.TimeZone;

import ccn2279.a16031806a.nodrinknolife.sync.AlarmReceiver;
import ccn2279.a16031806a.nodrinknolife.sync.ReminderUtilities;
import ccn2279.a16031806a.nodrinknolife.utilities.CalculationUtils;
import ccn2279.a16031806a.nodrinknolife.utilities.SharedPreferencesUtils;
import pl.droidsonroids.gif.GifImageView;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

/**
 * Updated by Kiros Choi on 2018/04/11.
 */
public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String TAG = "Debug_NoDrinkNoLife";

    private ImageView healthBar_iv;
    private WaveDrawable mWaveDrawable;
    private SharedPreferences mSharedPreferences, mPreferences;
    public boolean characterIsDead;
    private FrameLayout healthFrame;
    private TextView healthValue_tv, characterStatus_tv;
    private GifImageView characterGif;
    FloatingActionButton fAB;

    private Menu menu;

    // Define the code block to be executed
    Runnable runnableCode = new Runnable() {
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

    // For the loop counter
    private int i;

    // Create the Handler object (on the main thread by default)
    Handler handler = new Handler();
    private int characterStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //      Initialize View object      //
        healthBar_iv = findViewById(R.id.healthBar);
        healthValue_tv = findViewById(R.id.healthValue);
        healthFrame = findViewById(R.id.healthFrame);
        characterStatus_tv = findViewById(R.id.statusText);
        characterGif = findViewById(R.id.characterImage);
        fAB = findViewById(R.id.drinkButton);

        mWaveDrawable = new WaveDrawable(this, R.drawable.health_bar);
        healthBar_iv.setImageDrawable(mWaveDrawable);
        mWaveDrawable.setIndeterminate(false);
        mWaveDrawable.setWaveAmplitude(5);
        mWaveDrawable.setWaveSpeed(3);
        //      End of initialization       //

        ReminderUtilities.scheduleChargingReminder(this, 60);
        int initializationValue = SharedPreferencesUtils.initSharedPreferences(this);
        Log.d(TAG, String.valueOf(initializationValue));

        // Start the feature discovery if it is the first time use
        if (initializationValue == 1) {
            // For the Drink Button
            new MaterialTapTargetPrompt.Builder(MainActivity.this)
                    .setTarget(fAB)
                    .setBackgroundColour(getResources().getColor(R.color.colorSink))
                    .setPrimaryText(R.string.intro_title_drink)
                    .setSecondaryText(R.string.intro_body_drink)
                    .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                        @Override
                        public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state) {
                            // For the Health UI
                            if (state == MaterialTapTargetPrompt.STATE_DISMISSED) {
                                new MaterialTapTargetPrompt.Builder(MainActivity.this)
                                        .setTarget(healthFrame)
                                        .setBackgroundColour(getResources().getColor(R.color.colorSink))
                                        .setPromptBackground(new RectanglePromptBackground())
                                        .setPromptFocal(new RectanglePromptFocal())
                                        .setPrimaryText(R.string.intro_title_health)
                                        .setSecondaryText(R.string.intro_body_health)
                                        .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                                            @Override
                                            public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state) {
                                                // For the Stat. Button
                                                if (state == MaterialTapTargetPrompt.STATE_DISMISSED) {
                                                    // Set the Stat. icon to black temporarily
                                                    Drawable drawable = menu.findItem(R.id.stat).getIcon();
                                                    drawable = DrawableCompat.wrap(drawable);
                                                    DrawableCompat.setTint(drawable, ContextCompat.getColor(MainActivity.this, R.color.black));
                                                    menu.findItem(R.id.stat).setIcon(drawable);
                                                    final android.support.v7.widget.Toolbar toolbar = findViewById(android.support.v7.appcompat.R.id.action_bar);
                                                    final View child = toolbar.getChildAt(1);
                                                    final android.support.v7.widget.ActionMenuView actionMenuView = (android.support.v7.widget.ActionMenuView) child;
                                                    final MaterialTapTargetPrompt.Builder builder = new MaterialTapTargetPrompt.Builder(MainActivity.this)
                                                            .setPrimaryText(R.string.intro_title_stat)
                                                            .setFocalColour(getResources().getColor(R.color.white))
                                                            .setSecondaryText(R.string.intro_body_stat);
                                                    builder.setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                                                        @Override
                                                        public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt, int state) {
                                                            if (state == MaterialTapTargetPrompt.STATE_DISMISSED) {
                                                                Drawable drawable = menu.findItem(R.id.stat).getIcon();
                                                                drawable = DrawableCompat.wrap(drawable);
                                                                DrawableCompat.setTint(drawable, ContextCompat.getColor(MainActivity.this, R.color.white));
                                                                menu.findItem(R.id.stat).setIcon(drawable);
                                                            }
                                                        }
                                                    });
                                                    builder.setTarget(actionMenuView.getChildAt(actionMenuView.getChildCount() - 2));
                                                    builder.show();
                                                }
                                            }
                                        })
                                        .show();
                            }
                        }
                    })
                    .show();
        }

        mSharedPreferences = getSharedPreferences(SharedPreferencesUtils.PREFERENCE_NAME, MODE_PRIVATE);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        updateHealthUI(mSharedPreferences.getFloat(getString(R.string.character_health), (float) 0));
        updateCharacterImage(mSharedPreferences.getFloat(getString(R.string.character_health), (float) 0));

        // Schedule task
        registerAlarm();
        // Start update Health UI continuously
        handler.post(runnableCode);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        mPreferences.registerOnSharedPreferenceChangeListener(this);

        // Calculate the character's health since last leave
        try {
            CalculationUtils.updateHealth(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        handler.post(runnableCode);

        // Check if the character is dead
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
        mPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop update Health UI continuously
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        this.menu = menu;
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
        } else if (id == R.id.about) {
            Intent intent = new Intent(this, AboutActivity.class);
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
            updateCharacterImage(sharedPreferences.getFloat(key, (float) 0));
        } else if (key.equals(getString(R.string.not_enough_water))) {
            // Change the character GIF and Drink Button
            if (sharedPreferences.getBoolean(key, true)) {
                updateHealthUI((float) 0.0);

                characterGif.setImageResource(R.drawable.gif_human_death);
                characterGif.setBackgroundColor(getResources().getColor(R.color.colorThirsty));
                characterGif.setImageTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.black));
                characterGif.refreshDrawableState();
                characterStatus_tv.setText(getString(R.string.status_rip));
                characterStatus_tv.setTextColor(getResources().getColor(R.color.textDarkPrimary));
                healthValue_tv.setTextColor(getResources().getColor(R.color.textDarkPrimary));

                fAB.setImageResource(R.drawable.ic_respawn);
                fAB.setScaleType(ImageView.ScaleType.CENTER);
                characterIsDead = true;
                handler.removeCallbacksAndMessages(null);
            } else {
                updateHealthUI(sharedPreferences.getFloat(getString(R.string.character_health), (float) 0));
            }
        } else if (key.equals(getString(R.string.too_much_water))) {
            // Change the character GIF and Drink Button
            if (sharedPreferences.getBoolean(key, true)) {
                updateHealthUI((float) 200.0);

                characterGif.setImageResource(R.drawable.gif_human_death);
                characterGif.setBackgroundColor(getResources().getColor(R.color.colorThirsty));
                characterGif.setImageTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.black));
                characterGif.refreshDrawableState();
                characterStatus_tv.setText(getString(R.string.status_rip));
                characterStatus_tv.setTextColor(getResources().getColor(R.color.textDarkPrimary));
                healthValue_tv.setTextColor(getResources().getColor(R.color.textDarkPrimary));

                fAB.setImageResource(R.drawable.ic_respawn);
                fAB.setScaleType(ImageView.ScaleType.CENTER);
                characterIsDead = true;
                handler.removeCallbacksAndMessages(null);
            } else {
                updateHealthUI(sharedPreferences.getFloat(getString(R.string.character_health), (float) 0));
            }
        }
    }

    public void drinkWaterOrRespawn(View view) {
        if (!characterIsDead) {
            try {
                CalculationUtils.increaseDrankWater(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (mSharedPreferences.getBoolean(getString(R.string.not_enough_water), false)) {
                // Disable the Button and show the respawn dialog if the character is dead
                fAB.setClickable(false);
                DialogFragment fragment = new NotEnoughWaterDialogFragment();
                fragment.setCancelable(false);
                fragment.show(getFragmentManager(), "Dialog_not_enough_water");
            } else {
                fAB.setClickable(false);
                DialogFragment fragment = new TooMuchWaterDialogFragment();
                fragment.setCancelable(false);
                fragment.show(getFragmentManager(), "Dialog_too_much_water");
            }
        }
    }

    public void respawn(View view) {

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
        int temp = mWaveDrawable.getLevel();
        if (temp > (float) 9990) {
            temp = 9990;
        }
        final int originalLevel = temp;

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
     * Update different character image with corresponding health.
     *
     * @param health The current character health value.
     */
    private void updateCharacterImage(float health) {
        if (health >= 150 && health < 199.95) {
            if (characterStatus == 1) {
                return;
            }
            characterGif.setImageResource(R.drawable.gif_human_sink);
            characterGif.setBackgroundColor(getResources().getColor(R.color.colorSink));
            characterGif.setImageTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.black));
            characterGif.refreshDrawableState();
            characterStatus_tv.setText(getString(R.string.status_sink));
            characterStatus_tv.setTextColor(getResources().getColor(R.color.textLightPrimary));
            healthValue_tv.setTextColor(getResources().getColor(R.color.textLightPrimary));
            characterStatus = 1;
        } else if (health > 50 && health < 150) {
            if (characterStatus == 2) {
                return;
            }
            characterGif.setImageResource(R.drawable.gif_human_swim);
            characterGif.setBackgroundColor(getResources().getColor(R.color.colorSwim));
            characterGif.setImageTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.colorTintSwim));
            characterGif.refreshDrawableState();
            characterStatus_tv.setText(getString(R.string.status_swim));
            characterStatus_tv.setTextColor(getResources().getColor(R.color.textDarkPrimary));
            healthValue_tv.setTextColor(getResources().getColor(R.color.textDarkPrimary));
            characterStatus = 2;
        } else if (health > 0.04 && health <= 50) {
            if (characterStatus == 3) {
                return;
            }
            characterGif.setImageResource(R.drawable.gif_human_thirsty);
            characterGif.setBackgroundColor(getResources().getColor(R.color.colorThirsty));
            characterGif.setImageTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.black));
            characterGif.refreshDrawableState();
            characterStatus_tv.setText(getString(R.string.status_thirsty));
            characterStatus_tv.setTextColor(getResources().getColor(R.color.textDarkPrimary));
            healthValue_tv.setTextColor(getResources().getColor(R.color.textDarkPrimary));
            characterStatus = 3;
        }
    }

    /**
     * Set an schedule alarm on every midnight.
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
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}