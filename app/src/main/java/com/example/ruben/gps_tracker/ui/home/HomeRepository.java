package com.example.ruben.gps_tracker.ui.home;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.PointF;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

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
            public void onChange(boolean self) {
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
        Cursor cursor = mCntResolver.query(GpsTrackerContract.LocationEntry.CONTENT_URI, null, null, null, null);
        ArrayList<PointF> coords = new ArrayList<PointF>();
        Integer latIndx = cursor.getColumnIndex(GpsTrackerContract.LocationEntry.COLUMN_COORD_LAT_1);
        Integer lonIndx = cursor.getColumnIndex(GpsTrackerContract.LocationEntry.COLUMN_COORD_LONG_1);
        try {
            while (cursor.moveToNext())
            {
                Integer lat = cursor.getInt(latIndx);
                Integer lon = cursor.getInt(lonIndx);
                Log.d(TAG,  "To position: lat: " + lat + " lon:" + lon);
                Log.d(TAG,  "To position: lat: " + Float.valueOf(lat)/1000000 + " lon:" + Float.valueOf(lon)/1000000);
                coords.add(new PointF(Float.valueOf(lon)/1000000, Float.valueOf(lat)/1000000));
            }
        } finally {
            cursor.close();
        }

        mTrackerPath.postValue(coords);
    }
}
