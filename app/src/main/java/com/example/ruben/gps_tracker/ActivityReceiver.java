package com.example.ruben.gps_tracker;

import android.app.Activity;
import android.content.ContentResolver;

public interface ActivityReceiver
{
    Activity getActivity();
    SMSDeliver getSmsDeliver();
    ContentResolver getCntentResolver();
}
