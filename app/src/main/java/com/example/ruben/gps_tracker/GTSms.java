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
    private Location mLocation1;
    private Location mLocation2;
    private Location mLocation3;
    private Location mLocation4;

    GTSmsLocationFormat(SmsCode pCode, String pBody)
    {

        super(pCode);
        String creditStr = pBody.substring(GTSmsFormat.Location.Credit0.ordinal(), GTSmsFormat.Location.Credit2.ordinal());
        String batteryStr = pBody.substring(GTSmsFormat.Location.BatteryStatus0.ordinal(), GTSmsFormat.Location.BatteryStatus2.ordinal());
        String speedStr = pBody.substring(GTSmsFormat.Location.Speed0.ordinal(), GTSmsFormat.Location.Speed2.ordinal());
        String statusStr = pBody.substring(GTSmsFormat.Location.StatusData0.ordinal(), GTSmsFormat.Location.StatusData2.ordinal());
        String timestampStr = pBody.substring(GTSmsFormat.Location.Time0.ordinal(), GTSmsFormat.Location.Time9.ordinal());
        String location1Str = pBody.substring(GTSmsFormat.Location.Location100.ordinal(), GTSmsFormat.Location.Location119.ordinal());
        String location2Str = pBody.substring(GTSmsFormat.Location.Location200.ordinal(), GTSmsFormat.Location.Location219.ordinal());
        String location3Str = pBody.substring(GTSmsFormat.Location.Location300.ordinal(), GTSmsFormat.Location.Location319.ordinal());
        String location4Str = pBody.substring(GTSmsFormat.Location.Location400.ordinal(), GTSmsFormat.Location.Location419.ordinal());

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
        String soundStr = pBody.substring(GTSmsFormat.Settings.SoundAlarm.ordinal(), GTSmsFormat.Settings.SoundAlarm.ordinal());
        String visualStr = pBody.substring(GTSmsFormat.Settings.VisualAlarm.ordinal(), GTSmsFormat.Settings.VisualAlarm.ordinal());
        String trackStr = pBody.substring(GTSmsFormat.Settings.AutoTrack.ordinal(), GTSmsFormat.Settings.AutoTrack.ordinal());
        String phoneNumberStr = pBody.substring(GTSmsFormat.Settings.UserTelephone0.ordinal(), GTSmsFormat.Settings.UserTelephone11.ordinal());
        String timeStr = pBody.substring(GTSmsFormat.Settings.Time0.ordinal(), GTSmsFormat.Settings.Time9.ordinal());
    }

    @Override
    public String toString()
    {
        return super.toString() + mSoundAlarm.toString() + mVisualAlarm.toString() + mTrackMode.toString() + mPhoneNumber + mTimestamp.toString();
    }
}

class GTSmsLocation extends GTSmsLocationFormat
{
    public GTSmsLocation(String pBody) { super(SmsCode.L, pBody);}
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
