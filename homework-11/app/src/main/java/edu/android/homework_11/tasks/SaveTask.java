package edu.android.homework_11.tasks;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import edu.android.homework_11.MainActivity;
import edu.android.homework_11.model.Day;
import edu.android.homework_11.model.Weather;

/**
 * @author liosha (27.08.2016).
 */
public class SaveTask extends GenericTask<Weather, Void, Void> {
    public SaveTask(MainActivity activity) {
        super(activity);
    }

    @Override
    protected Void doInBackground(Weather... params) {
        final Weather weather = params[0];
        final SQLiteDatabase wdb = ref.get().dbHelper.getWritableDatabase();
        try {
            wdb.beginTransaction();

            wdb.execSQL("DELETE FROM weathers");
            wdb.execSQL("DELETE FROM days");

            ContentValues weathersValues = new ContentValues();
            weathersValues.put("_id", 1);
            weathersValues.put("country", weather.country);
            weathersValues.put("city", weather.city);
            weathersValues.put("lat", weather.lat);
            weathersValues.put("lon", weather.lon);
            wdb.insertOrThrow("weathers", null, weathersValues);

            List<Day> days = weather.days;
            for (int n = 0; n < days.size(); n++) {
                Day day = days.get(n);
                ContentValues daysValues = new ContentValues();
                daysValues.put("_id", n + 1);
                daysValues.put("dayicon", day.dayicon);
                daysValues.put("daytitle", day.daytitle);
                daysValues.put("dayfcttext", day.dayfcttext);
                daysValues.put("nighticon", day.nighticon);
                daysValues.put("nighttitle", day.nighttitle);
                daysValues.put("nightfcttext", day.nightfcttext);
                daysValues.put("date", day.date);
                daysValues.put("high", day.high);
                daysValues.put("low", day.low);
                daysValues.put("conditions", day.conditions);
                daysValues.put("maxwind", day.maxwind);
                daysValues.put("maxwinddir", day.maxwinddir);
                daysValues.put("avewind", day.avewind);
                daysValues.put("avewinddir", day.avewinddir);
                wdb.insertOrThrow("days", null, daysValues);
            }
            wdb.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("MY", "Can't save the weather!", e);
        } finally {
            wdb.endTransaction();
        }
        wdb.close();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        ref.get().onDataSaved();
    }
}
