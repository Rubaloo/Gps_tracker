package com.example.ruben.gps_tracker;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.ruben.gps_tracker.data.GpsTrackerContract;

import java.util.ArrayList;
import java.util.List;

public class SMSReceiver extends BroadcastReceiver
{
    private static final String TAG = SMSReceiver.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 1;
    private List<Listener> mListeners;
    private GTPermissionChecker mReceiveSMSPC;
    private ActivityReceiver mActivityReceiver;

    public SMSReceiver()
    {
    }

    public SMSReceiver(Activity pActivity)
    {
        mActivityReceiver = (ActivityReceiver) pActivity;
        mListeners = new ArrayList<Listener>();
        mReceiveSMSPC = new GTPermissionChecker(mActivityReceiver.getActivity(), Manifest.permission.RECEIVE_SMS, MY_PERMISSIONS_REQUEST_RECEIVE_SMS);
        mReceiveSMSPC.requestIfNeeded();
    }


    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION))
        {
            String smsSender = "";
            String smsBody = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            {
                for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent))
                {
                    smsSender = smsMessage.getDisplayOriginatingAddress();
                    smsBody += smsMessage.getMessageBody();
                }
            }
            else
                {
                Bundle smsBundle = intent.getExtras();
                if (smsBundle != null)
                {
                    Object[] pdus = (Object[]) smsBundle.get("pdus");
                    if (pdus == null)
                    {
                        // Display some error to the user
                        Log.e(TAG, "SmsBundle had no pdus key");
                        return;
                    }
                    SmsMessage[] messages = new SmsMessage[pdus.length];
                    for (int i = 0; i < messages.length; i++)
                    {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        smsBody += messages[i].getMessageBody();
                    }
                    smsSender = messages[0].getOriginatingAddress();
                }
            }
        }

        //TODO: Parse sms content (by now Default insert when a sms is received)
        ContentValues cv = new ContentValues(2);
        cv.put(GpsTrackerContract.LocationEntry.COLUMN_COORD_LAT, 10);
        cv.put(GpsTrackerContract.LocationEntry.COLUMN_COORD_LONG, 1000);

        try {
            context.getContentResolver().insert(GpsTrackerContract.LocationEntry.CONTENT_URI, cv);
        }
        catch (Exception e)
        {
            Log.d(TAG, e.getMessage());
        }
    }

    interface Listener
    {
        void onSmsReceived(String sender, String body);
    }

    public boolean addListener(Listener pListener)
    {
        return mListeners.add(pListener);
    };
}
