package ccn2279.a16031806a.nodrinknolife;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.race604.drawable.wave.WaveDrawable;

import ccn2279.a16031806a.nodrinknolife.utilities.NotificationUtils;
import ccn2279.a16031806a.nodrinknolife.utilities.SharedPreferencesUtils;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String TAG = "NoDrinkNoLifeDebug";

    private ImageView healthBar_iv;
    private TextView healthValue_tv;
    private WaveDrawable mWaveDrawable;

    private SharedPreferences mSharedPreferences;

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
        updateHealthUI((float) 100);
        //      End of initialization       //

        //ReminderUtilities.scheduleChargingReminder(this);
        boolean value1 = SharedPreferencesUtils.getInstance(this);
        int value2 = SharedPreferencesUtils.initSharedPreferences(this);
        Log.d(TAG, String.valueOf(value1));
        Log.d(TAG, String.valueOf(value2));

        mSharedPreferences = getSharedPreferences(SharedPreferencesUtils.PREFERENCE_NAME, MODE_PRIVATE);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
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
            Intent intent = new Intent(this, ccn2279.a16031806a.nodrinknolife.SettingsActivity.class);
            startActivity(intent);
        }
        return true;
    }

    public void testNotification(View view) {
        NotificationUtils.remindUserToDrink(this);
    }

    public void updateHealthUI(float health) {
        healthValue_tv.setText(String.format("%.1f%%", health));
        // Min: 2570; Max: 9990
        health = 2570 + (7420 * health / 100);
        mWaveDrawable.setLevel((int) health);
        mWaveDrawable.invalidateSelf();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG, key);
        if (key.equals(getString(R.string.character_health))) {
            updateHealthUI(sharedPreferences.getFloat(key, (float) 0));
        }
    }
}
