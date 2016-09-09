package edu.android.homework_14.fragment;

import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import edu.android.homework_14.services.CharacteristicsService;

public class CharacteristicsFragment extends GenericFragment<MainActivity> implements View.OnClickListener {
    public static final String CHB_KEEP_KEY = "chb_keep";
    public static final String EDT_DELAY_KEY = "edt_delay";
    public static final String CHB_RESTART_KEY = "chb_restart";
    public static final String CHB_BOARD_KEY = "chb_board";
    public static final String CHB_BOOTLOADER_KEY = "chb_bootloader";
    public static final String CHB_BRAND_KEY = "chb_brand";
    public static final String CHB_DEVICE_KEY = "chb_device";
    public static final String CHB_DISPLAY_KEY = "chb_display";
    public static final String CHB_HARDWARE_KEY = "chb_hardware";
    public static final String CHB_ID_KEY = "chb_id";
    public static final String CHB_SERIAL_KEY = "chb_serial";
    public static final String CHB_CODENAME_KEY = "chb_codename";
    public static final String CHB_RELEASE_KEY = "chb_release";
    public static final String CHB_SDK_KEY = "chb_sdk";
    public static final String CHB_MODEL_KEY = "chb_model";

    private Button btn_start;
    private Button btn_stop;
    private Button btn_test;
    private TextView txb_status;
    private CheckBox chb_keep;
    private EditText edt_delay;
    private CheckBox chb_restart;

    private CheckBox chb_board;
    private CheckBox chb_bootloader;
    private CheckBox chb_brand;
    private CheckBox chb_device;
    private CheckBox chb_display;
    private CheckBox chb_hardware;
    private CheckBox chb_id;
    private CheckBox chb_serial;
    private CheckBox chb_base_os;
    private CheckBox chb_codename;
    private CheckBox chb_release;
    private CheckBox chb_sdk;
    private CheckBox chb_model;

    public CharacteristicsFragment() {
        super(R.layout.fragment_characteristics);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        (btn_start = view(R.id.btn_start)).setOnClickListener(this);
        (btn_stop = view(R.id.btn_stop)).setOnClickListener(this);
        (btn_test = view(R.id.btn_test)).setOnClickListener(this);
        txb_status = view(R.id.txb_status);
        (chb_keep = view(R.id.chb_keep)).setOnCheckedChangeListener(new CheckboxListener(sharedPreferences, CHB_KEEP_KEY));
        (edt_delay = view(R.id.txv_delay)).setOnFocusChangeListener(new EdittextListener(sharedPreferences, EDT_DELAY_KEY));
        (chb_restart = view(R.id.chb_restart)).setOnCheckedChangeListener(new CheckboxListener(sharedPreferences,  CHB_RESTART_KEY));

        (chb_board = view(R.id.chb_board)).setOnCheckedChangeListener(new CheckboxListener(sharedPreferences, CHB_BOARD_KEY));
        (chb_bootloader = view(R.id.chb_bootloader)).setOnCheckedChangeListener(new CheckboxListener(sharedPreferences, CHB_BOOTLOADER_KEY));
        (chb_brand = view(R.id.chb_brand)).setOnCheckedChangeListener(new CheckboxListener(sharedPreferences, CHB_BRAND_KEY));
        (chb_device = view(R.id.chb_device)).setOnCheckedChangeListener(new CheckboxListener(sharedPreferences, CHB_DEVICE_KEY));
        (chb_display = view(R.id.chb_display)).setOnCheckedChangeListener(new CheckboxListener(sharedPreferences,  CHB_DISPLAY_KEY));
        (chb_hardware = view(R.id.chb_hardware)).setOnCheckedChangeListener(new CheckboxListener(sharedPreferences, CHB_HARDWARE_KEY));
        (chb_id = view(R.id.chb_id)).setOnCheckedChangeListener(new CheckboxListener(sharedPreferences,  CHB_ID_KEY));
        (chb_serial = view(R.id.chb_serial)).setOnCheckedChangeListener(new CheckboxListener(sharedPreferences, CHB_SERIAL_KEY));
        (chb_codename = view(R.id.chb_codename)).setOnCheckedChangeListener(new CheckboxListener(sharedPreferences, CHB_CODENAME_KEY));
        (chb_release = view(R.id.chb_release)).setOnCheckedChangeListener(new CheckboxListener(sharedPreferences, CHB_RELEASE_KEY));
        (chb_sdk = view(R.id.chb_sdk)).setOnCheckedChangeListener(new CheckboxListener(sharedPreferences, CHB_SDK_KEY));
        (chb_model = view(R.id.chb_model)).setOnCheckedChangeListener(new CheckboxListener(sharedPreferences, CHB_MODEL_KEY));

        loadState();
    }

    private void loadState() {
        chb_keep.setChecked(sharedPreferences.getBoolean(CHB_KEEP_KEY, false));
        edt_delay.setText(sharedPreferences.getString(EDT_DELAY_KEY, "5"));
        chb_restart.setChecked(sharedPreferences.getBoolean(CHB_RESTART_KEY, false));

        chb_board.setChecked(sharedPreferences.getBoolean(CHB_BOARD_KEY, false));
        chb_bootloader.setChecked(sharedPreferences.getBoolean(CHB_BOOTLOADER_KEY, false));
        chb_brand.setChecked(sharedPreferences.getBoolean(CHB_BRAND_KEY, true));
        chb_device.setChecked(sharedPreferences.getBoolean(CHB_DEVICE_KEY, false));
        chb_display.setChecked(sharedPreferences.getBoolean( CHB_DISPLAY_KEY, false));
        chb_hardware.setChecked(sharedPreferences.getBoolean(CHB_HARDWARE_KEY, false));
        chb_id.setChecked(sharedPreferences.getBoolean(CHB_ID_KEY, false));
        chb_serial.setChecked(sharedPreferences.getBoolean(CHB_SERIAL_KEY, false));
        chb_codename.setChecked(sharedPreferences.getBoolean(CHB_CODENAME_KEY, false));
        chb_release.setChecked(sharedPreferences.getBoolean(CHB_RELEASE_KEY, false));
        chb_sdk.setChecked(sharedPreferences.getBoolean(CHB_SDK_KEY, false));
        chb_model.setChecked(sharedPreferences.getBoolean(CHB_MODEL_KEY, true));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                if (checkParameters()) {
                    startService();
                }
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
        Intent intent = new Intent(getActivity(), CharacteristicsService.class)
                .putExtra(MainActivity.CHECK_KEY, true);
        getActivity().startService(intent);
    }

    private void stopService() {
        getActivity().stopService(new Intent(getActivity().getBaseContext(), CharacteristicsService.class));
    }

    private void startService() {
        Intent intent = new Intent(getActivity(), CharacteristicsService.class)
                .putExtra(EDT_DELAY_KEY, sharedPreferences.getString(EDT_DELAY_KEY, "5"))
                .putExtra(CHB_KEEP_KEY, sharedPreferences.getBoolean(CHB_KEEP_KEY, false))
                .putExtra(CHB_RESTART_KEY, sharedPreferences.getBoolean(CHB_RESTART_KEY, false))

                .putExtra(CHB_BOARD_KEY, sharedPreferences.getBoolean(CHB_BOARD_KEY, false))
                .putExtra(CHB_BOOTLOADER_KEY, sharedPreferences.getBoolean(CHB_BOOTLOADER_KEY, false))
                .putExtra(CHB_BRAND_KEY, sharedPreferences.getBoolean(CHB_BRAND_KEY, false))
                .putExtra(CHB_DEVICE_KEY, sharedPreferences.getBoolean(CHB_DEVICE_KEY, false))
                .putExtra(CHB_DISPLAY_KEY, sharedPreferences.getBoolean(CHB_DISPLAY_KEY, false))
                .putExtra(CHB_HARDWARE_KEY, sharedPreferences.getBoolean(CHB_HARDWARE_KEY, false))
                .putExtra(CHB_ID_KEY, sharedPreferences.getBoolean(CHB_ID_KEY, false))
                .putExtra(CHB_SERIAL_KEY, sharedPreferences.getBoolean(CHB_SERIAL_KEY, false))
                .putExtra(CHB_CODENAME_KEY, sharedPreferences.getBoolean(CHB_CODENAME_KEY, false))
                .putExtra(CHB_RELEASE_KEY, sharedPreferences.getBoolean(CHB_RELEASE_KEY, false))
                .putExtra(CHB_SDK_KEY, sharedPreferences.getBoolean(CHB_SDK_KEY, false))
                .putExtra(CHB_MODEL_KEY, sharedPreferences.getBoolean(CHB_MODEL_KEY, false));
        getActivity().startService(intent);
        Toast.makeText(getActivity(), "Сервис запущен!", Toast.LENGTH_SHORT).show();
    }

    private boolean checkParameters() {
        final String delay = sharedPreferences.getString(EDT_DELAY_KEY, "5");
        if (delay.isEmpty()) {
            Toast.makeText(getActivity(), "Настройки не корректные!", Toast.LENGTH_SHORT).show();
            txb_status.setText("Невозможно запустить сервис!");
            return false;
        }
        return true;
    }
}
