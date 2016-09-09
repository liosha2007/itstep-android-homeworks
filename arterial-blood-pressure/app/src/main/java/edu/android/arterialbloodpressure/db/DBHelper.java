package edu.android.arterialbloodpressure.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author liosha (09.09.2016).
 */
public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "arterial-blood-pressure.sqlite", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            sqLiteDatabase.beginTransaction();
            sqLiteDatabase.execSQL("CREATE TABLE pressures (_id INTEGER AUTO INCREMENT PRIMARY KEY, date INTEGER UNIQUE NOT NULL, up INTEGER NOT NULL, down INTEGER NOT NULL)", new Object[]{});
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("MY", "Can't create database!", e);
        } finally {
            sqLiteDatabase.endTransaction();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
