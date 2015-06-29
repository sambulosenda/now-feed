package com.jaellysbales.nowfeed;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by jaellysbales on 6/29/15.
 */
public class GetRouteJsonData extends GetRawData {

    private String LOG_TAG = GetRouteJsonData.class.getSimpleName();
    private List<Route> routes;
    private Uri destinationUri;

    public GetRouteJsonData(double startLat, double startLng, double endLat, double endLng) {
        super(null);
        createAndUpdateUri(startLat, startLng, endLat, endLng);
    }

    // Takes coordinates of start and end points and returns URL (string) for Directions API request.
    public boolean createAndUpdateUri(double startLat, double startLng, double endLat, double endLng) {

        // These are required or never change
        final String MAPS_API_BASE_URL = "https://maps.googleapis.com/maps/api/directions/json";
        final String PARAM_ORIGIN = "origin=";
        final String PARAM_DESTINATION = "destination=";
        final String KEY = "key=";

        // Optional parameters
        final String MODE = "mode=";
        final String ALTERNATIVES = "alternatives=";
        final String TRANSIT_MODE = "transit_mode=";

        // Locations
        String startLatLng = startLat + "," + startLng;
        String endLatLng = endLat + "," + endLng;

        // Building the Directions API request
        destinationUri = Uri.parse(MAPS_API_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_ORIGIN, startLatLng)
                .appendQueryParameter(PARAM_DESTINATION, endLatLng)
                .appendQueryParameter(MODE, "transit") // For simplicity's sake, limiting myself to one mode of transit...
                .appendQueryParameter(ALTERNATIVES, "false") // ...and one route, for now.
                .appendQueryParameter(TRANSIT_MODE, "subway")
                .appendQueryParameter(KEY, "PUT_YOUR_KEY_HERE")
                .build();

        Log.d(LOG_TAG, destinationUri.toString());

        return destinationUri != null;
    }

    public void processResult() {
        // If no file, don't process
        if (getDownloadStatus() != DownloadStatus.OK) {
            Log.e(LOG_TAG, "Error downloading raw file");
            return;
        }

        final String MAPS_ROUTES = "routes"; // array
        final String MAPS_BOUNDS = "bounds";
        final String MAPS_BOUNDS_NE = "northeast";
        final String MAPS_BOUNDS_SW = "southwest";
        final String MAPS_LEGS = "legs"; // array
        final String MAPS_TRIP_DISTANCE = "distance";
        final String MAPS_TRIP_DURATION = "trip duration";
        final String MAPS_START_ADDRESS = "start address";
        final String MAPS_END_ADDRESS = "end address";
        final String MAPS_OVERVIEW_POLYLINE = "overview_polyline";
        final String MAPS_POLYLINE_POINTS = "points";

        // Capture and retrieve data
        try {
            JSONObject jsonData = new JSONObject(getData());
            JSONArray routesArray = jsonData.getJSONArray(MAPS_ROUTES);

            // get first route in routes array
            // in that route, get bounds [ne, sw]
            // in that route, get legs [distance, duration, start/end addresses]
            // in that route, get polyline points

        } catch (JSONException jsone) {
            jsone.printStackTrace();
            Log.v(LOG_TAG, "Error processing JSON data");
        }
    }

    public class DownloadJsonData extends DownloadRawData {

        protected void onPostExecute(String webData) {
            super.onPostExecute(webData);
            processResult();
        }

        protected String doInBackground(String... params) {
            return super.doInBackground(params);
        }
    }
}
