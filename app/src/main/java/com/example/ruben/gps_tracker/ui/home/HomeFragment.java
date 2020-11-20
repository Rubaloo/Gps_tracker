package com.example.ruben.gps_tracker.ui.home;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.ruben.gps_tracker.ActivityReceiver;
import com.example.ruben.gps_tracker.R;
import com.example.ruben.gps_tracker.data.GpsTrackerContract;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private HomeViewModel mHomeViewModel;
    private static final String TAG = HomeFragment.class.getSimpleName();
    private ActivityReceiver mActivityReceiver;
    private boolean mTest = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mHomeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        mHomeViewModel.getTrackerPath().observe(this, new Observer<ArrayList<PointF>>() {
            @Override
            public void onChanged(ArrayList<PointF> trackerPath)
            {
                Log.d(TAG, "Update map");
                Log.d(TAG, String.valueOf(trackerPath.size()));
                SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
                mapFragment.getMapAsync(HomeFragment.this);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return root;
    }


    @Override
    public void onActivityCreated(Bundle savedInstance)
    {
        super.onActivityCreated(savedInstance);
        mActivityReceiver = (ActivityReceiver) getActivity();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Context ctx = mActivityReceiver.getActivity();
        mHomeViewModel.setContentResolver(ctx.getContentResolver());
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mHomeViewModel.unregisterRepoObservers();
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        mMap.clear(); //clear old markers
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        ArrayList<PointF> lastPositions = mHomeViewModel.getTrackerPath().getValue();
        if(lastPositions != null && lastPositions.size() != 0)
        {
            for (PointF point : lastPositions)
            {
                LatLng pos = new LatLng(point.x, point.y);
                mMap.addMarker(new MarkerOptions().position(pos)
                        .title("MArker 1"));
            }

            PointF cameraFocus = (!lastPositions.isEmpty()) ? lastPositions.get(lastPositions.size() - 1) : new PointF(37.4219999f, -122.0862462f);
            CameraPosition googlePlex = CameraPosition.builder()
                    .target(new LatLng(cameraFocus.x, cameraFocus.y))
                    .zoom(10)
                    .bearing(0)
                    .tilt(45)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null);
        }
    }

    private ArrayList<PointF> getTrackerPathContent()
    {
        ArrayList<PointF> mCoords = new ArrayList<PointF>();

        ContentResolver cres = mActivityReceiver.getCntentResolver();
        Cursor cursor = cres.query(GpsTrackerContract.LocationEntry.CONTENT_URI, null, null, null, null);

        Integer latIndx = cursor.getColumnIndex(GpsTrackerContract.LocationEntry.COLUMN_COORD_LAT_1);
        Integer lonIndx = cursor.getColumnIndex(GpsTrackerContract.LocationEntry.COLUMN_COORD_LONG_1);
        try {
            while (cursor.moveToNext())
            {
                Integer lat = cursor.getInt(latIndx);
                Integer lon = cursor.getInt(lonIndx);
                Log.d(TAG,  "To position: lat: " + lat + " lon:" + lon);
                Log.d(TAG,  "To position: lat: " + Float.valueOf(lat)/1000000 + " lon:" + Float.valueOf(lon)/1000000);
                mCoords.add(new PointF(Float.valueOf(lon)/1000000, Float.valueOf(lat)/1000000));
            }
        } finally {
            cursor.close();
        }

        return mCoords;
    }

}