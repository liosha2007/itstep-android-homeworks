package edu.android.arterialbloodpressure.tasks;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import edu.android.arterialbloodpressure.MainActivity;
import edu.android.arterialbloodpressure.entity.Pressure;
import edu.android.arterialbloodpressure.fragment.MainFragment;

/**
 * @author liosha (09.09.2016).
 */
public class InsertTask extends GenericTask<MainFragment, Pressure, Void, Void> {
    public InsertTask(MainFragment fragment) {
        super(fragment);
    }

    @Override
    protected Void doInBackground(Pressure... pressures) {
        Pressure pressure = pressures[0];
        final SQLiteDatabase readableDatabase = MainActivity.dbHelper.getReadableDatabase();
        try {
            readableDatabase.beginTransaction();
            readableDatabase.execSQL("INSERT INTO pressures (date, up, down) VALUES(?, ?, ?)", new Object[]{
                    pressure.date.getTime(), pressure.up, pressure.down
            });
            readableDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("MY", "Can't insert into database!", e);
        } finally {
            readableDatabase.endTransaction();
            readableDatabase.close();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        ref.get().onDataInserted();
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onCancelled() {
        ref.get().onInsertCancelled();
        super.onCancelled();
    }
}
