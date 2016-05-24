package edu.android.homework_05;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RemoteViews;

public class MainActivity extends GenericActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    public static final String IS_DARK_KEY = "IS_DARK";
    public static final String IS_LEFT_KEY = "IS_LEFT";
    public static final String IS_RIGHT_KEY = "IS_RIGHT";

    protected ImageView imv_case;
    protected RadioButton rdb_light;
    protected RadioButton rdb_dark;
    protected CheckBox chb_left;
    protected CheckBox chb_right;
    protected Button btn_save;

    private int caseId = R.drawable.case_dark_no_sides;
    private boolean isDark = true;
    private boolean isLeft = true;
    private boolean isRight = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        final Intent intent = getIntent();
//        isDark = intent.getBooleanExtra(IS_DARK_KEY, true);
//        isLeft = intent.getBooleanExtra(IS_LEFT_KEY, true);
//        isRight = intent.getBooleanExtra(IS_RIGHT_KEY, true);

        imv_case = view(R.id.imv_case);
        (rdb_light = view(R.id.rdb_light)).setOnCheckedChangeListener(this);
        (rdb_dark = view(R.id.rdb_dark)).setOnCheckedChangeListener(this);
        (chb_left = view(R.id.chb_left)).setOnCheckedChangeListener(this);
        (chb_right = view(R.id.chb_right)).setOnCheckedChangeListener(this);
        (btn_save = view(R.id.btn_save)).setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.rdb_light:
                isDark = !isChecked;
                break;
            case R.id.rdb_dark:
                isDark = isChecked;
                break;
            case R.id.chb_left:
                isLeft = isChecked;
                break;
            case R.id.chb_right:
                isRight = isChecked;
                break;
        }
        updatePreview();
    }

    private void updatePreview() {
        if (isDark) {
            if (isLeft && isRight) {
                caseId = R.drawable.case_dark_both_sides;
            } else if (isLeft) {
                caseId = R.drawable.case_dark_left_side;
            } else if (isRight) {
                caseId = R.drawable.case_dark_right_side;
            } else {
                caseId = R.drawable.case_dark_no_sides;
            }
        } else {
            if (isLeft && isRight) {
                caseId = R.drawable.case_light_both_sides;
            } else if (isLeft) {
                caseId = R.drawable.case_light_left_side;
            } else if (isRight) {
                caseId = R.drawable.case_light_right_side;
            } else {
                caseId = R.drawable.case_light_no_sides;
            }
        }
        imv_case.setImageResource(caseId);
    }

    @Override
    public void onClick(View v) {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.widget_case);
        remoteViews.setImageViewResource(R.id.imv_case, caseId);

        ComponentName componentName = new ComponentName(this, CaseWidgetProvider.class);
        AppWidgetManager.getInstance(this).updateAppWidget(componentName, remoteViews);

        Intent intent = new Intent(CaseWidgetProvider.ACTION_CASE_CHANGED);
        intent.putExtra(IS_DARK_KEY, isDark);
        intent.putExtra(IS_LEFT_KEY, isLeft);
        intent.putExtra(IS_RIGHT_KEY, isRight);
        getApplicationContext().sendBroadcast(intent);
        Log.d("MY", "Values sent!");

        finish();
    }
}
