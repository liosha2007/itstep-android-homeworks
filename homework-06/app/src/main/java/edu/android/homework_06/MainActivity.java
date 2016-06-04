package edu.android.homework_06;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements DialogInterface.OnClickListener, View.OnClickListener {

    private int currentValue = 0;
    private int commonValue = 0;
    private Random random = new Random(GregorianCalendar.getInstance().getTimeInMillis());
    private int[] icons = {
            R.drawable.question_1,
            R.drawable.question_2,
            R.drawable.question_3,
            R.drawable.question_4,
            R.drawable.question_5,
            R.drawable.question_6,
            R.drawable.question_7
    };

    private ImageView imv_result;
    private TextView txv_result;
    private Iterator<Map.Entry<String, String>> currentEntry = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imv_result = view(R.id.imv_result);
        txv_result = view(R.id.txv_result);
        this.<Button>view(R.id.btn_start).setOnClickListener(this);
    }

    private void startTest() {
        imv_result.setImageDrawable(null);
        txv_result.setText("");
        Map<String, String> hashMapResource = getHashMapResource(this, R.xml.self_assessment);
        if (hashMapResource != null) {
            this.currentEntry = hashMapResource.entrySet().iterator();
            showQuestion();
        }

    }

    private void showQuestion() {

        if (currentEntry.hasNext()) {
            Map.Entry<String, String> entry = currentEntry.next();
            this.currentValue = Integer.parseInt(entry.getValue());
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog alertDialog = builder
                    .setMessage(entry.getKey())
                    .setCancelable(false) // Не закрывать по клику вне диалога
                    .setPositiveButton("Верно", this)
                    .setNegativeButton("Неверно", this)
                    .setNeutralButton("Остановить тест", this)
                    .setTitle(R.string.app_name)
                    .setIcon(icons[random.nextInt(6)])
                    .create();
            alertDialog.show();
        } else {
            if (commonValue <= -4) {
                imv_result.setImageResource(R.drawable.low_self_assessment);
                txv_result.setText(R.string.low_self_assessment);
            } else if (commonValue >= 4) {
                imv_result.setImageResource(R.drawable.high_self_assessment);
                txv_result.setText(R.string.high_self_assessment);
            } else {
                imv_result.setImageDrawable(null);
                txv_result.setText("");
            }
            commonValue = 0;
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case AlertDialog.BUTTON_POSITIVE:
                commonValue += currentValue;
                break;
            case AlertDialog.BUTTON_NEUTRAL:
                return;
        }
        dialog.cancel();
        showQuestion();
    }

    @SuppressWarnings("unchecked")
    public <T> T view(int viewId) {
        try {
            return (T) findViewById(viewId);
        } catch (ClassCastException e) {
            Log.e("MY", "Can't cast view (" + viewId + ") to target tipe! " + e.getMessage());
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                startTest();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnu_start:
                startTest();
                break;
            case R.id.mnu_exit:
                finish();
                break;
            default:
                return false;
        }
        return true;
    }

    public static Map<String, String> getHashMapResource(Context context, int hashMapResId) {
        Map<String, String> map = new HashMap<>();
        XmlResourceParser parser = context.getResources().getXml(hashMapResId);

        String key = null, value = null;

        try {
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (parser.getName().equals("entry")) {
                        key = parser.getAttributeValue(null, "key");

                        if (null == key) {
                            parser.close();
                            return null;
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (parser.getName().equals("entry")) {
                        map.put(value, key);
                        key = null;
                        value = null;
                    }
                } else if (eventType == XmlPullParser.TEXT) {
                    if (null != key) {
                        value = parser.getText();
                    }
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return map;
    }
}
