package edu.android.homework_11.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author liosha (27.08.2016).
 */
public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "weather.sqlite", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE weathers (" +
                "_id INTEGER PRIMARY KEY, " +
                "country VARCHAR(32), " +
                "city VARCHAR(32), " +
                "lat VARCHAR(32), " +
                "lon VARCHAR(32))");
        sqLiteDatabase.execSQL("CREATE TABLE days (" +
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
