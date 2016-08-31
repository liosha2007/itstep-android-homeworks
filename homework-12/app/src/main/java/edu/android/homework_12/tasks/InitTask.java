package edu.android.homework_12.tasks;

import android.util.Log;

import java.sql.Connection;
import java.sql.Statement;

import edu.android.homework_12.MainActivity;
import edu.android.homework_12.Utils;

/**
 * @author liosha (27.08.2016).
 */
public class InitTask extends GenericTask<String, Void, Void> {

    public InitTask(MainActivity activity) {
        super(activity);
    }

    @Override
    protected Void doInBackground(String... contexts) {
        final String connectionString = contexts[0];
        try {
            final Connection connection = Utils.openConnection(connectionString);
            try {
                final Statement statement = connection.createStatement();
                try {
                    final boolean executeWeather = statement.execute("DROP TABLE weathers; CREATE TABLE weathers (" +
                            "_id INTEGER PRIMARY KEY, " +
                            "country VARCHAR(32), " +
                            "city VARCHAR(32), " +
                            "lat VARCHAR(32), " +
                            "lon VARCHAR(32))");
                    final boolean executeDay = statement.execute("DROP TABLE days; CREATE TABLE days (" +
                            "_id INTEGER PRIMARY KEY, " +
                            "dayicon VARCHAR(32), " +
                            "daytitle VARCHAR(32), " +
                            "dayfcttext VARCHAR(256), " +
                            "nighticon VARCHAR(32), " +
                            "nighttitle VARCHAR(32), " +
                            "nightfcttext VARCHAR(256), " +
                            "date VARCHAR(32), " +
                            "high VARCHAR(32), " +
                            "low VARCHAR(32), " +
                            "conditions VARCHAR(32), " +
                            "maxwind VARCHAR(32), " +
                            "maxwinddir VARCHAR(32), " +
                            "avewind VARCHAR(32), " +
                            "avewinddir VARCHAR(32))");
                    if (executeWeather || executeDay) {
                        Log.e("MY", "Can't create tables!");
                    }
                } finally {
                    statement.close();
                }
            } finally {
                connection.close();
            }
        } catch (Exception e) {
            Log.e("MY", "Error during creating tables!", e);
            cancel(false);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        ref.get().onDatabaseInitialized();
    }
}
