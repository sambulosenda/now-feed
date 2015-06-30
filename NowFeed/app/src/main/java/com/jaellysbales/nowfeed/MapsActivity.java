package com.jaellysbales.nowfeed;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by jaellysbales on 6/23/15.
 */
public class MapsActivity extends Activity
        implements OnMapReadyCallback,
        LocationProvider.LocationCallback {

    public static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap googleMap;
    private LocationProvider locationProvider;
    private LatLng start = new LatLng(40.815009, -73.95929799999999); // start point for Directions API test
    private LatLng end = new LatLng (40.742790, -73.935558); // end point for Directions API test

    private TextView tv_card_map_title_minutes;
    private TextView tv_card_map_title_destination;
    private TextView tv_card_map_directions;

    private String tripDuration;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        initializeViews();

        /**
         * TODO:
         * Check network/location is enabled, handle each case.
         * Parse directions and draw routes.
         * Save home/work addresses and rewrite code to handle.
         * savedInstanceState (+ lock to portrait)
         */

        setUpMapIfNeeded();

        locationProvider = new LocationProvider(this, this);
        DirectionsProvider directionsProvider = new DirectionsProvider(this);

        directionsProvider.makeUrl(start.latitude, start.longitude, end.latitude, end.longitude);

        // Launch intent for user to get directions from current location to destination
        tv_card_map_directions.setOnClickListener(mapDirectionsListener);

        ListView cardListView = (ListView) findViewById(R.id.card_list_view);
        cardListView.setAdapter(new CardAdapter(MapsActivity.this));
        

    }

    public void initializeViews() {
        tv_card_map_title_minutes = (TextView) findViewById(R.id.tv_card_map_title_minutes);
        tv_card_map_title_destination = (TextView) findViewById(R.id.tv_card_map_title_destination);
        tv_card_map_directions = (TextView) findViewById(R.id.tv_card_map_directions);

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);
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

    protected void setUpMapIfNeeded() {
        // Verify map has not already been instantiated.
        if (googleMap == null) {
            // Get maps system and view.
            MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.card_map_frag);
            mapFragment.getMapAsync(this);
            // Verify map successfully obtained.
            if (googleMap != null) {
                loadMap();
            }
        }
    }

    private void loadMap() {
        // TODO: Any special markers, etc.
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

    public View.OnClickListener mapDirectionsListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String uri = "https://maps.google.com/maps?f=d&daddr=" +
                    Double.toString(end.latitude) + "," + Double.toString(end.longitude);
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(i);
        }
    };


}
