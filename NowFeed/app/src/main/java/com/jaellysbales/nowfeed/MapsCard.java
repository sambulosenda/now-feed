package com.jaellysbales.nowfeed;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by c4q-rosmary on 6/27/15.
 */
public class MapsCard implements OnMapReadyCallback,
        LocationProvider.LocationCallback {

    public static final String TAG = MapsActivity.class.getSimpleName();
    private static GoogleMap googleMap;
    private static LocationProvider locationProvider;
    private static LatLng start = new LatLng(40.815009, -73.95929799999999); // start point for Directions API test
    private static LatLng end = new LatLng (40.742790, -73.935558); // end point for Directions API test

    private static TextView tv_card_map_title_minutes;
    private static TextView tv_card_map_title_destination;
    private static TextView tv_card_map_directions;

    private static String tripDuration;

    private static View mapsView;


    public static View createMapsView (LayoutInflater inflater, Context context, Activity activity) {
        mapsView = inflater.inflate(R.layout.activity_maps, null);

        initializeViews();

        /**
         * TODO:
         * Check network/location is enabled, handle each case.
         * Parse directions and draw routes.
         * Save home/work addresses and rewrite code to handle.
         * savedInstanceState (+ lock to portrait)
         */

        //setUpMapIfNeeded(activity);

        locationProvider = new LocationProvider(mapsView.getContext(), null);
        DirectionsProvider directionsProvider = new DirectionsProvider(mapsView.getContext());

        directionsProvider.makeUrl(start.latitude, start.longitude, end.latitude, end.longitude);

        // Launch intent for user to get directions from current location to destination
        tv_card_map_directions.setOnClickListener(tv_map_directions_listener);

        return mapsView;

    }

    public static void initializeViews() {
        tv_card_map_title_minutes = (TextView) mapsView.findViewById(R.id.tv_card_map_title_minutes);
        tv_card_map_title_destination = (TextView) mapsView.findViewById(R.id.tv_card_map_title_destination);
        tv_card_map_directions = (TextView) mapsView.findViewById(R.id.tv_card_map_directions);
    }

    public static View.OnClickListener tv_map_directions_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String uri = "https://maps.google.com/maps?f=d&daddr=" +
                    Double.toString(end.latitude) + "," + Double.toString(end.longitude);
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            mapsView.getContext().startActivity(i);
        }
    };


    public static void loadMap() {
        // TODO: Any special markers, etc.
    }

    protected void setUpMapIfNeeded(Activity activity) {
        // Verify map has not already been instantiated.
        if (googleMap == null) {
            // Get maps system and view.
            MapFragment mapFragment = (MapFragment) activity.getFragmentManager().findFragmentById(R.id.card_map_frag);
            mapFragment.getMapAsync(this);
            // Verify map successfully obtained.
//            if (googleMap != null) {
//                loadMap();
//            }
        }
    }


    @Override
    public void handleNewLocation(Location location) {
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        // FIXME: This never starts the camera in correct position.
        if (googleMap != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);
    }





}
