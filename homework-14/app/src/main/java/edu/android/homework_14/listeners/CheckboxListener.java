package edu.android.homework_14.listeners;

import android.content.SharedPreferences;
import android.widget.CompoundButton;

/**
 * @author liosha (03.09.2016).
 */
public class CheckboxListener implements CompoundButton.OnCheckedChangeListener {
    private final SharedPreferences sharedPreferences;
    private final String key;

    public CheckboxListener(SharedPreferences sharedPreferences, String key) {
        this.sharedPreferences = sharedPreferences;
        this.key = key;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        sharedPreferences.edit().putBoolean(key, checked).apply();
    }
}
