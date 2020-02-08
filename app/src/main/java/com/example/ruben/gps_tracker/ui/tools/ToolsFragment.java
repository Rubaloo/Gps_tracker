package com.example.ruben.gps_tracker.ui.tools;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.ruben.gps_tracker.ActivityReceiver;
import com.example.ruben.gps_tracker.GTSms;
import com.example.ruben.gps_tracker.R;
import com.example.ruben.gps_tracker.SMSDeliver;
import com.example.ruben.gps_tracker.SmsBuilder;


public class ToolsFragment extends PreferenceFragmentCompat
{

    public ActivityReceiver mActivityReceiver;
    public SmsBuilder mSmsBuilder;

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        mActivityReceiver = (ActivityReceiver) context;
        mSmsBuilder = new SmsBuilder((Activity) context);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
    {
        setPreferencesFromResource(R.xml.preference_main, rootKey);

        Preference button = findPreference(getString(R.string.settings_key_save_button));
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                Toast.makeText(getActivity(), R.string.toast_sending_message, Toast.LENGTH_LONG).show();

                SMSDeliver smsDeliver = mActivityReceiver.getSmsDeliver();
                String trackerPhone = getString(R.string.preference_key_tracker_phone_number);
                GTSms sms = mSmsBuilder.getSettingsSms();

                smsDeliver.sendGtSms(trackerPhone, sms);
                return true;
            }
        });
    }
}