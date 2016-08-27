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
public class ClearTask extends GenericTask<Void, Void, Void> {
    public ClearTask(MainActivity activity) {
        super(activity);
    }

    @Override
    protected Void doInBackground(Void... params) {
        final SQLiteDatabase wdb = ref.get().dbHelper.getWritableDatabase();
        try {
            wdb.beginTransaction();

            wdb.execSQL("DELETE FROM weathers");
            wdb.execSQL("DELETE FROM days");

            wdb.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("MY", "Can't clear the data!", e);
        } finally {
            wdb.endTransaction();
        }
        wdb.close();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        ref.get().onDataCleared();
    }
}
