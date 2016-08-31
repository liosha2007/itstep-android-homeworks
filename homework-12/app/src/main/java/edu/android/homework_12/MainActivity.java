package edu.android.homework_12;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import edu.android.homework_12.tasks.InitTask;
import edu.android.homework_12.model.Day;
import edu.android.homework_12.model.Weather;
import edu.android.homework_12.tasks.ClearTask;
import edu.android.homework_12.tasks.LoadTask;
import edu.android.homework_12.tasks.NetworkTask;
import edu.android.homework_12.tasks.SaveTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String APP_PREFERENCES = "APP_PREFERENCES";
    // Doc http://jtds.sourceforge.net/faq.html#driverImplementation
    private static final String CONNECTION_STRING = "jdbc:jtds:sqlserver://%s:%s;instanceName=SQLExpress;DatabaseName=%s;integratedSecurity=true;user=%s;password=%s";
    private static final String DATABASE_INITIALIZED = "DATABASE_INITIALIZED";
    private static final String SERV_KEY = "SERV_KEY";
    private static final String PORT_KEY = "PORT_KEY";
    private static final String DBNM_KEY = "DBNM_KEY";
    private static final String USER_KEY = "USER_KEY";
    private static final String PASS_KEY = "PASS_KEY";

    private ImageButton imb_insert;
    private ImageButton imb_import;
    private TextView txv_country;
    private TextView txv_city;
    private TextView txv_coordinates;
    private LinearLayout lnl_days;
    private String serv = null;
    private String port = null;
    private String dbnm = null;
    private String user = null;
    private String pass = null;
    private SharedPreferences sharedPreferences;
    private boolean isSettingsChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        (imb_insert = view(R.id.imb_insert)).setOnClickListener(this);
        (imb_import = view(R.id.imb_import)).setOnClickListener(this);
        txv_country = view(R.id.txv_country);
        txv_city = view(R.id.txv_city);
        txv_coordinates = view(R.id.txv_coordinates);
        lnl_days = view(R.id.lly_days);

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
        } catch (Exception e) {
            Log.e("MY", "Can't load database driver!", e);
        }
        if (isDatabaseInitialized()) {
            new LoadTask(this).execute(makeConnectionString());
        }
    }

    @Override
    protected void onPostResume() {
        if (isSettingsChanged) {
            isSettingsChanged = false;
            if (isDatabaseInitialized()) {
                new LoadTask(this).execute(makeConnectionString());
            }
        }
        super.onPostResume();
    }

    private boolean isDatabaseInitialized() {
        this.serv = sharedPreferences.getString(SERV_KEY, null/*"192.168.0.100"*/);
        this.port = sharedPreferences.getString(PORT_KEY, null/*"1433"*/);
        this.dbnm = sharedPreferences.getString(DBNM_KEY, null/*"firstdb"*/);
        this.user = sharedPreferences.getString(USER_KEY, null/*"liosha"*/);
        this.pass = sharedPreferences.getString(PASS_KEY, null/*"612287"*/);
        try {
            if (this.serv != null) {
                if (sharedPreferences.getBoolean(DATABASE_INITIALIZED, false)) {
                    return true;
                } else {
                    new InitTask(this).execute(makeConnectionString());
                }
            }
        } catch (Exception e) {
            Log.e("MY", "Can't execute query!", e);
        }
        return false;
    }

    private String makeConnectionString() {
        final String connectionString = String.format(CONNECTION_STRING, serv, port, dbnm, this.user, this.pass);
        Log.d("MY", "connectionString: " + connectionString);
        return connectionString;
    }

    public void onDatabaseInitialized() {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(DATABASE_INITIALIZED, true);
        editor.commit();

        new LoadTask(this).execute(makeConnectionString());
    }

    public void onDataDownloaded(Weather weather) {
//        Log.d("MY", "weather: " + weather);
        if (this.serv == null) {
            Toast.makeText(this, "Введите данные сервера в настройках!", Toast.LENGTH_SHORT).show();
        }
        new SaveTask(this).execute(weather, makeConnectionString());
    }

    public void onDataSaved() {
        new LoadTask(this).execute(makeConnectionString());
    }

    public void onDataLoaded(Weather weather) {
        Log.d("MY", "weather: " + weather);
        if (weather != null) {
            txv_country.setText(weather.country);
            txv_city.setText(weather.city);
            txv_coordinates.setText(String.format(Locale.US, "(%s, %s)", weather.lat, weather.lon));
            displayDaysWeather(weather.days);
        } else {
            lnl_days.removeAllViews();
            txv_country.setText("");
            txv_city.setText("Нет данных");
            txv_coordinates.setText("Импортируйте или введите данные");
        }
    }

    private void displayDaysWeather(List<Day> days) {
        lnl_days.removeAllViews();
        final LayoutInflater layoutInflater = getLayoutInflater();
        for (Day day : days) {
            final View view = layoutInflater.inflate(R.layout.layout_day_weather, lnl_days, false);

            this.<TextView>view(view, R.id.txv_date).setText(day.date);
            this.<TextView>view(view, R.id.txv_conditions).setText(String.format(Locale.US, "%s,\n температура от %s до %s", day.conditions, day.low, day.high));

            this.<TextView>view(view, R.id.txv_day).setText(day.daytitle);
            this.<ImageView>view(view, R.id.imv_dayicon).setImageResource(Utils.resourceIdByName(day.dayicon));
            this.<TextView>view(view, R.id.txv_dayfcttext).setText(day.dayfcttext);

            this.<TextView>view(view, R.id.txv_night).setText(day.nighttitle);
            this.<ImageView>view(view, R.id.imv_nighticoin).setImageResource(Utils.resourceIdByName(day.nighticon));
            this.<TextView>view(view, R.id.txv_nightfcttext).setText(day.nightfcttext);
            lnl_days.addView(view);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imb_insert:
                Toast.makeText(this, "Функционал назодится в разработке!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.imb_import:
                new NetworkTask(this).execute((String) null);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // получим идентификатор выбранного пункта меню
        int id = item.getItemId();

        switch (id) {
            case R.id.mnu_clear:
                new ClearTask(this).execute(makeConnectionString());
                return true;
            case R.id.mnu_settings:
                this.isSettingsChanged = true;
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onDataCleared() {
        Toast.makeText(this, "Данные очищены!", Toast.LENGTH_SHORT).show();
        new LoadTask(this).execute(makeConnectionString());
    }

    public void onCantLoad() {
        Toast.makeText(this, "Не удалось загрузить данные!", Toast.LENGTH_SHORT).show();
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

    @SuppressWarnings("unchecked")
    public <T> T view(final View root, final int viewId) {
        try {
            return (T) root.findViewById(viewId);
        } catch (ClassCastException e) {
            Log.e("MY", "Can't cast view to target type! View ID: " + viewId);
        }
        return null;
    }
}
