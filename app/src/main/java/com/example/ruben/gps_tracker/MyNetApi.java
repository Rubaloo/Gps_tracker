package com.example.ruben.gps_tracker;

import android.graphics.PointF;

import java.util.Map;

public class MyNetApi {


    static String LOCATION_RESOURCE = "location";
    static String LOCATION_ARG_LATITUDE = "lat";
    static String LOCATION_ARG_LONGITUDE = "lng";


    public boolean isLocationResource(RESTUriParser.ApiRequest pApiRequest)
    {
        return (pApiRequest.getPath().contains(LOCATION_RESOURCE));
    }

    public PointF getLocation(RESTUriParser.ApiRequest pApiRequest)
    {
        PointF geoCord = null;
        if(!isLocationResource(pApiRequest)) return geoCord;

        Map<String,String> locationArgs = pApiRequest.getArgs();
        String lat = locationArgs.get(LOCATION_ARG_LATITUDE);
        String lng = locationArgs.get(LOCATION_ARG_LONGITUDE);

        geoCord = new PointF(Float.parseFloat(lat),Float.parseFloat(lng));
        return geoCord;
    }
}
