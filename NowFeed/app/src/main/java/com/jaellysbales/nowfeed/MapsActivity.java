package com.jaellysbales.nowfeed;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

/**
 * Created by jaellysbales on 6/23/15.
 */
public class MapsActivity extends FragmentActivity
        implements LocationProvider.LocationCallback {

    SharedPreferences sharedPreferences;

    public static final String MAPS_PREFS = "UserPlacesPrefs";
    public static final String DEFAULT = "Default";

    private GoogleMap googleMap;
    private LocationProvider locationProvider;
    private LatLng startLatLng = null;
    private LatLng endLatLng = null;
    LatLngBounds latLngBounds;

    private TextView tv_card_map_title_minutes;
    private TextView tv_card_map_title_destination;
    private TextView tv_card_map_subhead;
    private TextView tv_card_map_directions;
    private String addressHome;
    private String addressWork;
    private String endAddress;

    private GetRouteJsonData getRouteJsonData;
    private List<Route> routes;

    private BroadcastReceiver mMapRouteMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            routes = getRouteJsonData.getRoutes();
            Log.d("ROUTES", "Received routes: " + routes.size());
            drawRoutePolylines();
            setEndLatLngAndMarker();
            setTextViewsToJsonData();
            setMapCameraView();
        }
    };

    private void drawRoutePolylines() {
        if (routes != null && googleMap != null) {
            Route chosenRoute = routes.get(0);
            PolylineOptions rectOptions = new PolylineOptions();
            rectOptions.addAll(chosenRoute.getPointsOnPath());

            // Get back the mutable Polyline
            Polyline polyline = googleMap.addPolyline(rectOptions);
        }
    }

    public void setEndLatLngAndMarker() {
        if (routes != null && googleMap != null) {
            endLatLng = routes.get(0).getEndPoint();

            String markerTitle = "";
            if (endAddress.equals(addressHome)) {
                markerTitle = "Home";
            } else {
                markerTitle = "Work";
            }

            MarkerOptions endLoc = new MarkerOptions()
                    .position(endLatLng)
                    .title(markerTitle);
            googleMap.addMarker(endLoc);
        }
    }

    public void setTextViewsToJsonData() {
        if (routes != null) {
            tv_card_map_title_minutes.setText(routes.get(0).getDuration());

            if (endAddress.equals(addressHome)) {
                tv_card_map_title_destination.setText(" to home");
            } else {
                tv_card_map_title_destination.setText(" to work");
            }
            tv_card_map_subhead.setText(routes.get(0).getEndAddress());
            tv_card_map_directions.setText("Get subway directions"); // TODO: Allow for more modes of transit
        }
    }

    public void setMapCameraView() {
        if (routes != null) {
            LatLng northeastLatLng = new LatLng(routes.get(0).getBoundsNortheastLat(),
                    routes.get(0).getBoundsNortheastLng());
            LatLng southwestLatLng = new LatLng(routes.get(0).getBoundsSouthwestLat(),
                    routes.get(0).getBoundsSouthwestLng());

            latLngBounds = new LatLngBounds(southwestLatLng, northeastLatLng);
        }

        // Set position and zoom of camera on new location [use latlngbounds]
        if (googleMap != null) {
            CameraUpdate cameraUpdate = CameraUpdateFactory
                    .newLatLngBounds(latLngBounds, 16);
            googleMap.moveCamera(cameraUpdate);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        initializeViews();
        setUpMapIfNeeded();

        locationProvider = new LocationProvider(this, this);

        loadPreferences();

        // register our receiver with LocalBroadcastManager
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMapRouteMessageReceiver,
                new IntentFilter(GetRouteJsonData.MAP_ROUTE_DATA_AVAILABLE));

    }

    public void initializeViews() {
        tv_card_map_title_minutes = (TextView) findViewById(R.id.tv_card_map_title_minutes);
        tv_card_map_title_destination = (TextView) findViewById(R.id.tv_card_map_title_destination);
        tv_card_map_directions = (TextView) findViewById(R.id.tv_card_map_directions);
        tv_card_map_subhead = (TextView) findViewById(R.id.tv_card_map_subhead);
    }

    private void setUpMapIfNeeded() {
        // Verify map has not already been instantiated.
        if (googleMap == null) {
            // Get maps system and view.
            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.card_map_frag);
            mapFragment.getMapAsync(onMapReadyCallback);
        }
    }

    private OnMapReadyCallback onMapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.setMyLocationEnabled(true);
            MapsActivity.this.googleMap = googleMap;
        }
    };

    @Override
    public void handleNewLocation(Location location) {
        // TODO: CLEAN THIS UP
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        LatLng newPosition = new LatLng(currentLatitude, currentLongitude);

        if (startLatLng == null) {
            startLatLng = newPosition;
            getRouteData();
        }

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(startLatLng)
                .zoom(16)
                .build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void getRouteData() {
        endAddress = addressHome; // TODO: if/else set destination morning vs evening

        if (startLatLng == null) {
            Log.d("GET ROUTE DATA", "No start location yet");
            return;
        }

        if (endAddress == null) {
            Log.d("GET ROUTE DATA", "No end location yet");
            return;
        }

        getRouteJsonData = new GetRouteJsonData(startLatLng.latitude, startLatLng.longitude,
                endAddress);
        getRouteJsonData.execute();

//        setTextViewsToJsonData();

        // retrieve json values and set textviews, set retrieve endLatLng latlng

        tv_card_map_directions.setOnClickListener(mapDirectionsListener);
    }

    // Launch intent for user to get directions from current location to destination
    public View.OnClickListener mapDirectionsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (startLatLng != null && endLatLng != null) {
                String uri = "https://maps.google.com/maps?f=d&daddr=" +
                        Double.toString(endLatLng.latitude) + "," + Double.toString(endLatLng.longitude); // change this
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(i);
            } else if (startLatLng == null) {
                Toast.makeText(MapsActivity.this, "No origin/location set", Toast.LENGTH_SHORT).show();
            } else if (endLatLng == null) {
                Toast.makeText(MapsActivity.this, "No destination set", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void setPreferences(String prefAddressHome, String prefAddressWork) {
        sharedPreferences = this.getSharedPreferences(MAPS_PREFS,
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
        sharedPreferences = this.getSharedPreferences(MAPS_PREFS,
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
            getRouteData();
//            Toast.makeText(MapsActivity.this, "Home: " + addressHome + "\n" + "Work: " +
//                    addressWork, Toast.LENGTH_SHORT).show();
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

                getRouteData();
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