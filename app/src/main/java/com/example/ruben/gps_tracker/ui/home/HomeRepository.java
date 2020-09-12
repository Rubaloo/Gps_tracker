package com.example.ruben.gps_tracker.ui.home;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.PointF;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.ruben.gps_tracker.SMSReceiver;
import com.example.ruben.gps_tracker.data.GpsTrackerContract;

import java.util.ArrayList;

public class HomeRepository {
    private static final String TAG = SMSReceiver.class.getSimpleName();
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
        /*Cursor cursor = mCntResolver.query(GpsTrackerContract.LocationEntry.CONTENT_URI, null, null, null, null);
        ArrayList<PointF> coords = new ArrayList<PointF>();
        int latIndx = cursor.getColumnIndex(GpsTrackerContract.LocationEntry.COLUMN_COORD_LAT);
        int lonIndx = cursor.getColumnIndex(GpsTrackerContract.LocationEntry.COLUMN_COORD_LONG);
        try {
            while (cursor.moveToNext())
            {
                String lat = cursor.getString(latIndx);
                String lon = cursor.getString(lonIndx);
                coords.add(new PointF(Float.parseFloat(lat), Float.parseFloat(lon)));
            }
        } finally {
            cursor.close();
        }

        mTrackerPath.setValue(coords);*/
    }
}
