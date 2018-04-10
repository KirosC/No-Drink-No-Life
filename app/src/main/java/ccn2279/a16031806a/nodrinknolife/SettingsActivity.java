package ccn2279.a16031806a.nodrinknolife;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Updated by Kiros Choi on 2018/04/11.
 */
public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new ccn2279.a16031806a.nodrinknolife.SettingsFragment())
                .commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
