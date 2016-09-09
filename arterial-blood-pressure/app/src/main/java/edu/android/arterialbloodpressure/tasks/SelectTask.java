package edu.android.arterialbloodpressure.tasks;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.android.arterialbloodpressure.MainActivity;
import edu.android.arterialbloodpressure.entity.Pressure;
import edu.android.arterialbloodpressure.fragment.MainFragment;

/**
 * @author liosha (10.09.2016).
 */
public class SelectTask extends GenericTask<MainFragment, Date, Void, List<Pressure>> {
    public SelectTask(MainFragment controller) {
        super(controller);
    }

    @Override
    protected List<Pressure> doInBackground(Date... params) {
        Date date = params[0];

        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(date.getTime());
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);

        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(date.getTime());
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);

        List<Pressure> pressures = new ArrayList<>();
        final SQLiteDatabase readableDatabase = MainActivity.dbHelper.getReadableDatabase();
        try {
            readableDatabase.beginTransaction();
            final Cursor cursor = readableDatabase.rawQuery("SELECT date, up, down FROM pressures WHERE date > ? AND date < ?", new String[]{
                    String.valueOf(start.getTimeInMillis()), String.valueOf(end.getTimeInMillis())
            });
            while (cursor.moveToNext()) {
                pressures.add(new Pressure(new Date(cursor.getLong(0)), cursor.getInt(1), cursor.getInt(2)));
            }
            cursor.close();
            readableDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("MY", "Can't create database!", e);
        } finally {
            readableDatabase.endTransaction();
            readableDatabase.close();
        }
        Log.d("MY", "pressures.size: " + pressures.size());
        return pressures;
    }

    @Override
    protected void onPostExecute(List<Pressure> pressures) {
        ref.get().onDataSelected(pressures);
        super.onPostExecute(pressures);
    }

    @Override
    protected void onCancelled() {
        ref.get().onSelectCancelled();
        super.onCancelled();
    }
}
