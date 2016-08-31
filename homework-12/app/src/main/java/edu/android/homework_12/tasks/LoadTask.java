package edu.android.homework_12.tasks;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import edu.android.homework_12.MainActivity;
import edu.android.homework_12.Utils;
import edu.android.homework_12.model.Day;
import edu.android.homework_12.model.Weather;

/**
 * @author liosha (27.08.2016).
 */
public class LoadTask extends GenericTask<String, Void, Weather> {
    public LoadTask(MainActivity activity) {
        super(activity);
    }

    @Override
    protected Weather doInBackground(String... params) {
        final String connectionString = params[0];
        Weather weather = null;
        try {
            final Connection connection = Utils.openConnection(connectionString);
            try {
                final Statement weathersStatement = connection.createStatement();
                try {
                    final ResultSet resultSet = weathersStatement.executeQuery("SELECT country, city, lat, lon FROM weathers");
                    if (resultSet.next()) {
                        final String country = resultSet.getString("country");
                        final String city = resultSet.getString("city");
                        final String lat = resultSet.getString("lat");
                        final String lon = resultSet.getString("lon");
                        weather = new Weather(country, city, lat, lon);
                    }
                } finally {
                    weathersStatement.close();
                }

                if (weather == null) {
                    throw new Exception("Weather is not loaded!");
                }

                final Statement daysStatement = connection.createStatement();
                try {
                    final ResultSet resultSet = daysStatement.executeQuery(
                            "SELECT dayicon, daytitle, dayfcttext, nighticon, nighttitle, nightfcttext, date, high, low, conditions, maxwind, maxwinddir, avewind, avewinddir FROM days"
                );
                    while (resultSet.next()) {
                        final String dayicon = resultSet.getString("dayicon");
                        final String daytitle = resultSet.getString("daytitle");
                        final String dayfcttext = resultSet.getString("dayfcttext");
                        final String nighticon = resultSet.getString("nighticon");
                        final String nighttitle = resultSet.getString("nighttitle");
                        final String nightfcttext = resultSet.getString("nightfcttext");
                        final String date = resultSet.getString("date");
                        final String high = resultSet.getString("high");
                        final String low = resultSet.getString("low");
                        final String conditions = resultSet.getString("conditions");
                        final String maxwind = resultSet.getString("maxwind");
                        final String maxwinddir = resultSet.getString("maxwinddir");
                        final String avewind = resultSet.getString("avewind");
                        final String avewinddir = resultSet.getString("avewinddir");
                        weather.days.add(new Day(dayicon, daytitle, dayfcttext, nighticon, nighttitle, nightfcttext, date, high, low, conditions, maxwind, maxwinddir, avewind, avewinddir));
                    }
                } finally {
                    daysStatement.close();
                }
            } finally {
                connection.close();
            }
        } catch (Exception e) {
            Log.e("MY", "Can't create connection!", e);
        }
        return weather;

    }

    @Override
    protected void onPostExecute(Weather weather) {
        super.onPostExecute(weather);
        ref.get().onDataLoaded(weather);
    }
}
