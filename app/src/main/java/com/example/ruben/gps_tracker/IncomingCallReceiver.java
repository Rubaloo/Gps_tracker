package com.example.ruben.gps_tracker;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onMissedCall(Context ctx, String number, Date start)
    {
        String msg = "Missed call from " + number;
        Log.d(TAG, msg);
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();

        //TODO: Become compatible with API 24 != getDefaultSharedPreferencesName
        String name = PreferenceManager.getDefaultSharedPreferencesName(ctx);
        Log.d(TAG, "Name shared preferences"  + name);
        SharedPreferences sharedPref = ctx.getSharedPreferences(name, Context.MODE_PRIVATE);
        String trackerPhone = sharedPref.getString(ctx.getString(R.string.preference_key_tracker_phone_number), "");

        if(number.equals(trackerPhone))
        {
            String uiModeKey = ctx.getString(R.string.preference_key_ui_mode);
            String uiModeAlert = sharedPref.getString(ctx.getString(R.string.preference_value_ui_mode_alert), "alert");
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(uiModeKey, uiModeAlert);
            editor.apply();

            // Create an explicit intent for an Activity in your app
            Intent intent = new Intent(ctx, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, intent, 0);


            //TODO: https://developer.android.com/training/notify-user/expanded
            NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, ctx.getString(R.string.CHANNEL_ID))
                    .setSmallIcon(R.drawable.common_google_signin_btn_text_dark)
                    .setContentTitle("Alert!")
                    .setContentText("It seems that somebody wants to steal your bike!")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ctx);

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(0, builder.build());
        }
    }
}
