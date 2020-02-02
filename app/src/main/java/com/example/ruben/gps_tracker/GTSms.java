package com.example.ruben.gps_tracker;


import android.location.Location;
import android.telephony.SmsMessage;

import java.sql.Timestamp;

public class GTSms implements GTSmsSerializer
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

    GTSms(SmsCode pCode)
    {
        mCode = pCode;
    }

    public SmsCode getCode() {
        return mCode;
    }

    @Override
    public String getData()
    {
        return "";
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
    private Location mLocation;


    GTSmsLocationFormat(SmsMessage pMessage, SmsCode pCode)
    {
        super(pCode);
    }

    public GTSmsLocationFormat(SmsCode pCode, Float mCredit, Float mBattery, String mStatus,
                               Float mSpeed, String mReserved, Timestamp mTimestamp, Location mLocation) {
        super(pCode);
        this.mCredit = mCredit;
        this.mBattery = mBattery;
        this.mStatus = mStatus;
        this.mSpeed = mSpeed;
        this.mReserved = mReserved;
        this.mTimestamp = mTimestamp;
        this.mLocation = mLocation;
    }

    @Override
    public String getData()
    {
        return mStatus + mReserved;
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

    public GTSmsSettings(SmsCode pCode, Boolean mSoundAlarm, Boolean mVisualAlarm,
                         TrackMode mTrackMode, String mPhoneNumber, Timestamp mTimestamp)
    {
        super(pCode);
        this.mSoundAlarm = mSoundAlarm;
        this.mVisualAlarm = mVisualAlarm;
        this.mTrackMode = mTrackMode;
        this.mPhoneNumber = mPhoneNumber;
        this.mTimestamp = mTimestamp;
    }

    public GTSmsSettings(SmsMessage pMessage) {
        super(SmsCode.S);
    }

    @Override
    public String getData()
    {
        return mPhoneNumber;
    }
}

class GTSmsLocation extends GTSmsLocationFormat
{
    public GTSmsLocation(SmsMessage pMessage)
    {
        super(pMessage, SmsCode.L);

    }
}

class GTSmsRefresh extends GTSmsLocationFormat
{
    public GTSmsRefresh(SmsMessage pMessage)
    {
        super(pMessage, SmsCode.R);
    }
}

class GTSmsAlarm extends GTSmsLocationFormat
{
    public GTSmsAlarm(SmsMessage pMessage)
    {
        super(pMessage, SmsCode.A);
    }
}

class GTSmsWarning extends GTSmsLocationFormat
{
    public GTSmsWarning(SmsMessage pMessage)
    {
        super(pMessage, SmsCode.W);
    }
}
