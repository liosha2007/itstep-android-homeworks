package edu.android.homework_11.tasks;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import edu.android.homework_11.MainActivity;
import edu.android.homework_11.model.Day;
import edu.android.homework_11.model.Weather;

/**
 * @author liosha (27.08.2016).
 */
public class LoadTask extends GenericTask<Void, Void, Weather> {
    public LoadTask(MainActivity activity) {
        super(activity);
    }

    @Override
    protected Weather doInBackground(Void... voids) {
        final SQLiteDatabase rdb = ref.get().dbHelper.getReadableDatabase();
        final Cursor weatherCursor = rdb.rawQuery("SELECT country, city, lat, lon FROM weathers", null);
        Weather weather = null;
        if (weatherCursor.moveToFirst()) {
            int index = 0;
            weather = new Weather(
                    weatherCursor.getString(index++),
                    weatherCursor.getString(index++),
                    weatherCursor.getString(index++),
                    weatherCursor.getString(index)
            );
        }
        weatherCursor.close();
        final Cursor dayCursor = rdb.rawQuery("SELECT dayicon, daytitle, dayfcttext, nighticon, nighttitle, nightfcttext, date, high, low, conditions, maxwind, maxwinddir, avewind, avewinddir FROM days", null);
        if (dayCursor.moveToFirst() && weather != null) {
            do {
                int index = 0;
                Day day = new Day(
                        dayCursor.getString(index++),
                        dayCursor.getString(index++),
                        dayCursor.getString(index++),
                        dayCursor.getString(index++),
                        dayCursor.getString(index++),
                        dayCursor.getString(index++),
                        dayCursor.getString(index++),
                        dayCursor.getString(index++),
                        dayCursor.getString(index++),
                        dayCursor.getString(index++),
                        dayCursor.getString(index++),
                        dayCursor.getString(index++),
                        dayCursor.getString(index++),
                        dayCursor.getString(index)
                );
                weather.days.add(day);

            } while (dayCursor.moveToNext());
            dayCursor.close();
        }
        return weather;
    }

    @Override
    protected void onPostExecute(Weather weather) {
        super.onPostExecute(weather);
        ref.get().onDataLoaded(weather);
    }
}
