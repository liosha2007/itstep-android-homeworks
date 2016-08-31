package edu.android.homework_12.tasks;

import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import edu.android.homework_12.MainActivity;
import edu.android.homework_12.Utils;
import edu.android.homework_12.model.Day;
import edu.android.homework_12.model.Weather;

/**
 * @author liosha (27.08.2016).
 */
public class SaveTask extends GenericTask<Object, Void, Void> {
    public SaveTask(MainActivity activity) {
        super(activity);
    }

    @Override
    protected Void doInBackground(Object... params) {
        final Weather weather = (Weather) params[0];
        final String connectionString = (String) params[1];

        try {
            final Connection connection = Utils.openConnection(connectionString);
            try {
                deleteOldData(connection);
                insertWeathers(weather, connection);
                insertDays(weather, connection);
            } finally {
                connection.close();
            }
        } catch (Exception e) {
            Log.e("MY", "Can't create connection!", e);
        }
        return null;
    }

    private void insertWeathers(Weather weather, Connection connection) throws SQLException {
        final PreparedStatement insertWeather = connection.prepareStatement("INSERT INTO weathers (_id, country, city, lat, lon) VALUES (?, ?, ?, ?, ?)");
        insertWeather.setInt(   1, 1);
        insertWeather.setString(2, weather.country);
        insertWeather.setString(3, weather.city);
        insertWeather.setString(4, weather.lat);
        insertWeather.setString(5, weather.lon);
        try {
            insertWeather.executeUpdate();
        } finally {
            insertWeather.close();
        }
    }

    private void insertDays(Weather weather, Connection connection) throws SQLException {
            List<Day> days = weather.days;
            for (int n = 0; n < days.size(); n++) {
                Day day = days.get(n);
                final PreparedStatement insertWeather = connection.prepareStatement(
                        "INSERT INTO days (_id, dayicon, daytitle, dayfcttext, nighticon, nighttitle, nightfcttext, date, high, low, conditions, maxwind, maxwinddir, avewind, avewinddir) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
                );
                insertWeather.setInt(   1, n + 1);
                insertWeather.setString(2, day.dayicon);
                insertWeather.setString(3, day.daytitle);
                insertWeather.setString(4, day.dayfcttext);
                insertWeather.setString(5, day.nighticon);
                insertWeather.setString(6, day.nighttitle);
                insertWeather.setString(7, day.nightfcttext);
                insertWeather.setString(8, day.date);
                insertWeather.setString(9, day.high);
                insertWeather.setString(10, day.low);
                insertWeather.setString(11, day.conditions);
                insertWeather.setString(12, day.maxwind);
                insertWeather.setString(13, day.maxwinddir);
                insertWeather.setString(14, day.avewind);
                insertWeather.setString(15, day.avewinddir);
                try {
                    insertWeather.executeUpdate();
                } finally {
                    insertWeather.close();
                }
            }
    }

    private void deleteOldData(Connection connection) throws SQLException {
        final Statement deleteStatement = connection.createStatement();
        try {
            deleteStatement.execute("DELETE FROM weathers");
            deleteStatement.execute("DELETE FROM days");
        } finally {
            deleteStatement.close();
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        ref.get().onDataSaved();
    }
}
