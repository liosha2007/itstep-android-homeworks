package edu.android.homework_14.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.android.homework_14.MainActivity;
import edu.android.homework_14.R;
import edu.android.homework_14.listeners.CheckboxListener;
import edu.android.homework_14.listeners.EdittextListener;
import edu.android.homework_14.services.BenchmarkService;

public class BenchmarkFragment extends GenericFragment<MainActivity> implements View.OnClickListener {
    public static final String CHB_KEEP_KEY = "chb_keep";
    public static final String EDT_COUNTER_KEY = "edt_counter";
    public static final String EDT_REPEAT_KEY = "edt_repeat";
    public static final String EDT_DELAY_KEY = "edt_delay";
    public static final String CHB_RESTART_KEY = "chb_restart";
    private Button btn_start;
    private Button btn_stop;
    private Button btn_test;
    private TextView txb_status;
    private CheckBox chb_keep;
    private EditText edt_counter;
    private EditText edt_repeat;
    private EditText edt_delay;
    private CheckBox chb_restart;

    public BenchmarkFragment() {
        super(R.layout.fragment_benchmark);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        (btn_start = view(R.id.btn_start)).setOnClickListener(this);
        (btn_stop = view(R.id.btn_stop)).setOnClickListener(this);
        (btn_test = view(R.id.btn_test)).setOnClickListener(this);
        txb_status = view(R.id.txb_status);
        (chb_keep = view(R.id.chb_keep)).setOnCheckedChangeListener(new CheckboxListener(sharedPreferences, CHB_KEEP_KEY));
        (edt_counter = view(R.id.edt_counter)).setOnFocusChangeListener(new EdittextListener(sharedPreferences, EDT_COUNTER_KEY));
        (edt_repeat = view(R.id.edt_repeat)).setOnFocusChangeListener(new EdittextListener(sharedPreferences, EDT_REPEAT_KEY));
        (edt_delay = view(R.id.txv_delay)).setOnFocusChangeListener(new EdittextListener(sharedPreferences, EDT_DELAY_KEY));
        (chb_restart = view(R.id.chb_restart)).setOnCheckedChangeListener(new CheckboxListener(sharedPreferences, CHB_RESTART_KEY));

        loadState();
    }

    private void loadState() {
        chb_keep.setChecked(sharedPreferences.getBoolean(CHB_KEEP_KEY, false));
        edt_counter.setText(sharedPreferences.getString(EDT_COUNTER_KEY, "10000"));
        edt_repeat.setText(sharedPreferences.getString(EDT_REPEAT_KEY, "5"));
        edt_delay.setText(sharedPreferences.getString(EDT_DELAY_KEY, "5"));
        chb_restart.setChecked(sharedPreferences.getBoolean(CHB_RESTART_KEY, false));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                startService();
                break;
            case R.id.btn_stop:
                stopService();
                break;
            case R.id.btn_test:
                testNotification();
                break;
        }
    }

    private void testNotification() {
        Intent intent = new Intent(getActivity(), BenchmarkService.class)
                .putExtra(MainActivity.CHECK_KEY, true);
        getActivity().startService(intent);

    }

    private void stopService() {
        getActivity().stopService(new Intent(getActivity().getBaseContext(), BenchmarkService.class));
    }

    private void startService() {
        int permsRequestCode = 200;
        String[] perms = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1 && !isPermissionGranted(perms)) {
            requestPermissions(perms, permsRequestCode);
        } else {
            onPermissionGranted();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean isPermissionGranted(String[] perms) {
        for (String perm : perms) {
            if (getActivity().checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void onPermissionGranted() {
        Intent intent = new Intent(getActivity(), BenchmarkService.class)
                .putExtra(EDT_DELAY_KEY, sharedPreferences.getString(EDT_DELAY_KEY, "5"))
                .putExtra(CHB_KEEP_KEY, sharedPreferences.getBoolean(CHB_KEEP_KEY, false))
                .putExtra(CHB_RESTART_KEY, sharedPreferences.getBoolean(CHB_RESTART_KEY, false))

                .putExtra(EDT_COUNTER_KEY, sharedPreferences.getString(EDT_COUNTER_KEY, "10000"))
                .putExtra(EDT_REPEAT_KEY, sharedPreferences.getString(EDT_REPEAT_KEY, "5"));
        getActivity().startService(intent);
        Toast.makeText(getActivity(), "Сервис запущен!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch(permsRequestCode){
            case 200:
                boolean readAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean writeAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (readAccepted && writeAccepted) {
                    onPermissionGranted();
                }
                break;
            default:
                Toast.makeText(getActivity(), "Не хватает прав для запуска сервиса!", Toast.LENGTH_SHORT).show();
        }
    }
}
