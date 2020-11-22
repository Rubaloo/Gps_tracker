package com.example.ruben.gps_tracker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.example.ruben.gps_tracker.data.GpsTrackerContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements SmsReceiver.Listener, ActivityReceiver, SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = SmsReceiver.class.getSimpleName();
    private AppBarConfiguration mAppBarConfiguration;
    private SmsDeliver mSmsDeliver;
    private SmsReceiver mSmsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSmsReceiver = new SmsReceiver(this);
        try {
            Log.d(TAG, Boolean.toString(mSmsReceiver.addListener(this)));
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }


        mSmsDeliver = new SmsDeliver(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* String phoneNumber = getString(R.string.service_provider_number);
                String message = getString(R.string.sms_default_message);

                mSmsDeliver.sendSMSMessage(phoneNumber, message);

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                RESTUriParser p = new RESTUriParser();
                p.parse(RESTUriParser.URL_TEST);
            }
        });

        FloatingActionButton quiet = findViewById(R.id.quiet);
        quiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref =
                        PreferenceManager.getDefaultSharedPreferences(v.getContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.preference_key_ui_mode), getString(R.string.preference_value_ui_mode_quite));
                editor.apply();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    protected void onStart() {
        super.onStart();
        createNotificationChannel();
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onResume() {
        super.onResume();
        FloatingActionButton quiet = findViewById(R.id.quiet);
        quiet.setVisibility((isAlertMode()) ? View.VISIBLE : View.GONE);
        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        mSmsDeliver.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onSmsReceived(String address, String body) {
        GTSms sms = null;
        if (address.equals(R.string.preference_key_tracker_phone_number)) {
            GTSmsFactory fct = new GTSmsFactory();
            sms = fct.getSms(body);
            dispatch(sms);
        }
        Log.d(TAG, "sms received");
        Toast.makeText(MainActivity.this, "sms received", Toast.LENGTH_LONG).show();
    }

    @Override
    public Resources.Theme getTheme() {
        Integer themeResId = (isAlertMode()) ? R.style.AppTheme_Alarm : R.style.AppTheme_NoActionBar;
        Resources.Theme theme = super.getTheme();
        theme.applyStyle(themeResId, true);
        return theme;
    }

    private void dispatch(GTSms pSms) {
        if (pSms instanceof GTSmsLocation) {
            addLocation((GTSmsLocation) pSms);
        }
    }

    private void addLocation(GTSmsLocation pSmsLoc) {
        // Insert location data into the database.
        Uri insertedUri = getContentResolver().insert(
                GpsTrackerContract.LocationEntry.CONTENT_URI,
                pSmsLoc.toContentValues()
        );
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public SmsDeliver getSmsDeliver() {
        return mSmsDeliver;
    }

    @Override
    public ContentResolver getCntentResolver() {
        return this.getContentResolver();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.preference_key_ui_mode))) {
            Intent intent = getIntent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            startActivity(intent);
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            String id = getString(R.string.CHANNEL_ID);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(id, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    enum UIMode {
        Quiet,
        Alert
    }

    private UIMode getUIMode() {
        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);
        String uiModeKey = this.getString(R.string.preference_key_ui_mode);
        String uiMode = sharedPref.getString(uiModeKey, getString(R.string.preference_value_ui_mode_quite));
        String alertMode = getString(R.string.preference_value_ui_mode_alert);
        return (uiMode.equals(alertMode)) ? UIMode.Alert : UIMode.Quiet;
    }

    public boolean isAlertMode()
    {
        return getUIMode().equals(UIMode.Alert);
    }
}