package edu.android.homework_14.listeners;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;

/**
 * @author liosha (03.09.2016).
 */
public class EdittextListener implements View.OnFocusChangeListener {
    private final SharedPreferences sharedPreferences;
    private final String key;

    public EdittextListener(SharedPreferences sharedPreferences, String key) {
        this.sharedPreferences = sharedPreferences;
        this.key = key;
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (!hasFocus) {
            sharedPreferences.edit().putString(key, ((EditText) view).getText().toString()).apply();
        }
    }
}
