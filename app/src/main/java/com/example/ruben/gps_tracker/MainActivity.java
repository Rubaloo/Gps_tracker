package com.example.ruben.gps_tracker;

import com.example.ruben.gps_tracker.data.GpsTrackerDbHelper;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.location.Location;
import android.net.Uri;
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

import com.example.ruben.gps_tracker.data.GpsTrackerContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements SMSReceiver.Listener, ActivityReceiver {
    private static final String TAG = SMSReceiver.class.getSimpleName();
    private AppBarConfiguration mAppBarConfiguration;
    private GpsTrackerDbHelper mDbHelper;

    private SMSDeliver mSmsDeliver;
    private SMSReceiver mSmsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        /*// Create a Constraints object that defines when the task should run
        Constraints constraints = new Constraints.Builder()
                .set
                .setRequiresCharging(true)
                .build();

        OneTimeWorkRequest compressionWork =
                new OneTimeWorkRequest.Builder(CompressWorker.class)
                        .setConstraints(constraints)
                        .build();*/
        mDbHelper = new GpsTrackerDbHelper(this);
        mDbHelper.getWritableDatabase(); //Force database creation just for test purposes, to be removed in the future
        mSmsReceiver = new SMSReceiver(this);
        try {
            Log.d(TAG, Boolean.toString(mSmsReceiver.addListener(this)));
        }
        catch (Exception e)
        {
            Log.d(TAG, e.getMessage());
        }


        mSmsDeliver = new SMSDeliver(this);
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
    protected void onStart()
    {
        super.onStart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        mSmsDeliver.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onSmsReceived(String address, String body)
    {
        GTSms sms = null;
        if(address.equals(R.string.preference_key_tracker_phone_number))
        {
            GTSmsFactory fct = new GTSmsFactory();
            sms = fct.getSms(body);
            dispatch(sms);
        }
        Log.d(TAG, "sms received");
        Toast.makeText(MainActivity.this, "sms received", Toast.LENGTH_LONG).show();
    }

    private void dispatch(GTSms pSms)
    {
        if(pSms instanceof GTSmsLocation)
        {
            addLocation((GTSmsLocation) pSms);
        }
    }

    private void addLocation(GTSmsLocation pSmsLoc)
    {
        Location loc = pSmsLoc.getLocation1();

        double lat = loc.getLatitude();
        double lon = loc.getLongitude();

        //Save new data
        ContentValues locationValues = new ContentValues();
        locationValues.put(GpsTrackerContract.LocationEntry.COLUMN_COORD_LAT, String.valueOf(lat));
        locationValues.put(GpsTrackerContract.LocationEntry.COLUMN_COORD_LONG, String.valueOf(lon));

        // Finally, insert location data into the database.
        Uri insertedUri = getContentResolver().insert(
                GpsTrackerContract.LocationEntry.CONTENT_URI,
                locationValues
        );
    }

    @Override
    public Activity getActivity()
    {
        return this;
    }

    @Override
    public SMSDeliver getSmsDeliver()
    {
        return mSmsDeliver;
    }

    @Override
    public ContentResolver getCntentResolver()
    {
        return this.getContentResolver();
    }
}
