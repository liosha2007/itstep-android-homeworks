package edu.android.homework_14;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * @author liosha (31.08.2016).
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
