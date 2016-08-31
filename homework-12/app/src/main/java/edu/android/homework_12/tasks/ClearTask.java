package edu.android.homework_12.tasks;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import edu.android.homework_12.MainActivity;
import edu.android.homework_12.Utils;
import edu.android.homework_12.model.Day;
import edu.android.homework_12.model.Weather;

/**
 * @author liosha (27.08.2016).
 */
public class ClearTask extends GenericTask<String, Void, Void> {
    public ClearTask(MainActivity activity) {
        super(activity);
    }

    @Override
    protected Void doInBackground(String... params) {
        final String connectionString = params[0];
        try {
            final Connection connection = Utils.openConnection(connectionString);
            try {
                final Statement statement = connection.createStatement();
                try {
                    statement.executeUpdate("DELETE FROM weathers");
                    statement.executeUpdate("DELETE FROM days");
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (Exception e) {
            Log.e("MY", "Can't create connection!", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        ref.get().onDataCleared();
    }
}
