package com.example.ruben.gps_tracker;

public class GTSmsFactory
{
    public GTSms getSms(String smsBody)
    {
        if (smsBody.length() == 0)
        {
            return null;
        }
        String smsCode = smsBody.substring(GTSmsFormat.Location.Code.ordinal(), GTSmsFormat.Location.Credit0.ordinal());
        GTSms.SmsCode code = GTSms.SmsCode.valueOf(smsCode);

        if(code == null)
        {
            return null;
        }
        if(code == GTSms.SmsCode.S)
        {
            return new GTSmsSettings(smsBody);
        }
        else if(code == GTSms.SmsCode.A)
        {
            return new GTSmsAlarm(smsBody);
        }
        else if(code == GTSms.SmsCode.W)
        {
            return new GTSmsWarning(smsBody);
        }
        else if(code == GTSms.SmsCode.R)
        {
            return new GTSmsRefresh(smsBody);
        }
        else if(code == GTSms.SmsCode.L)
        {
            return new GTSmsLocation(smsBody);
        }

        return null;
    }
}
