package com.jaellysbales.nowfeed;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationProvider implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final String TAG = LocationProvider.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationCallback locationCallback;
    private Context myContext;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;

    // Create instance of googleApiClient and connect to Location API.
    public LocationProvider(Context context, LocationCallback callback) {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        locationCallback = callback;

        // Create LocationRequest object.
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)        // 10 secs in ms
                .setFastestInterval(1000); // 1 sec in ms

        myContext = context;
    }

    // Call to connect client.
    public void connect() {
        googleApiClient.connect();
    }

    // Verify client is connected and remove location updates before disconnecting.
    public void disconnect() {
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    // Gets coordinates of last known location on connect
    @Override
    public void onConnected(Bundle connectionHint) {
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);

        // If last location is not known, request update.
        if (lastLocation == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        } else {
            locationCallback.handleNewLocation(lastLocation);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // If Google Play services can solve error, send intent to start Google Play services activity.
        if (connectionResult.hasResolution() && myContext instanceof Activity) {
            try {
                Activity activity = (Activity) myContext;
                // Start activity that tries to resolve the error.
                connectionResult.startResolutionForResult(activity, CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // Throw exception if original PendingIntent is cancelled by Google Play services.
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            // If no resolution available -> display dialog with error.
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        locationCallback.handleNewLocation(location);
    }

    public interface LocationCallback {
        void handleNewLocation(Location location);
    }
}
