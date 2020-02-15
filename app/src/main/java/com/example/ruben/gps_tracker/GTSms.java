package com.example.ruben.gps_tracker;


import android.location.Location;

import java.sql.Timestamp;
public class GTSms
{


    public enum SmsCode
    {
        L, // Location
        W, // Warning
        A, // Alarm
        R, // Refresh
        S  // Settings
    };

    private SmsCode mCode;

    GTSms(GTSms.SmsCode pCode)
    {
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

}

class GTSmsLocationFormat extends GTSms
{

    private Float mCredit;
    private Float mBattery;
    private String mStatus;
    private Float mSpeed;
    private String mReserved;
    private Timestamp mTimestamp;

    public Location getLocation1() {
        return mLocation1;
    }

    private Location mLocation1;
    private Location mLocation2;
    private Location mLocation3;
    private Location mLocation4;

    GTSmsLocationFormat(SmsCode pCode, String pBody)
    {

        super(pCode);
        String creditStr = pBody.substring(GTSmsFormat.getIndex(GTSmsFormat.Location.Credit0), GTSmsFormat.getIndex(GTSmsFormat.Location.Credit2));
        String batteryStr = pBody.substring(GTSmsFormat.getIndex(GTSmsFormat.Location.BatteryStatus0), GTSmsFormat.getIndex(GTSmsFormat.Location.BatteryStatus2));
        String speedStr = pBody.substring(GTSmsFormat.getIndex(GTSmsFormat.Location.Speed0), GTSmsFormat.getIndex(GTSmsFormat.Location.Speed2));
        String statusStr = pBody.substring(GTSmsFormat.getIndex(GTSmsFormat.Location.StatusData0), GTSmsFormat.getIndex(GTSmsFormat.Location.StatusData2));
        String timestampStr = pBody.substring(GTSmsFormat.getIndex(GTSmsFormat.Location.Time0), GTSmsFormat.getIndex(GTSmsFormat.Location.Time9));
        String location1Str = pBody.substring(GTSmsFormat.getIndex(GTSmsFormat.Location.Location100), GTSmsFormat.getIndex(GTSmsFormat.Location.Location119));
        String location2Str = pBody.substring(GTSmsFormat.getIndex(GTSmsFormat.Location.Location200), GTSmsFormat.getIndex(GTSmsFormat.Location.Location219));
        String location3Str = pBody.substring(GTSmsFormat.getIndex(GTSmsFormat.Location.Location300), GTSmsFormat.getIndex(GTSmsFormat.Location.Location319));
        String location4Str = pBody.substring(GTSmsFormat.getIndex(GTSmsFormat.Location.Location400), GTSmsFormat.getIndex(GTSmsFormat.Location.Location419));

    }

    public GTSmsLocationFormat(SmsCode pCode, Float mCredit, Float mBattery, String mStatus,
                               Float mSpeed, String mReserved, Timestamp mTimestamp,
                               Location mLocation1, Location mLocation2, Location mLocation3, Location mLocation4) {
        super(pCode);
        this.mCredit = mCredit;
        this.mBattery = mBattery;
        this.mStatus = mStatus;
        this.mSpeed = mSpeed;
        this.mReserved = mReserved;
        this.mTimestamp = mTimestamp;
        this.mLocation1 = mLocation1;
        this.mLocation2 = mLocation2;
        this.mLocation3 = mLocation3;
        this.mLocation4 = mLocation4;
    }

    @Override
    public String toString()
    {
        return super.toString() + mCredit.toString()  + mBattery.toString() + mStatus +
                mSpeed.toString() + mReserved + mTimestamp.toString() +
                mLocation1.toString() + mLocation2.toString() + mLocation3.toString() + mLocation4.toString();
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
}

class GTSmsRefresh extends GTSmsLocationFormat
{
    public GTSmsRefresh(String pBody)
    {
        super(SmsCode.R, pBody);
    }
}

class GTSmsAlarm extends GTSmsLocationFormat
{
    public GTSmsAlarm(String pBody)
    {
        super(SmsCode.A, pBody);
    }

}

class GTSmsWarning extends GTSmsLocationFormat
{
    public GTSmsWarning(String pBody)
    {
        super(SmsCode.W, pBody);
    }
}
