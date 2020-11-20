package com.example.ruben.gps_tracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.ruben.gps_tracker.data.GpsTrackerContract.LocationEntry;

public class GpsTrackerDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "gpstracker.db";

    public GpsTrackerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE " + LocationEntry.TABLE_NAME + " (" +
                LocationEntry._ID + " INTEGER PRIMARY KEY," +
                LocationEntry.COLUMN_STATUS + " INTEGER NOT NULL, " +
                LocationEntry.COLUMN_CREDIT + " INTEGER NOT NULL, " +
                LocationEntry.COLUMN_SPEED + " INTEGER NOT NULL, " +
                LocationEntry.COLUMN_BATTERY + " INTEGER NOT NULL, " +
                LocationEntry.COLUMN_COORD_LAT_1 + " INTEGER NOT NULL, " +
                LocationEntry.COLUMN_COORD_LONG_1 + " INTEGER NOT NULL, " +
                LocationEntry.COLUMN_COORD_LAT_2 + " INTEGER NOT NULL, " +
                LocationEntry.COLUMN_COORD_LONG_2 + " INTEGER NOT NULL, " +
                LocationEntry.COLUMN_COORD_LAT_3 + " INTEGER NOT NULL, " +
                LocationEntry.COLUMN_COORD_LONG_3 + " INTEGER NOT NULL, " +
                LocationEntry.COLUMN_COORD_LAT_4 + " INTEGER NOT NULL, " +
                LocationEntry.COLUMN_COORD_LONG_4 + " INTEGER NOT NULL, " +
                LocationEntry.COLUMN_TIMESTAMP + " TEXT NOT NULL " +
                " );";
        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude
        db.execSQL(SQL_CREATE_LOCATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        final String SQL_DELETE_LOCATION_TABLE = "DROP TABLE IF EXISTS " + LocationEntry.TABLE_NAME;

        db.execSQL(SQL_DELETE_LOCATION_TABLE);
        onCreate(db);

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
