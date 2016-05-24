package edu.sample.homework03;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by liosha
 */
public class GenericActivity extends AppCompatActivity {

    @SuppressWarnings("unchecked")
    public <T> T view(int viewId) {
        try {
            return (T)findViewById(viewId);
        } catch (ClassCastException e) {
            Log.e("MY", "Can't cast view (" + viewId + ") to target tipe! " + e.getMessage());
        }
        return null;
    }

    public String string(int stringId) {
        return getResources().getString(stringId);
    }
}
