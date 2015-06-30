package com.jaellysbales.nowfeed;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by jaellysbales on 6/23/15.
 */
public class MapsActivity extends FragmentActivity
        implements LocationProvider.LocationCallback {

    private GoogleMap googleMap;
    private LocationProvider locationProvider;
    private LatLng start = new LatLng(40.815009, -73.95929799999999); // start point for Directions API test
    private LatLng end = new LatLng(40.742790, -73.935558); // end point for Directions API test

    private TextView tv_card_map_title_minutes;
    private TextView tv_card_map_title_destination;
    private TextView tv_card_map_directions;

    private String tripDuration;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        /**
         * TODO:
         * Check network/location is enabled, handle each case.
         * Save home/work addresses and rewrite code to handle.
         * Draw routes.
         * savedInstanceState (+ lock to portrait)
         */

        initializeViews();
        setUpMapIfNeeded();

        locationProvider = new LocationProvider(this, this);

        /* TODO: Rewrite this to take non-hardcoded values.
         * Ex - user location coords for start latlng, their home/work for end latlng.
         */

        GetRouteJsonData jsonData = new GetRouteJsonData(start.latitude, start.longitude,
                end.latitude, end.longitude);
        jsonData.execute();

        // Launch intent for user to get directions from current location to destination
        tv_card_map_directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (start != null && end != null) {
                    String uri = "https://maps.google.com/maps?f=d&daddr=" +
                            Double.toString(end.latitude) + "," + Double.toString(end.longitude);
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(i);
                } else if (start == null) {
                    Toast.makeText(MapsActivity.this, "No origin/location set", Toast.LENGTH_SHORT).show();
                } else if (end == null) {
                    Toast.makeText(MapsActivity.this, "No destination set", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void initializeViews() {
        tv_card_map_title_minutes = (TextView) findViewById(R.id.tv_card_map_title_minutes);
        tv_card_map_title_destination = (TextView) findViewById(R.id.tv_card_map_title_destination);
        tv_card_map_directions = (TextView) findViewById(R.id.tv_card_map_directions);
    }

    private OnMapReadyCallback onMapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.setMyLocationEnabled(true);
            MapsActivity.this.googleMap = googleMap;
        }
    };

    private void setUpMapIfNeeded() {
        // Verify map has not already been instantiated.
        if (googleMap == null) {
            // Get maps system and view.
            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.card_map_frag);
            mapFragment.getMapAsync(onMapReadyCallback);
        }
    }

    @Override
    public void handleNewLocation(Location location) {
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        // Set position and zoom of camera on new location.
        if (googleMap != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)
                    .zoom(16)
                    .build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        locationProvider.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationProvider.disconnect();
    }
}
