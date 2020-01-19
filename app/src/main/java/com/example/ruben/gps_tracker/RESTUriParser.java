package com.example.ruben.gps_tracker;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;



public class RESTUriParser {
    static final String URL_TEST = "http://gpstracker.com/location/current?lat=37.4219999&lng=122.0862462";
    private static final String TAG = RESTUriParser.class.getSimpleName();

    public class ApiRequest {
        private String mServer;
        private String mPath;
        private String mProtocol;
        private Map<String,String> mParams;

        public String getPath()
        {
            return mPath;
        }

        public Map<String, String> getArgs()
        {
            return mParams;
        }

        ApiRequest(String pServer, String pPath, String pProtocol, Map<String, String> pParams)
        {
            mServer = pServer;
            mPath = pPath;
            mProtocol = pProtocol;
            mParams = pParams;
        }

        @NonNull
        @Override
        public String toString()
        {
            return mServer + " " + mPath +  " " + mProtocol + " " + mParams.toString();
        }
    }

    public ApiRequest parse(String pText)
    {
        ApiRequest apiRequest = null;
        if(isUri(pText))
        {
            Uri uri = Uri.parse(pText);
            String server = uri.getAuthority();
            String path = uri.getPath();
            String protocol = uri.getScheme();
            Map<String, String> params = new HashMap<String,String>();

            Set<String> parNames = uri.getQueryParameterNames();
            for(String parName : parNames)
            {
                String parValue = uri.getQueryParameter(parName);
                params.put(parName, parValue);
            }

            apiRequest = new ApiRequest(server, path, protocol, params);
            Log.d(TAG, apiRequest.toString());

        }
        return apiRequest;
    }

    public boolean isUri(String text) { return true; }

}
