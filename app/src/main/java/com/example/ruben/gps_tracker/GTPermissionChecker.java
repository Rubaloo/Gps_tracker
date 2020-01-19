package com.example.ruben.gps_tracker;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class GTPermissionChecker {

    public static final int GTPERMISSION_RESULT_GRANTED = 0;
    public static final int GTPERMISSION_RESULT_NOT_GRANTED = -1;



    private Activity mActivity;
    private String mPermission;
    private final int mPermissionRequestCode;

    public GTPermissionChecker()
    {
        mPermissionRequestCode = -1;
    }
    
    public GTPermissionChecker(Activity pActivity,
                               String pPermission,
                               int pPermissionRequestCode)
    {
        mActivity = pActivity;
        mPermission = pPermission;
        mPermissionRequestCode = pPermissionRequestCode;
    }

    public int checkSelfPermission()
    {
        return (ContextCompat.checkSelfPermission(mActivity, mPermission) != PackageManager.PERMISSION_GRANTED) ?
            GTPERMISSION_RESULT_NOT_GRANTED : GTPERMISSION_RESULT_GRANTED;
    }

    public void requestSelfPermission()
    {
        ActivityCompat.requestPermissions(mActivity,
                new String[]{mPermission},
                mPermissionRequestCode);

    }

    public void requestIfNeeded()
    {
        if(checkSelfPermission() == GTPERMISSION_RESULT_NOT_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
                    mPermission))
            {
                System.out.println("Permission already requested: Request permission it's not needed");
            }
            else
            {
                requestSelfPermission();
            }
        }
    }

    public boolean isSelfPermissionGrantedOnRequest(int pRequestCode, int[] grantResults)
    {
        if (pRequestCode == mPermissionRequestCode)
        {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                Toast.makeText(mActivity,
                        "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return false;
    }
}
