package com.example.ruben.gps_tracker;

import android.app.Activity;
import android.graphics.PointF;
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

import com.example.ruben.gps_tracker.ui.home.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements SMSReceiver.Listener, ActivityReceiver {
    private static final String TAG = SMSReceiver.class.getSimpleName();
    private AppBarConfiguration mAppBarConfiguration;

    private SMSDeliver mSmsDeliver;
    private SMSReceiver mSmsReceiver;
    private MyNetApi mNetApi;
    private RESTUriParser mRestUriParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSmsReceiver = new SMSReceiver(this);
        mSmsDeliver = new SMSDeliver(this);
        mRestUriParser = new RESTUriParser();
        mNetApi = new MyNetApi();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSmsReceiver.setListener(this);


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
    public void onTextReceived(String text)
    {
        RESTUriParser.ApiRequest ar = mRestUriParser.parse(text);
        PointF geo = mNetApi.getLocation(ar);

        HomeFragment homeFrag = (HomeFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);

        if (homeFrag != null && geo != null)
        {
            homeFrag.updateMapMarker(geo);
        }

        Log.d(TAG, text);
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public SMSDeliver getSmsDeliver() {
        return mSmsDeliver;
    }
}
