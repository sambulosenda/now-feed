package com.jaellysbales.nowfeed;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaellysbales on 6/29/15.
 */
public class GetRouteJsonData extends GetRawData {
    public static final String MAP_ROUTE_DATA_AVAILABLE = "GetRouteJsonData.mapRouteReady";
    private String LOG_TAG = GetRouteJsonData.class.getSimpleName();
    private List<Route> routes;
    private Uri destinationUri;

    public GetRouteJsonData(double startLat, double startLng, String endAddress) {
        super(null);
        createAndUpdateUri(startLat, startLng, endAddress);
        routes = new ArrayList<>();
        Log.d(LOG_TAG, destinationUri.toString());
    }

    public void execute() {
        super.setRawUrl(destinationUri.toString());
        DownloadJsonData downloadJsonData = new DownloadJsonData();
        downloadJsonData.execute(destinationUri.toString());
    }

    // Takes coordinates of start and end points and returns URL (string) for Directions API request.
    public boolean createAndUpdateUri(double startLat, double startLng, String endAddress) {

        // These are required or never change
        final String MAPS_API_BASE_URL = "https://maps.googleapis.com/maps/api/directions/json";
        final String PARAM_ORIGIN = "origin";
        final String PARAM_DESTINATION = "destination";
        final String KEY = "key";

        // Optional parameters
        final String MODE = "mode";
        final String ALTERNATIVES = "alternatives";
        final String TRANSIT_MODE = "transit_mode";

        // Locations
        String origin = startLat + "," + startLng;
        String destination = endAddress;

        // Building the Directions API request
        destinationUri = Uri.parse(MAPS_API_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_ORIGIN, origin)
                .appendQueryParameter(PARAM_DESTINATION, destination)
                .appendQueryParameter(MODE, "transit") // For simplicity's sake, limiting myself to one mode of transit...
                .appendQueryParameter(ALTERNATIVES, "false") // ...and one route, for now.
                .appendQueryParameter(TRANSIT_MODE, "subway")
                .appendQueryParameter(KEY, NowFeedApplication.getInstance().getResources().getString(R.string.directions_api_key))
                .build();

        return destinationUri != null;
    }

    public List<Route> getRoutes() {
        return routes;
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
        final String MAPS_TRIP_DURATION = "duration";
        final String MAPS_START_ADDRESS = "start_address";
        final String MAPS_START_LOCATION = "start_location";
        final String MAPS_END_ADDRESS = "end_address";
        final String MAPS_END_LOCATION = "end_location";
        final String MAPS_OVERVIEW_POLYLINE = "overview_polyline";

        // Capture and retrieve data
        try {
            JSONObject jsonData = new JSONObject(getData());
            JSONArray routesArray = jsonData.getJSONArray(MAPS_ROUTES);

            // Retrieve first route
            JSONObject route = routesArray.getJSONObject(0);
            // Retrieve legs from route
            JSONArray legs = route.getJSONArray(MAPS_LEGS);
            // Retrieve first leg
            JSONObject leg = legs.getJSONObject(0);

            // Retrieve coordinates data
            JSONObject boundsObj = route.getJSONObject(MAPS_BOUNDS);
            JSONObject boundsNortheast = boundsObj.getJSONObject(MAPS_BOUNDS_NE);
            String boundsNortheastLat = boundsNortheast.getString("lat");
            String boundsNortheastLng = boundsNortheast.getString("lng");
            JSONObject boundsSouthwest = boundsObj.getJSONObject(MAPS_BOUNDS_SW);
            String boundsSouthwestLat = boundsSouthwest.getString("lat");
            String boundsSouthwestLng = boundsSouthwest.getString("lng");

            JSONObject distanceObj = leg.getJSONObject(MAPS_TRIP_DISTANCE);
            String distance = distanceObj.getString("text");

            JSONObject durationObj = leg.getJSONObject(MAPS_TRIP_DURATION);
            String duration = durationObj.getString("text");

            String startAddress = leg.getString(MAPS_START_ADDRESS);
            String startLocation = leg.getString(MAPS_START_LOCATION);
            String endAddress = leg.getString(MAPS_END_ADDRESS);
            String endLocation = leg.getString(MAPS_END_LOCATION);

            JSONObject overviewPolylineObj = route.getJSONObject(MAPS_OVERVIEW_POLYLINE);
            String polylinePoints = overviewPolylineObj.getString("points");

            // Create route object and load with new data
            Route routeObj = new Route(boundsNortheastLat, boundsNortheastLng, boundsSouthwestLat,
                    boundsSouthwestLng, distance, duration, startAddress, startLocation, endAddress,
                    endLocation, polylinePoints);

            this.routes.add(routeObj);

            sendNotification();

        } catch (JSONException jsone) {
            jsone.printStackTrace();
            Log.v(LOG_TAG, "Error processing JSON data");
        }
    }

    private void sendNotification() {
        Intent intent = new Intent(MAP_ROUTE_DATA_AVAILABLE);
        LocalBroadcastManager.getInstance(NowFeedApplication.getInstance()).sendBroadcast(intent);
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