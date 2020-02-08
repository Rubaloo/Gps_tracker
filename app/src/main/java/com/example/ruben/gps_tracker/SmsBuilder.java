package com.example.ruben.gps_tracker;

import android.app.Activity;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import java.sql.Timestamp;

public class SmsBuilder
{
    private ActivityReceiver mActivityReceiver;

    public SmsBuilder(Activity pActivity)
    {
        mActivityReceiver = (ActivityReceiver) pActivity;
    }

    public GTSmsSettings getSettingsSms()
    {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(mActivityReceiver.getActivity());

        String prefPhoneKey =  mActivityReceiver.getActivity().getString(R.string.preference_key_tracker_phone_number);
        String prefVisualAlarmKey =  mActivityReceiver.getActivity().getString(R.string.preference_key_visual_alarm);
        String prefSoundAlarmKey =  mActivityReceiver.getActivity().getString(R.string.preference_key_sound_alarm);
        String prefTrackerModeKey =  mActivityReceiver.getActivity().getString(R.string.preference_key_sound_alarm);

        String trackerPhone = sharedPreferences.getString(prefPhoneKey, "");
        Boolean visualAlarm = sharedPreferences.getBoolean(prefVisualAlarmKey, false);
        Boolean soundAlarm = sharedPreferences.getBoolean(prefSoundAlarmKey,false);
        String trackerMode = sharedPreferences.getString(prefTrackerModeKey, "");


        int time = (int) (System.currentTimeMillis());
        Timestamp tsTemp = new Timestamp(time);


        GTSmsSettings sms = new GTSmsSettings(soundAlarm, visualAlarm, GTSmsSettings.TrackMode.A, trackerPhone, tsTemp);

        return sms;
    }
}
