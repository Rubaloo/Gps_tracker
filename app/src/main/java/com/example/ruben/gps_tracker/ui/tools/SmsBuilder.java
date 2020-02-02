package com.example.ruben.gps_tracker.ui.tools;

import android.app.Activity;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.example.ruben.gps_tracker.ActivityReceiver;
import com.example.ruben.gps_tracker.R;

public class SmsBuilder
{
    private ActivityReceiver mActivityReceiver;

    public SmsBuilder(Activity pActivity)
    {
        mActivityReceiver = (ActivityReceiver) pActivity;
    }

    public String getSmsPhoneFromPreferences()
    {
        String deliverPhone = "";
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(mActivityReceiver.getActivity());
        String prefPhoneKey =  mActivityReceiver.getActivity().getString(R.string.preference_key_tracker_phone_number);

        deliverPhone = sharedPreferences.getString(prefPhoneKey, "");
        return  deliverPhone;
    }

    public String getSettingsSmsDataFromPreferences()
    {
        String data = "";

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(mActivityReceiver.getActivity());

        String prefVisualAlarmKey =  mActivityReceiver.getActivity().getString(R.string.preference_key_visual_alarm);
        String prefSoundAlarmKey =  mActivityReceiver.getActivity().getString(R.string.preference_key_sound_alarm);
        String prefTrackerModeKey =  mActivityReceiver.getActivity().getString(R.string.preference_key_sound_alarm);

        String visualAlarm = sharedPreferences.getString(prefVisualAlarmKey, "");
        String soundAlarm = sharedPreferences.getString(prefSoundAlarmKey, "");
        String trackerMode = sharedPreferences.getString(prefTrackerModeKey, "");

        data = soundAlarm + visualAlarm + trackerMode;

        return data;
    }
}
