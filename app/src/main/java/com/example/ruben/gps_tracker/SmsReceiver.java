package com.example.ruben.gps_tracker;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.ruben.gps_tracker.data.GpsTrackerDbHelper;
import com.example.ruben.gps_tracker.data.GpsTrackerDbUtils;
import com.example.ruben.gps_tracker.data.GpsTrackerProvider;

import java.util.ArrayList;
import java.util.List;

public class SmsReceiver extends BroadcastReceiver
{
    private static final String TAG = SmsReceiver.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_RECEIVE_SMS = 1;
    private List<Listener> mListeners;
    private GTPermissionChecker mReceiveSMSPC;
    private ActivityReceiver mActivityReceiver;

    public SmsReceiver()
    {
    }

    public SmsReceiver(Activity pActivity)
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
            else {
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
            GTSmsFactory smsFactory = new GTSmsFactory();
            GTSms receivedSMS = smsFactory.getSms(smsBody);
            GpsTrackerDbUtils dbUtils = new GpsTrackerDbUtils();
            switch (receivedSMS.getCode())
            {
                case A:
                    Log.d(TAG, "AlarmSMS received");
                    break;
                case L:
                    Log.d(TAG, "LocationSMS received");
                    dbUtils.insert(receivedSMS, context);
                    break;
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
