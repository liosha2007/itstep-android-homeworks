package edu.android.arterialbloodpressure.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.android.arterialbloodpressure.MainActivity;
import edu.android.arterialbloodpressure.R;
import edu.android.arterialbloodpressure.entity.Pressure;
import edu.android.arterialbloodpressure.tasks.InsertTask;
import edu.android.arterialbloodpressure.tasks.SelectTask;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends GenericFragment<MainActivity> implements View.OnClickListener, DatePicker.OnDateChangedListener, View.OnLongClickListener {
    private TimePicker tmp_time;
    private Button btn_add;
    private EditText edt_pressure_up;
    private EditText edt_pressure_down;
    private DatePicker dtp_date;
    private ListView lsv_results;

    public MainFragment() {
        super(R.layout.fragment_main);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tmp_time = view(R.id.tmp_time);
        (btn_add = view(R.id.btn_add)).setOnClickListener(this);
        edt_pressure_up = view(R.id.edt_pressure_up);
        edt_pressure_down = view(R.id.edt_pressure_down);
        dtp_date = view(R.id.dtp_date);
        lsv_results = view(R.id.lsv_results);

        Calendar calendar = Calendar.getInstance();
        dtp_date.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), this);
        selectData();

        btn_add.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                insertData();
                break;
        }
    }

    private void insertData() {
        insertData(Calendar.getInstance());
    }
    private void insertData(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, tmp_time.getCurrentHour());
        calendar.set(Calendar.MINUTE, tmp_time.getCurrentMinute());
        final String up = edt_pressure_up.getText().toString();
        final String down = edt_pressure_down.getText().toString();
        if (!up.isEmpty() && TextUtils.isDigitsOnly(up) && !down.isEmpty() && TextUtils.isDigitsOnly(down)) {
                new InsertTask(this).execute(new Pressure(calendar.getTime(), Integer.parseInt(up), Integer.parseInt(down)));
                edt_pressure_up.setText("");
                edt_pressure_down.setText("");
        } else {
                onInsertCancelled();
        }
    }

    public void onDataInserted() {
        Toast.makeText(getActivity(), "Данные сохранены!", Toast.LENGTH_SHORT).show();
        selectData();
    }

    private void selectData() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, dtp_date.getDayOfMonth());
        calendar.set(Calendar.MONTH, dtp_date.getMonth());
        calendar.set(Calendar.YEAR, dtp_date.getYear());
        new SelectTask(this).execute(calendar.getTime());
    }

    public void onInsertCancelled() {
        Toast.makeText(getActivity(), "Не удалось сохранить данные!", Toast.LENGTH_SHORT).show();
    }

    public void onDataSelected(List<Pressure> pressures) {
        if (pressures.size() > 0) {
            final Pressure[] strings = new Pressure[pressures.size()];
            pressures.toArray(strings);
            lsv_results.setAdapter(new ArrayAdapter<Pressure>(getActivity(), android.R.layout.simple_list_item_1, strings));
        } else {
            lsv_results.setAdapter(new ArrayAdapter<Pressure>(getActivity(), android.R.layout.simple_list_item_1));
        }
    }

    public void onSelectCancelled() {
        Toast.makeText(getActivity(), "Не удалось получить данные!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        selectData();
    }

    @Override
    public boolean onLongClick(View v) {
        final Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DAY_OF_MONTH, -1);
        insertData(instance);
        return true;
    }
}