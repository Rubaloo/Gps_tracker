package com.example.ruben.gps_tracker.ui.home;

import android.graphics.PointF;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel
{

    private MutableLiveData<PointF> mTrackerPosition;

    public HomeViewModel()
    {
        mTrackerPosition = new MutableLiveData<>();
        mTrackerPosition.setValue(new PointF(0,0));
    }

    public void setTrackerPosition(PointF pTrackerPosition)
    {
        mTrackerPosition.setValue(pTrackerPosition);
    }

    public LiveData<PointF> getTrackerPosition()
    {
        return mTrackerPosition;
    }
}