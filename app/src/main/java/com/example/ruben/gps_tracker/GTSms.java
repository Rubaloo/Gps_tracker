package com.example.ruben.gps_tracker;


import android.content.ContentValues;
import android.util.Log;

import com.example.ruben.gps_tracker.data.GpsTrackerContract;
import com.example.ruben.gps_tracker.ui.IPersistable;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GTSms implements IPersistable {
    public enum SmsCode
    {
        L, // Location
        W, // Warning
        A, // Alarm
        R, // Refresh
        S  // Settings
    };

    private SmsCode mCode;

    GTSms(GTSms.SmsCode pCode) {
        mCode = pCode;
    }

    public SmsCode getCode()
    {
        return mCode;
    }

    public String toString()
    {
        return mCode.toString();
    }

    @Override
    public ContentValues toContentValues() {
        return null;
    }
}

class GTSmsLocationFormat extends GTSms
{
    private static final String TAG = GTSmsLocationFormat.class.getSimpleName();

    private Integer mCredit;
    private Integer mBattery;
    private String mStatus;
    private Integer mSpeed;
    private Integer mReserved;
    private String mTimestamp;

    private Integer mLongitude1, mLatitude1;
    private Integer mLongitude2, mLatitude2;
    private Integer mLongitude3, mLatitude3;
    private Integer mLongitude4, mLatitude4;

    GTSmsLocationFormat(SmsCode pCode, String pBody)
    {

        super(pCode);
        String creditStr = pBody.substring(GTSmsFormat.getIndex(GTSmsFormat.Location.Credit0), GTSmsFormat.getIndex(GTSmsFormat.Location.Credit2) + 1);
        String batteryStr = pBody.substring(GTSmsFormat.getIndex(GTSmsFormat.Location.BatteryStatus0), GTSmsFormat.getIndex(GTSmsFormat.Location.BatteryStatus1) + 1);
        String speedStr = pBody.substring(GTSmsFormat.getIndex(GTSmsFormat.Location.Speed0), GTSmsFormat.getIndex(GTSmsFormat.Location.Speed2) + 1);
        String statusStr = pBody.substring(GTSmsFormat.getIndex(GTSmsFormat.Location.StatusData0), GTSmsFormat.getIndex(GTSmsFormat.Location.StatusData1) + 1);
        String timestampStr = pBody.substring(GTSmsFormat.getIndex(GTSmsFormat.Location.Time0), GTSmsFormat.getIndex(GTSmsFormat.Location.Time11) + 1);

        this.mCredit = Integer.parseInt(creditStr);
        this.mBattery = Integer.parseInt(batteryStr);
        this.mSpeed = Integer.parseInt(speedStr);
        this.mStatus = statusStr;
        //'HHMMSSddmmaa' -> 'yyyy-[m]m-[d]d hh:mm:ss[.f...]'
        String strCurrentDate = timestampStr;
        SimpleDateFormat format = new SimpleDateFormat("hhmmssddMMyy");
        Date newDate = null;
        try {
            newDate = format.parse(strCurrentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        this.mTimestamp = format.format(newDate);

        this.mLongitude1 = this.mLatitude1 = 0;
        this.mLongitude2 = this.mLatitude2 = 0;
        this.mLongitude3 = this.mLatitude3 = 0;
        this.mLongitude4 = this.mLatitude4 = 0;

        try {
            String longitudeStr = pBody.substring(GTSmsFormat.getIndex(GTSmsFormat.Location.Location100), GTSmsFormat.getIndex(GTSmsFormat.Location.Location109) + 1);
            String latitudeStr = pBody.substring(GTSmsFormat.getIndex(GTSmsFormat.Location.Location110), GTSmsFormat.getIndex(GTSmsFormat.Location.Location119) + 1);
            this.mLongitude1 = Integer.valueOf(longitudeStr);
            this.mLatitude1 = Integer.valueOf(latitudeStr);
        }
        catch (Exception e)
        {
            Log.i(TAG, e.getMessage());
        }
        try {
            String longitudeStr = pBody.substring(GTSmsFormat.getIndex(GTSmsFormat.Location.Location200), GTSmsFormat.getIndex(GTSmsFormat.Location.Location209) + 1);
            String latitudeStr = pBody.substring(GTSmsFormat.getIndex(GTSmsFormat.Location.Location210), GTSmsFormat.getIndex(GTSmsFormat.Location.Location219) + 1);
            this.mLongitude2 = Integer.valueOf(longitudeStr);
            this.mLatitude2 = Integer.valueOf(latitudeStr);
        }
        catch (Exception e)
        {
            Log.i(TAG, e.getMessage());
        }
        try {
            String longitudeStr = pBody.substring(GTSmsFormat.getIndex(GTSmsFormat.Location.Location300), GTSmsFormat.getIndex(GTSmsFormat.Location.Location309) + 1);
            String latitudeStr = pBody.substring(GTSmsFormat.getIndex(GTSmsFormat.Location.Location310), GTSmsFormat.getIndex(GTSmsFormat.Location.Location319) + 1);
            this.mLongitude3 = Integer.valueOf(longitudeStr);
            this.mLatitude3 = Integer.valueOf(latitudeStr);
        }
        catch (Exception e)
        {
            Log.i(TAG, e.getMessage());
        }
        try {
            String longitudeStr = pBody.substring(GTSmsFormat.getIndex(GTSmsFormat.Location.Location400), GTSmsFormat.getIndex(GTSmsFormat.Location.Location409) + 1);
            String latitudeStr = pBody.substring(GTSmsFormat.getIndex(GTSmsFormat.Location.Location410), GTSmsFormat.getIndex(GTSmsFormat.Location.Location419) + 1);
            this.mLongitude4 = Integer.valueOf(longitudeStr);
            this.mLatitude4 = Integer.valueOf(latitudeStr);
        }
        catch (Exception e)
        {
            Log.i(TAG, e.getMessage());
        }

        this.mReserved = 0;
    }

    @Override
    public String toString()
    {
        return super.toString() + mCredit.toString()  + mBattery.toString() + mStatus +
                mSpeed.toString() + mReserved + mTimestamp +
                mLatitude1.toString() + mLatitude2.toString() + mLatitude3.toString() + mLatitude4.toString() +
                mLongitude1.toString() + mLongitude2.toString() + mLongitude3.toString() + mLongitude4.toString();
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues cv = new ContentValues(GpsTrackerContract.LocationEntry.NUM_COLUMNS);
        cv.put(GpsTrackerContract.LocationEntry.COLUMN_STATUS, mStatus);
        cv.put(GpsTrackerContract.LocationEntry.COLUMN_CREDIT, mCredit);
        cv.put(GpsTrackerContract.LocationEntry.COLUMN_BATTERY, mBattery);
        cv.put(GpsTrackerContract.LocationEntry.COLUMN_SPEED, mSpeed);
        cv.put(GpsTrackerContract.LocationEntry.COLUMN_COORD_LAT_1, mLatitude1);
        cv.put(GpsTrackerContract.LocationEntry.COLUMN_COORD_LONG_1, mLongitude1);
        cv.put(GpsTrackerContract.LocationEntry.COLUMN_COORD_LAT_2, mLatitude2);
        cv.put(GpsTrackerContract.LocationEntry.COLUMN_COORD_LONG_2, mLongitude2);
        cv.put(GpsTrackerContract.LocationEntry.COLUMN_COORD_LAT_3, mLatitude3);
        cv.put(GpsTrackerContract.LocationEntry.COLUMN_COORD_LONG_3, mLongitude3);
        cv.put(GpsTrackerContract.LocationEntry.COLUMN_COORD_LAT_4, mLatitude4);
        cv.put(GpsTrackerContract.LocationEntry.COLUMN_COORD_LONG_4, mLongitude4);
        cv.put(GpsTrackerContract.LocationEntry.COLUMN_TIMESTAMP, mTimestamp);
        return cv;
    }


}

class GTSmsSettings extends GTSms
{
    public enum TrackMode
    {
        A,
        B,
        C
    };

    private Boolean mSoundAlarm;
    private Boolean mVisualAlarm;
    private TrackMode mTrackMode;
    private String mPhoneNumber;
    private Timestamp mTimestamp;

    public GTSmsSettings(Boolean mSoundAlarm, Boolean mVisualAlarm,
                         TrackMode mTrackMode, String mPhoneNumber,
                         Timestamp mTimestamp)
    {
        super(SmsCode.S);
        this.mSoundAlarm = mSoundAlarm;
        this.mVisualAlarm = mVisualAlarm;
        this.mTrackMode = mTrackMode;
        this.mPhoneNumber = mPhoneNumber;
        this.mTimestamp = mTimestamp;
    }

    public GTSmsSettings(String pBody) {

        super(SmsCode.S);
        String soundStr = pBody.substring(GTSmsFormat.getIndex(GTSmsFormat.Settings.SoundAlarm), GTSmsFormat.getIndex(GTSmsFormat.Settings.SoundAlarm));
        String visualStr = pBody.substring(GTSmsFormat.getIndex(GTSmsFormat.Settings.VisualAlarm), GTSmsFormat.getIndex(GTSmsFormat.Settings.VisualAlarm));
        String trackStr = pBody.substring(GTSmsFormat.getIndex(GTSmsFormat.Settings.AutoTrack), GTSmsFormat.getIndex(GTSmsFormat.Settings.AutoTrack));
        String phoneNumberStr = pBody.substring(GTSmsFormat.getIndex(GTSmsFormat.Settings.UserTelephone0), GTSmsFormat.getIndex(GTSmsFormat.Settings.UserTelephone11));
        String timeStr = pBody.substring(GTSmsFormat.getIndex(GTSmsFormat.Settings.Time0), GTSmsFormat.getIndex(GTSmsFormat.Settings.Time9));
    }

    @Override
    public String toString()
    {
        return super.toString() + mSoundAlarm.toString() + mVisualAlarm.toString() + mTrackMode.toString() + mPhoneNumber + mTimestamp.toString();
    }

    @Override
    public ContentValues toContentValues() {
        return null;
    }
}

class GTSmsRefresh extends GTSmsLocationFormat
{
    public GTSmsRefresh(String pBody)
    {
        super(SmsCode.R, pBody);
    }

    @Override
    public ContentValues toContentValues() {
        return null;
    }
}

class GTSmsAlarm extends GTSmsLocationFormat
{
    public GTSmsAlarm(String pBody)
    {
        super(SmsCode.A, pBody);
    }

    @Override
    public ContentValues toContentValues() {
        return null;
    }

}

class GTSmsWarning extends GTSmsLocationFormat
{
    public GTSmsWarning(String pBody)
    {
        super(SmsCode.W, pBody);
    }

    @Override
    public ContentValues toContentValues() {
        return null;
    }
}

class GTSmsLocation extends GTSmsLocationFormat
{
    public GTSmsLocation(String pBody) { super(SmsCode.L, pBody);}

    @Override
    public ContentValues toContentValues() {
        return super.toContentValues();
    }
}

