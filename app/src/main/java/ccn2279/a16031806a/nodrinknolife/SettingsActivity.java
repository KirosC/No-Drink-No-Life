package ccn2279.a16031806a.nodrinknolife;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import ccn2279.a16031806a.nodrinknolife.sync.ReminderUtilities;

/**
 * Updated by Kiros Choi on 2018/04/11.
 */
public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private SharedPreferences mSharePreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new ccn2279.a16031806a.nodrinknolife.SettingsFragment())
                .commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSharePreference = PreferenceManager.getDefaultSharedPreferences(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSharePreference.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSharePreference.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("pref_notification")) {
            // Reschedule notification interval
            ReminderUtilities.scheduleChargingReminder(this, Long.parseLong(sharedPreferences.getString("pref_notification", "0")));
        }
    }
}
