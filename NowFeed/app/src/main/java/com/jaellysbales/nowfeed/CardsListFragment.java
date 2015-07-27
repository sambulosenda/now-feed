package com.jaellysbales.nowfeed;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.jaellysbales.nowfeed.db.TaskContract;
import com.jaellysbales.nowfeed.db.TaskDBHelper;

import java.util.Calendar;
import java.util.List;

/**
 * Created by charlynbuchanan on 7/25/15.
 */
public class CardsListFragment extends Fragment implements LocationProvider.LocationCallback {
    private Context context;

    public static final String MAPS_PREFS = "UserPlacesPrefs";
    public static final String DEFAULT = "Default";
    SharedPreferences sharedPreferences;
    LatLngBounds latLngBounds;
    private GoogleMap googleMap;
    private LocationProvider locationProvider;
    private LatLng startLatLng = null;
    private LatLng endLatLng = null;
    private TextView tv_card_map_title_minutes;
    private TextView tv_card_map_title_destination;
    private TextView tv_card_map_subhead;
    private TextView tv_card_map_directions;
    private String addressHome;
    private String addressWork;
    private String endAddress;
    private GetRouteJsonData getRouteJsonData;
    private List<Route> routes;
    private AlarmCard alarmCard;
    private TodoCard todoCard;

    //maps card
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
    //maps card
    private void drawRoutePolylines() {
        if (routes != null && googleMap != null) {
            Route chosenRoute = routes.get(0);
            PolylineOptions rectOptions = new PolylineOptions().addAll(chosenRoute.getPointsOnPath())
                    .color(Color.parseColor("#FF307EE1"))
                    .width(10);
//            rectOptions.addAll(chosenRoute.getPointsOnPath());
            // Get back the mutable Polyline
            Polyline polyline = googleMap.addPolyline(rectOptions);
        }
    }

    //maps card
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
    //maps card
    public void setTextViewsToJsonData() {
        if (routes != null) {
            tv_card_map_title_minutes.setText(routes.get(0).getDuration());

            if (endAddress.equals(addressHome)) {
                tv_card_map_title_destination.setText(" to home");
            } else {
                tv_card_map_title_destination.setText(" to work");
            }
            tv_card_map_subhead.setText(routes.get(0).getEndAddress());
            tv_card_map_directions.setText("Get directions"); // TODO: Allow for more modes of transit
        }
    }
    //maps card
    public void setMapCameraView() {
        if (routes != null) {
            LatLng northeastLatLng = new LatLng(routes.get(0).getBoundsNortheastLat(),
                    routes.get(0).getBoundsNortheastLng());
            LatLng southwestLatLng = new LatLng(routes.get(0).getBoundsSouthwestLat(),
                    routes.get(0).getBoundsSouthwestLng());

            latLngBounds = new LatLngBounds(southwestLatLng, northeastLatLng);
        }

        // Set position and zoom of camera on location received
        if (googleMap != null) {
            CameraUpdate cameraUpdate = CameraUpdateFactory
                    .newLatLngBounds(latLngBounds, 16);
            googleMap.moveCamera(cameraUpdate);
        }
    }
    //maps card
    private OnMapReadyCallback onMapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.setMyLocationEnabled(true);
            CardsListFragment.this.googleMap = googleMap;
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragmentView =  inflater.inflate(R.layout.cards_list_fragment, container, false);
        locationProvider = new LocationProvider(getActivity(), this);

        retrieveWeatherData(fragmentView);
        alarmCard = new AlarmCard();
        alarmCard.setupAlarmView(fragmentView);

        todoCard = new TodoCard(fragmentView);
        todoCard.createTodoView(fragmentView);

        return fragmentView;
    }

    private void retrieveWeatherData(View fragmentView) {
        //Weather Stuff
        final TextView weatherString = (TextView) fragmentView.findViewById(R.id.weatherString);
        final TextView windData = (TextView) fragmentView.findViewById(R.id.windData);
        final TextView humidityTv = (TextView) fragmentView.findViewById(R.id.humidity);
        final TextView cityView = (TextView) fragmentView.findViewById(R.id.cityView);
        final TextView degrees = (TextView) fragmentView.findViewById(R.id.degrees);
        final ImageView weatherIcon = (ImageView) fragmentView.findViewById(R.id.weatherIcon);
        final TextView low = (TextView)fragmentView.findViewById(R.id.low);

        final WeatherFetcher weatherFetcher = new WeatherFetcher();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                String temp;
                temp = String.valueOf(weatherFetcher.getTemp());
                Log.d("current temp:", String.valueOf(temp));
//                degrees.setText(temp + "°");
                Log.d("temp: ", String.valueOf(temp));
                degrees.setText("Foo");

                String city = weatherFetcher.getCity();
                Log.v("post city", city);
                cityView.setText(city);

                String desc = weatherFetcher.getDescription();
                Log.d("description is: ", desc);
                weatherString.setText(desc);

                double maxTemp = weatherFetcher.getMaxTemp();
                double minTemp = weatherFetcher.getMinTemp();
                low.setText("Hi : " + (int)maxTemp +  "° \n\nLow :" + (int)minTemp + "°");

                double wind = weatherFetcher.getWind();
                windData.setText("Wind: " + String.valueOf(wind) + "mph");

                int humidity = weatherFetcher.getHumidity();
                humidityTv.setText("Humidity: " + String.valueOf(humidity) + "%");

                int id;
                id = weatherFetcher.getId();
                if ((id >= 200) || (id <= 232)){
                    weatherIcon.setBackgroundResource(R.drawable.thunderstorm);
                }
                if ((id >= 300) || (id <= 321)){
                    weatherIcon.setBackgroundResource(R.drawable.lightrain);
                }
                if ((id >= 500) || (id <= 531)){
                    weatherIcon.setBackgroundResource(R.drawable.moderaterain);
                }
                if ((id>=600) || (id <= 622)) {
                    weatherIcon.setBackgroundResource(R.drawable.snow);
                }
                if ((id >= 701) || (id <= 781)){
                    weatherIcon.setBackgroundResource(R.drawable.mist);
                }
            }
        };
        new Handler().postDelayed(runnable, 3000);
    }




    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        locationProvider.connect();

    }

    @Override
    public void handleNewLocation(Location location) {
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
    //maps card
    private void getRouteData() {
        getTimeAndSetEndAddress();

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

        tv_card_map_directions.setOnClickListener(mapDirectionsListener);
    }
    //maps card
    public void getTimeAndSetEndAddress() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            endAddress = addressWork;
        } else if (timeOfDay >= 12 && timeOfDay <= 24) {
            endAddress = addressHome;
        }
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
                Toast.makeText(getActivity(), "No origin/location set", Toast.LENGTH_SHORT).show();
            } else if (endLatLng == null) {
                Toast.makeText(getActivity(), "No destination set", Toast.LENGTH_SHORT).show();
            }
        }
    };
    //maps card
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected void setUpMapIfNeeded() {
        // Verify map has not already been instantiated.
        if (googleMap == null) {
            // Get maps system and view.
            MapFragment mapFragment = (MapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.card_map_frag);
            mapFragment.getMapAsync(onMapReadyCallback);
        }
    }

    //maps card
    public void setPreferences(String prefAddressHome, String prefAddressWork) {
        sharedPreferences = getActivity().getSharedPreferences(MAPS_PREFS,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("HOME", prefAddressHome);
        editor.putString("WORK", prefAddressWork);
        editor.apply();

        addressHome = prefAddressHome;
        addressWork = prefAddressWork;
    }
    //maps card
    public void loadPreferences() {
        sharedPreferences = getActivity().getSharedPreferences(MAPS_PREFS,
                Context.MODE_PRIVATE);
        String prefAddressHome = sharedPreferences.getString("HOME", DEFAULT);
        String prefAddressWork = sharedPreferences.getString("WORK", DEFAULT);

        if (prefAddressHome.equals(DEFAULT) || prefAddressWork.equals(DEFAULT)) {
            Toast.makeText(getActivity(), "No address data found. Please set home/work addresses.",
                    Toast.LENGTH_SHORT).show();
            getPlacesDialog().show();

            // TODO: 1.) Validate input. 2.) Allow user to access and edit their preferences.
        } else {
            addressHome = prefAddressHome;
            addressWork = prefAddressWork;
            getRouteData();
        }
    }

    public Dialog getPlacesDialog() {
        final View layout = View.inflate(getActivity(), R.layout.dialog_set_prefs, null);
        final EditText etAddressHome = (EditText) layout.findViewById(R.id.et_address_home);
        final EditText etAddressWork = (EditText) layout.findViewById(R.id.et_address_work);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
    public static void doneOnClick(View view) {
        View v = (View) view.getParent();
        TextView taskTextView = (TextView) v.findViewById(R.id.taskTextView);
        String task = taskTextView.getText().toString();

        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                TaskContract.TABLE,
                TaskContract.Columns.TASK,
                task);

        TodoCardTwo.helper = new TaskDBHelper(TodoCardTwo.todoViewTwo.getContext());
        SQLiteDatabase sqlDB = TodoCardTwo.helper.getWritableDatabase();
        sqlDB.execSQL(sql);
        TodoCardTwo.updateUI();
    }
}