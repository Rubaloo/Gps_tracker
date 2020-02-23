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
import java.util.List;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private HomeViewModel mHomeViewModel;
    private static final String TAG = HomeFragment.class.getSimpleName();
    private ActivityReceiver mActivityReceiver;
    private ContentObserver mObserver;

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
        mHomeViewModel.addTrackerSubPath(getTrackerPathContent());
        mHomeViewModel.getTrackerPath().observe(this, new Observer<List<PointF>>() {
            @Override
            public void onChanged(List<PointF> mPath)
            {
                Log.d(TAG, "updateMapMarker/s");
                //UpdateMArkers markers from positions
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
        ContentResolver cres = ctx.getContentResolver();
        cres.registerContentObserver(GpsTrackerContract.LocationEntry.CONTENT_URI, false, mObserver = new ContentObserver(null) {
            @Override
            public void onChange(boolean self) {
                mHomeViewModel.restartTrackerPath(null);
            }
        });
    }

    @Override
    public void onPause()
    {
        super.onPause();
        Context ctx = mActivityReceiver.getActivity();
        ContentResolver cres = ctx.getContentResolver();
        cres.unregisterContentObserver(mObserver);

    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        mMap.clear(); //clear old markers
        ArrayList<PointF> tckrPath = mHomeViewModel.getTrackerPath();

        for(PointF coord : tckrPath)
        {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(coord.x, coord.y))
                    .title("Captain America"));
        }
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);



        CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng(37.4219999, -122.0862462))
                .zoom(10)
                .bearing(0)
                .tilt(45)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null);

    }

    private ArrayList<PointF> getTrackerPathContent()
    {
        ArrayList<PointF> mCoords = new ArrayList<PointF>();

        ContentResolver cres = mActivityReceiver.getContentResolver();
        Cursor cursor = cres.query(GpsTrackerContract.LocationEntry.CONTENT_URI, null, null, null, null);

        int latIndx = cursor.getColumnIndex(GpsTrackerContract.LocationEntry.COLUMN_COORD_LAT);
        int lonIndx = cursor.getColumnIndex(GpsTrackerContract.LocationEntry.COLUMN_COORD_LONG);
        try {
            while (cursor.moveToNext())
            {
                String lat = cursor.getString(latIndx);
                String lon = cursor.getString(lonIndx);
                mCoords.add(new PointF(Float.parseFloat(lat), Float.parseFloat(lon)));
            }
        } finally {
            cursor.close();
        }

        return mCoords;
    }

}