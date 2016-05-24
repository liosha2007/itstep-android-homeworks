package edu.sample.homework04;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.List;

import edu.sample.homework04.adapter.HashMapAdapter;

public class MainActivity extends GenericActivity {

    public static final String TAB_COMMON = "cm";
    public static final String TAB_CHARACTERISTICS = "ch";
    public static final String TAB_OTHER = "ot";
    public static final int MAX_PRICE = 137000;

    private Button btn_open;

    private ListView lsv_class;
    private ListView lsv_vendor;
    private EditText edt_price_from;
    private SeekBar skb_price_from;
    private EditText edt_price_to;
    private SeekBar skb_price_to;
    private CheckBox chb_percent;

    private ListView lsv_screen;
    private ListView lsv_resolution;
    private ListView lsv_screen_type;
    private ListView lsv_screen_material;
    private RadioButton rdb_sensor_yes;
    private RadioButton rdb_sensor_no;
    private ListView lsv_core_count;
    private ListView lsv_core;
    private ListView lsv_memory;
    private CheckBox chb_3g;
    private ListView lsv_video_type;
    private ListView lsv_video;
    private ListView lsv_storage_type;
    private ListView lsv_storage;
    private Spinner spn_os;

    private CheckBox chb_keyboard_light;
    private Spinner spn_battery;
    private ListView lsv_weight;
    private ListView lsv_color;

    private HashMapAdapter class_adapter;
    private HashMapAdapter vendor_adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureTabs();
        findViews();
        fillViews();
        bindViews();
    }

    private String formQueryListView(String key, ListView listView) {
        final SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();
        StringBuilder builder = new StringBuilder(key).append("=");
        boolean isLast = false, isFirst = true;
        for (int n = 0; n < checkedItemPositions.size(); n++) {
            isLast = n == checkedItemPositions.size() - 1;
            if (checkedItemPositions.get(checkedItemPositions.keyAt(n))) {
                String value = (String) listView.getChildAt(checkedItemPositions.keyAt(n)).getTag();
                if (!isFirst) {
                    builder.append("%2C");
                }
                builder.append(value);
                isFirst = false;
            }
        }
        return isLast ? builder.toString() : null;
    }

    private void configureTabs() {
        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec(TAB_COMMON);

        tabSpec.setIndicator("Общее");
        tabSpec.setContent(R.id.scv_tab_common);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec(TAB_CHARACTERISTICS);
        tabSpec.setIndicator("Х-ки");
        tabSpec.setContent(R.id.scv_tab_characteristics);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec(TAB_OTHER);
        tabSpec.setIndicator("Еще");
        tabSpec.setContent(R.id.scv_tab_other);
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTabByTag(TAB_COMMON);
    }

    private void findViews() {
        btn_open = view(R.id.btn_open);

        lsv_class = view(R.id.lsv_class);
        lsv_vendor = view(R.id.lsv_vendor);
        edt_price_from = view(R.id.edt_price_from);
        skb_price_from = view(R.id.skb_price_from);
        edt_price_to = view(R.id.edt_price_to);
        skb_price_to = view(R.id.skb_price_to);
        chb_percent = view(R.id.chb_percent);

        lsv_screen = view(R.id.lsv_screen);
        lsv_resolution = view(R.id.lsv_resoluion);
        lsv_screen_type = view(R.id.lsv_screen_type);
        lsv_screen_material = view(R.id.lsv_screen_material);
        rdb_sensor_yes = view(R.id.rdb_sensor_yes);
        rdb_sensor_no = view(R.id.rdb_sensor_no);
        lsv_core_count = view(R.id.lsv_core_count);
        lsv_core = view(R.id.lsv_core);
        lsv_memory = view(R.id.lsv_memory);
        chb_3g = view(R.id.chb_3g);
        lsv_video_type = view(R.id.lsv_video_type);
        lsv_video = view(R.id.lsv_video);
        lsv_storage_type = view(R.id.lsv_storage_type);
        lsv_storage = view(R.id.lsv_storage);
        spn_os = view(R.id.spn_os);

        chb_keyboard_light = view(R.id.chb_keyboard_light);
        spn_battery = view(R.id.spn_battery);
        lsv_weight = view(R.id.lsv_weight);
        lsv_color = view(R.id.lsv_color);
    }

    private void bindViews() {
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> query = new ArrayList<>();

                query.add(formQueryListView("preset", lsv_class));
                query.add(formQueryListView("producer", lsv_vendor));
                query.add(formQueryCheckBox("58432", chb_percent, "166202"));
                query.add(formQueryForPrice("price", edt_price_from, edt_price_to));

                query.add(formQueryListView("20861", lsv_screen));
                query.add(formQueryListView("25800", lsv_resolution));
                query.add(formQueryListView("36519", lsv_screen_type));
                query.add(formQueryListView("23541", lsv_screen_material));
                query.add(formQueryForSensor("26413", rdb_sensor_yes, rdb_sensor_no));
                query.add(formQueryListView("72566", lsv_core_count));
                query.add(formQueryListView("processor", lsv_core));
                query.add(formQueryListView("20863", lsv_memory));
                query.add(formQueryCheckBox("69737", chb_3g, "272584"));
                query.add(formQueryListView("73143", lsv_video_type));
                query.add(formQueryListView("20881", lsv_video));
                query.add(formQueryListView("36514", lsv_storage_type));
                query.add(formQueryListView("20882", lsv_storage));

                query.add(formQuerySpinner("20886", spn_os));
                query.add(formQueryCheckBox("72560", chb_keyboard_light, "305288"));
                query.add(formQuerySpinner("batareya-87334", spn_battery));
                query.add(formQueryListView("20884", lsv_weight));
                query.add(formQueryListView("21737", lsv_color));

                StringBuilder builder = new StringBuilder("http://rozetka.com.ua/notebooks/c80004/filter/");
                List<String> params = new ArrayList<>();
                for (String param : query) {
                    if (param != null && !param.trim().isEmpty()) {
                        params.add(param);
                    }
                }
                builder.append(TextUtils.join(";", params));
                Log.d("MY", builder.toString());
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(builder.toString()));
                startActivity(browserIntent);
            }
        });
    }



    private void fillViews() {
        // Common

        lsv_class.setAdapter(class_adapter = new HashMapAdapter(getHashMapResource(this, R.xml.lsv_class)));
        setListViewHeightBasedOnItems(lsv_class);

        lsv_vendor.setAdapter(vendor_adapter = new HashMapAdapter(getHashMapResource(this, R.xml.lsv_vendor)));
        setListViewHeightBasedOnItems(lsv_vendor);

        skb_price_from.setMax(MAX_PRICE);
        skb_price_from.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                edt_price_from.setText(seekBar.getProgress() + "");
            }
        });
        skb_price_to.setMax(MAX_PRICE);
        skb_price_to.setProgress(MAX_PRICE);
        skb_price_to.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                skb_price_from.setMax(seekBar.getProgress());
                edt_price_from.setText(skb_price_from.getProgress() + "");
                edt_price_to.setText(seekBar.getProgress() + "");
            }
        });

        // Characteristics
        lsv_screen.setAdapter(new HashMapAdapter(getHashMapResource(this, R.xml.lsv_screen)));
        setListViewHeightBasedOnItems(lsv_screen);

        lsv_screen_type.setAdapter(new HashMapAdapter(getHashMapResource(this, R.xml.lsv_screen_type)));
        setListViewHeightBasedOnItems(lsv_screen_type);

        lsv_resolution.setAdapter(new HashMapAdapter(getHashMapResource(this, R.xml.lsv_resolution)));
        setListViewHeightBasedOnItems(lsv_resolution);

        lsv_screen_material.setAdapter(new HashMapAdapter(getHashMapResource(this, R.xml.lsv_screen_material)));
        setListViewHeightBasedOnItems(lsv_screen_material);

        lsv_core_count.setAdapter(new HashMapAdapter(getHashMapResource(this, R.xml.lsv_core_count)));
        setListViewHeightBasedOnItems(lsv_core_count);

        lsv_core.setAdapter(new HashMapAdapter(getHashMapResource(this, R.xml.lsv_core)));
        setListViewHeightBasedOnItems(lsv_core);

        lsv_memory.setAdapter(new HashMapAdapter(getHashMapResource(this, R.xml.lsv_memory)));
        setListViewHeightBasedOnItems(lsv_memory);

        lsv_video_type.setAdapter(new HashMapAdapter(getHashMapResource(this, R.xml.lsv_video_type)));
        setListViewHeightBasedOnItems(lsv_video_type);

        lsv_video.setAdapter(new HashMapAdapter(getHashMapResource(this, R.xml.lsv_video)));
        setListViewHeightBasedOnItems(lsv_video);

        lsv_storage_type.setAdapter(new HashMapAdapter(getHashMapResource(this, R.xml.lsv_storage_type)));
        setListViewHeightBasedOnItems(lsv_storage_type);

        lsv_storage.setAdapter(new HashMapAdapter(getHashMapResource(this, R.xml.lsv_storage)));
        setListViewHeightBasedOnItems(lsv_storage);

        // Others

        spn_os.setAdapter(new HashMapAdapter(getHashMapResource(this, R.xml.spn_os)));

        spn_battery.setAdapter(new HashMapAdapter(getHashMapResource(this, R.xml.spn_battery)));

        lsv_weight.setAdapter(new HashMapAdapter(getHashMapResource(this, R.xml.lsv_weight)));
        setListViewHeightBasedOnItems(lsv_weight);

        lsv_color.setAdapter(new HashMapAdapter(getHashMapResource(this, R.xml.lsv_color)));
        setListViewHeightBasedOnItems(lsv_color);

    }
}
