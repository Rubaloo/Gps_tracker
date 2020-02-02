package com.example.ruben.gps_tracker;

import android.telephony.SmsMessage;

public class GTSmsFactory
{
    public GTSms getSms(SmsMessage pMessage){

        GTSms.SmsCode code = null;
        if(code == null){
            return null;
        }
        if(code == GTSms.SmsCode.S)
        {
            return new GTSmsSettings(pMessage);
        }
        else if(code == GTSms.SmsCode.A)
        {
            return new GTSmsAlarm(pMessage);
        }
        else if(code == GTSms.SmsCode.W)
        {
            return new GTSmsWarning(pMessage);
        }
        else if(code == GTSms.SmsCode.R)
        {
            return new GTSmsRefresh(pMessage);
        }

        return null;
    }
}
