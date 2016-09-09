package edu.android.homework_14;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import edu.android.homework_14.fragment.BenchmarkFragment;
import edu.android.homework_14.fragment.CharacteristicsFragment;
import edu.android.homework_14.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {
    public static final String SHOW_CHARACTERISTICS = "SHOW_CHARACTERISTICS";
    public static final String CHECK_KEY = "CHECK_KEY";
    public static final String BM_YAG = "bm";
    public static final String CH_TAG = "ch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent intent = getIntent();
        if (intent != null) {
            final String type = intent.getStringExtra(SHOW_CHARACTERISTICS);
            if (type != null && CH_TAG.equals(type)) {
                openCharacteristics();
                return;
            } else if (type != null && BM_YAG.equals(type)) {
                openBenchmark();
                return;
            }
        }

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new MainFragment(), "main")
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.mnu_char:
                openCharacteristics();
                return true;
            case R.id.mnu_bench:
                openBenchmark();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @SuppressWarnings("unchecked")
    public <T> T view(final int viewId) {
        try {
            return (T) findViewById(viewId);
        } catch (ClassCastException e) {
            Log.e("MY", "Can't cast view to target type! View ID: " + viewId);
        } catch (IllegalArgumentException e) {
            Log.e("MY", "Can't get view!");
        }
        return null;
    }

    public void openBenchmark() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new BenchmarkFragment(), BM_YAG)
                .addToBackStack(null)
                .commit();
    }

    public void openCharacteristics() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new CharacteristicsFragment(), CH_TAG)
                .addToBackStack(null)
                .commit();
    }


}
