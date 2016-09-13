package edu.android.homework_15;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import edu.android.homework_15.fragment.GameFragment;
import edu.android.homework_15.fragment.MenuFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_content, new MenuFragment())
                .commit();
    }

    public void onStartGame() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_content, new GameFragment())
                .addToBackStack(null)
                .commit();
    }
}
