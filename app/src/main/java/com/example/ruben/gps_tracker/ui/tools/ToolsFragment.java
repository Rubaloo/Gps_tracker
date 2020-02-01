package com.example.ruben.gps_tracker.ui.tools;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import com.example.ruben.gps_tracker.R;

public class ToolsFragment extends PreferenceFragmentCompat
{
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preference_main, rootKey);
    }
}