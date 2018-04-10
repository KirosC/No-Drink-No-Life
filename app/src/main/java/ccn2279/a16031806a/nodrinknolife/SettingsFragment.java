package ccn2279.a16031806a.nodrinknolife;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Updated by Kiros Choi on 2018/04/11.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preference);
    }
}
