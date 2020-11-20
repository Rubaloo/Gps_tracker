package com.example.ruben.gps_tracker;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

public class IncomingCallReceiver extends PhoneCallReceiver
{
    private static final String TAG = IncomingCallReceiver.class.getSimpleName();

    @Override
    protected void onIncomingCallStarted(Context ctx, String number, Date start) {
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start)
    {
        String msg = "Missed call from " + number;
        Log.d(TAG, msg);
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }


}
