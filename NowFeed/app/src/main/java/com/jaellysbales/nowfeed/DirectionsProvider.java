package com.jaellysbales.nowfeed;

import android.content.Context;

/**
 * Created by jaellysbales on 6/26/15.
 */
public class DirectionsProvider {

    private Context myContext;
    public final static String URL = "";

    public DirectionsProvider(Context context){
        myContext = context;
    }

    // Takes coordinates of start and end points and returns URL (string) for Directions API request.
    public String makeUrl (double startLat, double startLng, double endLat, double endLng) {
        StringBuilder urlStr = new StringBuilder();

        // Make Directions API request, ask for JSON output.
        urlStr.append("https://maps.googleapis.com/maps/api/directions/json");
        urlStr.append("?origin="); // Start point
        urlStr.append(Double.toString(startLat));
        urlStr.append(",");
        urlStr.append(Double.toString(startLng));
        urlStr.append("&destination="); // End point
        urlStr.append(Double.toString(endLat));
        urlStr.append(",");
        urlStr.append(Double.toString(endLng));

        // For the sake of simplicity (...), I'm limiting myself to subway transit.
        urlStr.append("&sensor=false&mode=transit&alternatives=true&transit_mode=subway&key=PUT_YOUR_KEY_HERE");
        return urlStr.toString();
    }
}
