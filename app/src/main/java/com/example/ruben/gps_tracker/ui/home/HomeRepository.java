package com.example.ruben.gps_tracker.ui.home;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.PointF;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.ruben.gps_tracker.GTSms;
import com.example.ruben.gps_tracker.data.GpsTrackerContract;

import java.util.ArrayList;

public class HomeRepository {
    private static final String TAG = HomeRepository.class.getSimpleName();
    private MutableLiveData<ArrayList<PointF>> mTrackerPath;
    private ContentResolver mCntResolver;
    private ContentObserver mObserver;

    HomeRepository()
    {
        mTrackerPath = new MutableLiveData<ArrayList<PointF>>();
    }

    MutableLiveData<ArrayList<PointF>> getTrackerPath()
    {
        return mTrackerPath;
    }


    public void setContentResolver(ContentResolver pContentResolver)
    {
        mCntResolver = pContentResolver;
    }

    public void registerObservers() {
        mCntResolver.registerContentObserver(GpsTrackerContract.LocationEntry.CONTENT_URI, false, mObserver = new ContentObserver(null) {
            @Override
            public void onChange(boolean selfChange) {
                onChange(selfChange, null);
            }

            @Override
            public void onChange(boolean selfChange, Uri uri) {
                // Handle change.
                refreshTrackerPath();
            }
        });
    }

    public void unregisterObservers()
    {
        mCntResolver.unregisterContentObserver(mObserver);
    }

    private void refreshTrackerPath()
    {
        Log.d(TAG, "Location updated refresh data");
        String[] mProjection =
                {
                        GpsTrackerContract.LocationEntry._ID,
                        GpsTrackerContract.LocationEntry.COLUMN_COORD_LAT_1,
                        GpsTrackerContract.LocationEntry.COLUMN_COORD_LONG_1,
                        GpsTrackerContract.LocationEntry.COLUMN_COORD_LAT_2,
                        GpsTrackerContract.LocationEntry.COLUMN_COORD_LONG_2,
                        GpsTrackerContract.LocationEntry.COLUMN_COORD_LAT_3,
                        GpsTrackerContract.LocationEntry.COLUMN_COORD_LONG_3,
                        GpsTrackerContract.LocationEntry.COLUMN_COORD_LAT_4,
                        GpsTrackerContract.LocationEntry.COLUMN_COORD_LONG_4
                };
        Cursor cursor = mCntResolver.query(GpsTrackerContract.LocationEntry.CONTENT_URI,
                mProjection,
                null,
                null,
                GpsTrackerContract.LocationEntry.COLUMN_TIMESTAMP + " ASC "+" LIMIT 30");
        Integer latIndx1 = cursor.getColumnIndex(GpsTrackerContract.LocationEntry.COLUMN_COORD_LAT_1);
        Integer lonIndx1 = cursor.getColumnIndex(GpsTrackerContract.LocationEntry.COLUMN_COORD_LONG_1);
        Integer latIndx2 = cursor.getColumnIndex(GpsTrackerContract.LocationEntry.COLUMN_COORD_LAT_2);
        Integer lonIndx2 = cursor.getColumnIndex(GpsTrackerContract.LocationEntry.COLUMN_COORD_LONG_2);
        Integer latIndx3 = cursor.getColumnIndex(GpsTrackerContract.LocationEntry.COLUMN_COORD_LAT_3);
        Integer lonIndx3 = cursor.getColumnIndex(GpsTrackerContract.LocationEntry.COLUMN_COORD_LONG_3);
        Integer latIndx4 = cursor.getColumnIndex(GpsTrackerContract.LocationEntry.COLUMN_COORD_LAT_4);
        Integer lonIndx4 = cursor.getColumnIndex(GpsTrackerContract.LocationEntry.COLUMN_COORD_LONG_4);
        ArrayList<PointF> coords = new ArrayList<>();
        try {
            while (cursor.moveToNext())
            {
                Integer lat1 = cursor.getInt(latIndx1); Integer lon1 = cursor.getInt(lonIndx1);
                Integer lat2 = cursor.getInt(latIndx2); Integer lon2 = cursor.getInt(lonIndx2);
                Integer lat3 = cursor.getInt(latIndx3); Integer lon3 = cursor.getInt(lonIndx3);
                Integer lat4 = cursor.getInt(latIndx4); Integer lon4 = cursor.getInt(lonIndx4);

                Log.d(TAG,  "To position: lat1: " + Float.valueOf(lat1)/1000000 + " lon1:" + Float.valueOf(lon1)/1000000);
                Log.d(TAG,  "To position: lat2: " + Float.valueOf(lat2)/1000000 + " lon2:" + Float.valueOf(lon2)/1000000);
                Log.d(TAG,  "To position: lat3: " + Float.valueOf(lat3)/1000000 + " lon3:" + Float.valueOf(lon3)/1000000);
                Log.d(TAG,  "To position: lat4: " + Float.valueOf(lat4)/1000000 + " lon4:" + Float.valueOf(lon4)/1000000);

                if (lat1 != 0 && lon1 != 0) coords.add(new PointF(Float.valueOf(lon1)/1000000, Float.valueOf(lat1)/1000000));
                if (lat2 != 0 && lon2 != 0) coords.add(new PointF(Float.valueOf(lon2)/1000000, Float.valueOf(lat2)/1000000));
                if (lat3 != 0 && lon3 != 0) coords.add(new PointF(Float.valueOf(lon3)/1000000, Float.valueOf(lat3)/1000000));
                if (lat4 != 0 && lon4 != 0) coords.add(new PointF(Float.valueOf(lon4)/1000000, Float.valueOf(lat4)/1000000));
            }
        } finally {
            cursor.close();
        }
        mTrackerPath.postValue(coords);
    }
}
