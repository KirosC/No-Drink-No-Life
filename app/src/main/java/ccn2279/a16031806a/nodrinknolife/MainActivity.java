package ccn2279.a16031806a.nodrinknolife;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import ccn2279.a16031806a.nodrinknolife.sync.ReminderUtilities;
import ccn2279.a16031806a.nodrinknolife.utilities.NotificationUtils;
import ccn2279.a16031806a.nodrinknolife.utilities.SharedPreferencesUtils;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "NoDrinkNoLifeDebug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ReminderUtilities.scheduleChargingReminder(this);
        boolean value1 = SharedPreferencesUtils.getInstance(this);
        int value2 = SharedPreferencesUtils.initSharedPreferences(this);
        Log.d(TAG, String.valueOf(value1));
        Log.d(TAG, String.valueOf(value2));
        SharedPreferencesUtils.setValue(this, getString(R.string.today_day), 7);
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
}
