package com.example.ruben.gps_tracker;

import android.app.Activity;
import android.content.ContentResolver;

public interface ActivityReceiver
{
    Activity getActivity();
    SmsDeliver getSmsDeliver();
    ContentResolver getCntentResolver();
}
