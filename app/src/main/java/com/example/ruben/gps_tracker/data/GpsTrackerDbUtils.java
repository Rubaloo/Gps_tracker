package com.example.ruben.gps_tracker.data;

import com.example.ruben.gps_tracker.GTSms;
import com.example.ruben.gps_tracker.SmsReceiver;

import android.content.Context;
import android.util.Log;
import android.net.Uri;

public class GpsTrackerDbUtils {
    private static final String TAG = SmsReceiver.class.getSimpleName();
    public Uri insert(GTSms sms, Context context)
    {
        switch (sms.getCode())
        {
            case A:
                Log.d(TAG, "AlarmSMS received");
                break;
            case L:
                Log.d(TAG, "LocationSMS received");
                return context.getContentResolver().insert(GpsTrackerContract.LocationEntry.CONTENT_URI,  sms.toContentValues());
            case W:
                Log.d(TAG, "WarningSMS received");
                break;
            case R:
                Log.d(TAG, "RefreshSMS received");
                break;
            case S:
                Log.d(TAG, "SettingsSMS received");
                break;
            default:
                Log.e(TAG, "Unkown SMS received");
        }
        return null;
    }
}
