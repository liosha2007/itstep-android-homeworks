package edu.sample.homework02;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends GenericActivity
        implements View.OnClickListener {

    private Button btnStartTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        btnStartTest = view(R.id.btn_start_test);
        btnStartTest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_test:
                onMyStartTestClicked();
                break;
        }
    }

    private void onMyStartTestClicked() {
        Intent toA = new Intent(this, ActivityA.class);
        startActivity(toA);
    }
}
