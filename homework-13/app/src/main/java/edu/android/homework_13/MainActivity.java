package edu.android.homework_13;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, FileChooser.FileSelectedListener {
    private EditText edt_left;
    private RadioButton rdb_plus;
    private RadioButton rdb_minus;
    private RadioButton rdb_multiply;
    private RadioButton rdb_divide;
    private EditText edt_right;
    private EditText edt_result;
    private Button btn_add;
    private Button btn_file;
    private Button btn_calculate;
    private ListView lsv_sentences;
    private String operation = "+";
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt_left = view(R.id.edt_left);
        (rdb_plus = view(R.id.rdb_plus)).setOnClickListener(this);
        (rdb_minus = view(R.id.rdb_minus)).setOnClickListener(this);
        (rdb_multiply = view(R.id.rdb_multiply)).setOnClickListener(this);
        (rdb_divide = view(R.id.rdb_divide)).setOnClickListener(this);
        edt_right = view(R.id.edt_right);
        edt_result = view(R.id.edt_result);
        (btn_add = view(R.id.btn_add)).setOnClickListener(this);
        (btn_file = view(R.id.btn_file)).setOnClickListener(this);
        (btn_calculate = view(R.id.btn_calculate)).setOnClickListener(this);
        lsv_sentences = view(R.id.lsv_sentences);

        rdb_plus.setChecked(true);
        lsv_sentences.setAdapter(adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1));
    }


    private void calculateResults() {
        final int count = adapter.getCount();
        for (int n = 0; n < count; n++) {
            final String item = adapter.getItem(n);
            new CalculateTask(this).execute(item);
        }
        adapter.clear();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                checkList();
                final String left = edt_left.getText().toString();
                final String right = edt_right.getText().toString();
                final String result = edt_result.getText().toString();
                if (TextUtils.isEmpty(left) || TextUtils.isEmpty(right) || TextUtils.isEmpty(result)) {
                    Toast.makeText(this, "Нужно заполнить все поля!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (    ("x".equals(left.toLowerCase()) || left.matches("[\\d\\.]+")) &&
                        ("x".equals(right.toLowerCase()) || right.matches("[\\d\\.]+")) &&
                        ("x".equals(result.toLowerCase()) || result.matches("[\\d\\.]+"))) {
                    adapter.add(left + " " + operation + " " + right + " = " + result);
                    adapter.notifyDataSetChanged();
                    return;
                }
                Toast.makeText(this, "Допустимы числа или 'X'!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_calculate:
                calculateResults();
                break;
            case R.id.btn_file:
                checkList();
                int permsRequestCode = 200;
                String[] perms = {"android.permission.READ_EXTERNAL_STORAGE"};
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1 && !isPermissionGranted(perms)) {
                    requestPermissions(perms, permsRequestCode);
                } else {
                    onPermissionGranted();
                }
                break;
            default:
                if (view instanceof CompoundButton) {
                    final CompoundButton compoundButton = (CompoundButton) view;
                    if (compoundButton.isChecked()) {
                        operation = compoundButton.getText().toString();
                    }
                }
                break;
        }
    }

    private void onPermissionGranted() {
        FileChooser chooser = new FileChooser(this);
        chooser.setExtension(".txt");
        chooser.setFileListener(this);
        chooser.showDialog();
    }

    private void checkList() {
        final int count = adapter.getCount();
        for (int n = 0; n < count; n++) {
            final String item = adapter.getItem(n);
            if (item.contains(",")) {
                adapter.clear();
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean isPermissionGranted(String[] perms) {
        for (String perm : perms) {
            if (checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
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
                Toast.makeText(this, "Не хватает прав для запуска сервиса!", Toast.LENGTH_SHORT).show();
        }
    }
    @SuppressWarnings("unchecked")
    public <T> T view(final int viewId) {
        try {
            return (T) findViewById(viewId);
        } catch (ClassCastException e) {
            Log.e("MY", "Can't cast view to target type! View ID: " + viewId);
        }
        return null;
    }

    public void onSentenceCalculated(String sentence) {
        adapter.add(sentence);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void fileSelected(File file) {
        try {
            FileInputStream inputStream = new FileInputStream(file);
            final String[] sentences = IOUtils.toString(inputStream, "UTF-8").split("[\r\n]+");
            for (String sentence : sentences) {
                if (sentence.trim().length() == 0) {
                    continue;
                }
                try {
                    if (isIncorrectSentence(sentence)) {
                        throw new Exception("Sentence is incorrect!");
                    }
                    adapter.add(sentence);
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e("MY", "Can't parse sentence!", e);
                }
            }

        } catch (Exception e) {
            Toast.makeText(this, "Can't parse file!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isIncorrectSentence(String sentence) {
        return !sentence.matches("[xX\\d\\. ]+[\\+\\-\\*/ ]+[xX\\d\\. ]+[= ]+[xX\\d\\. ]+");
    }
}
