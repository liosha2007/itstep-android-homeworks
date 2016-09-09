package edu.android.arterialbloodpressure;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import edu.android.arterialbloodpressure.db.DBHelper;
import edu.android.arterialbloodpressure.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {
    public static DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new MainFragment(), "main")
                .commit();
    }
}
