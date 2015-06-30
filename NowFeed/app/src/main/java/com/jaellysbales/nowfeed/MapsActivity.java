package com.jaellysbales.nowfeed;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import android.widget.ListView;

import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by jaellysbales on 6/23/15.
 */

public class MapsActivity extends FragmentActivity
        implements LocationProvider.LocationCallback {

    SharedPreferences sharedPreferences;

    public static final String USER_PLACES_PREFS = "UserPlacesPrefs";
    public static final String DEFAULT = "Default";

    private GoogleMap googleMap;
    private LocationProvider locationProvider;
    private LatLng start = new LatLng(40.742790, -73.935558); // start point for Directions API test (C4Q)
    private LatLng end = new LatLng(40.741781, -74.004501); // end point for Directions API test (Googz)

    private TextView tv_card_map_title_minutes;
    private TextView tv_card_map_title_destination;
    private TextView tv_card_map_directions;
    private String endAddress;
    private String distance;
    private String duration;

    private String addressHome;
    private String addressWork;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        /**
         * TODO:
         * Check network/location is enabled, handle each case
         * Rewrite code to handle home/work as destinations (provide toggle)
         * Assign JSON values to textviews
         * Markers
         * Draw routes
         * savedInstanceState (+ lock to portrait)
         */

        initializeViews();
        setUpMapIfNeeded();

        locationProvider = new LocationProvider(this, this);

        loadPreferences();
        endAddress = addressHome; // TODO: Allow toggle

        GetRouteJsonData jsonData = new GetRouteJsonData(start.latitude, start.longitude,
                endAddress);
        jsonData.execute();

        // retrieve json values and set textviews, set retrieve end latlng

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

    private OnMapReadyCallback onMapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.setMyLocationEnabled(true);
            MapsActivity.this.googleMap = googleMap;

            MarkerOptions options = new MarkerOptions()
                    .position(end)
                    .title("Google");

            googleMap.addMarker(options);
        }
    };

    protected void setUpMapIfNeeded() {
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
        // TODO: CLEAN THIS UP
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng currentLocation = new LatLng(currentLatitude, currentLongitude);
        LatLng southwest = new LatLng(40.741971, -73.987691);
        LatLng northeast = new LatLng(40.815581, -73.93099699999999);
        LatLngBounds latLngBounds = new LatLngBounds(southwest, northeast);

        // Set position and zoom of camera on new location [use latlngbounds]
        if (googleMap != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 16));
        }
    }


    public View.OnClickListener mapDirectionsListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
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
    };

    public void setPreferences(String prefAddressHome, String prefAddressWork) {
        sharedPreferences = this.getSharedPreferences(USER_PLACES_PREFS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("HOME", prefAddressHome);
        editor.putString("WORK", prefAddressWork);
        editor.apply();
        Toast.makeText(MapsActivity.this, "Data set: Home - " + addressHome + " Work: " + addressWork,
                Toast.LENGTH_SHORT).show();
        addressHome = prefAddressHome;
        addressWork = prefAddressWork;
    }

    public void loadPreferences() {
        sharedPreferences = this.getSharedPreferences(USER_PLACES_PREFS,
                Context.MODE_PRIVATE);
        String prefAddressHome = sharedPreferences.getString("HOME", DEFAULT);
        String prefAddressWork = sharedPreferences.getString("WORK", DEFAULT);

        if (prefAddressHome.equals(DEFAULT) || prefAddressWork.equals(DEFAULT)) {
            Toast.makeText(MapsActivity.this, "No address data found. Please set home/work addresses.",
                    Toast.LENGTH_SHORT).show();
            getPlacesDialog().show();

            // TODO: 1.) Validate input. 2.) Allow user to access and edit their preferences.
        } else {
            Toast.makeText(MapsActivity.this, "Data loaded successfully", Toast.LENGTH_SHORT).show();
            addressHome = prefAddressHome;
            addressWork = prefAddressWork;
            Toast.makeText(MapsActivity.this, "Home: " + addressHome + "\n" + "Work: " +
                    addressWork, Toast.LENGTH_SHORT).show();
        }
    }

    public Dialog getPlacesDialog() {
        final View layout = View.inflate(this, R.layout.dialog_set_prefs, null);
        final EditText etAddressHome = (EditText) layout.findViewById(R.id.et_address_home);
        final EditText etAddressWork = (EditText) layout.findViewById(R.id.et_address_work);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_title)
                .setIcon(0);

        builder.setPositiveButton(R.string.ok, new Dialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Set
                addressHome = etAddressHome.getText().toString().trim();
                addressWork = etAddressWork.getText().toString().trim();
                setPreferences(addressHome, addressWork);
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.cancel();
            }
        });

        builder.setView(layout);
        return builder.create();
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
