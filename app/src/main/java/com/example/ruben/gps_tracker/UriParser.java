package com.example.ruben.gps_tracker;

import android.net.Uri;

import java.util.Set;

public class UriParser {

    public void parse(String text)
    {
        if(isUri(text))
        {
            Uri uri = Uri.parse(text);
            String server = uri.getAuthority();
            String path = uri.getPath();
            String protocol = uri.getScheme();
            Set<String> args = uri.getQueryParameterNames();
        }
    }

    public boolean isUri(String text)
    {
        return true;
    }
}
