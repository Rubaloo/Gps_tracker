package com.example.ruben.gps_tracker;

import android.app.Activity;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.example.ruben.gps_tracker.ActivityReceiver;
import com.example.ruben.gps_tracker.GTSms;
import com.example.ruben.gps_tracker.R;
import com.example.ruben.gps_tracker.ui.tools.GTSms;

public class SmsBuilder
{
    private ActivityReceiver mActivityReceiver;

    public SmsBuilder(Activity pActivity)
    {
        mActivityReceiver = (ActivityReceiver) pActivity;
    }

    public GTSmsSettings getSettingsSms()
    {
        GTSmsSettings sms;

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(mActivityReceiver.getActivity());

        String prefPhoneKey =  mActivityReceiver.getActivity().getString(R.string.preference_key_tracker_phone_number);
        String prefVisualAlarmKey =  mActivityReceiver.getActivity().getString(R.string.preference_key_visual_alarm);
        String prefSoundAlarmKey =  mActivityReceiver.getActivity().getString(R.string.preference_key_sound_alarm);
        String prefTrackerModeKey =  mActivityReceiver.getActivity().getString(R.string.preference_key_sound_alarm);

        String deliverPhone = sharedPreferences.getString(prefPhoneKey, "");
        String visualAlarm = sharedPreferences.getString(prefVisualAlarmKey, "");
        String soundAlarm = sharedPreferences.getString(prefSoundAlarmKey, "");
        String trackerMode = sharedPreferences.getString(prefTrackerModeKey, "");



        return data;
    }
}
