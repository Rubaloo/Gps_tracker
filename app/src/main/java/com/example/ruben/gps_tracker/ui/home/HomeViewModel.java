package com.example.ruben.gps_tracker.ui.home;

import android.content.ContentResolver;
import android.graphics.PointF;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel
{

    private MutableLiveData<ArrayList<PointF>> mTrackerPath;
    private HomeRepository mRepository;

    public HomeViewModel()
    {
        mTrackerPath = new MutableLiveData<ArrayList<PointF>>();
        mRepository = new HomeRepository();
        mTrackerPath = mRepository.getTrackerPath();
    }

    public MutableLiveData<ArrayList<PointF>> getTrackerPath()
    {
        return mTrackerPath;
    }

    public void setContentResolver(ContentResolver pContentResolver)
    {
        mRepository.setContentResolver(pContentResolver);
        mRepository.registerObservers();
    }

    public void unregisterRepoObservers()
    {
        mRepository.unregisterObservers();
    }
}