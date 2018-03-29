package ccn2279.a16031806a.nodrinknolife;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import ccn2279.a16031806a.nodrinknolife.sync.ReminderUtilities;
import ccn2279.a16031806a.nodrinknolife.utilities.NotificationUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ReminderUtilities.scheduleChargingReminder(this);
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
