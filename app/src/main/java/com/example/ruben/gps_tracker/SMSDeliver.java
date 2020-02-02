package com.example.ruben.gps_tracker;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class SMSDeliver {
    private static final String TAG = SMSReceiver.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    private String mPhoneNumber;
    private String mMessage;
    private GTPermissionChecker mSendSmsPC;
    private ActivityReceiver mActivityReceiver;

    public SMSDeliver(Activity pActivity)
    {
        mActivityReceiver = (ActivityReceiver) pActivity;
        mSendSmsPC = new GTPermissionChecker(pActivity, Manifest.permission.SEND_SMS, MY_PERMISSIONS_REQUEST_SEND_SMS);
    }

    public void sendSMSMessage(String phoneNumber, String message)
    {
        mPhoneNumber = phoneNumber;
        mMessage = message;
        if(mSendSmsPC.checkSelfPermission() == GTPermissionChecker.GTPERMISSION_RESULT_GRANTED)
        {
            sendSMSMessage();
        }
        mSendSmsPC.requestIfNeeded();
    }

    private void sendSMSMessage()
    {
        final String SENT = Intent.ACTION_SENDTO;
        final String DELIVERED = Telephony.Sms.Intents.SMS_DELIVER_ACTION;;

        PendingIntent sentIntent = PendingIntent.getBroadcast(mActivityReceiver.getActivity(), 0, new Intent(SENT),0);
        PendingIntent deliveryIntent = PendingIntent.getBroadcast(mActivityReceiver.getActivity(), 0, new Intent(DELIVERED),0);

        mActivityReceiver.getActivity().registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent arg1) {
                String message;
                Boolean succes;
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        message = "SMS sent";
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        message = "Generic failure " + arg1.getIntExtra("errorCode", -1) + " " + arg1.getStringExtra("noDefault");
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        message = "No service";
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        message = "Null PDU";
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        message = "Radio off";
                        break;
                    default:
                        message = SENT + " default";
                }
                Log.d(TAG, message);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        }, new IntentFilter(SENT));

        mActivityReceiver.getActivity().registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent arg1) {
                String message;
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        message = "SMS delivered";
                        break;
                    case Activity.RESULT_CANCELED:
                        message = "SMS not delivered";
                        break;
                    default:
                        message = DELIVERED + " default";
                }

                Log.d(TAG, message);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

            }
        }, new IntentFilter(DELIVERED));

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(mPhoneNumber, null, mMessage, sentIntent, deliveryIntent);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(mSendSmsPC.isSelfPermissionGrantedOnRequest(requestCode, grantResults))
        {
            sendSMSMessage();
        }
    }
}
