package com.example.ruben.gps_tracker.ui.home;

import android.graphics.PointF;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class HomeViewModel extends ViewModel
{

    private MutableLiveData<List<PointF>> mTrackerPath = new MutableLiveData<List<PointF>>();

    public HomeViewModel()
    {
    }

    public void addTrackerSubPath(List<PointF> mTrackerSubPath)
    {
        mTrackerPath.getValue().addAll(mTrackerSubPath);
    }

    public void restartTrackerPath(PointF pStartPosition)
    {
        mTrackerPath.getValue().clear();
        if(pStartPosition != null)
        {
            mTrackerPath.getValue().add(pStartPosition);
        }
    }

    public List<PointF> getTrackerPath()
    {
        return mTrackerPath.getValue();
    }
}