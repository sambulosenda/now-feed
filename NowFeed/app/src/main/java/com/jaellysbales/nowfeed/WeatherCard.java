package com.jaellysbales.nowfeed;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by charlynbuchanan on 7/2/15.
 */
public class WeatherCard  implements LocationListener{
private static View weatherView;

    public static View createWeatherView(LayoutInflater inflater) {

        weatherView = inflater.inflate(R.layout.weather_card, null);

        final WeatherFetcher weatherFetcher = new WeatherFetcher();

        //Elements of CardView
        CardView weatherCard = (CardView) weatherView.findViewById(R.id.weatherCard);
        final TextView weatherString = (TextView) weatherView.findViewById(R.id.weatherString);
        TextView windData = (TextView) weatherView.findViewById(R.id.windData);
        TextView humidityTv = (TextView) weatherView.findViewById(R.id.humidity);
        final TextView cityView = (TextView) weatherView.findViewById(R.id.cityView);
        TextView degrees = (TextView) weatherView.findViewById(R.id.degrees);
        ImageView weatherIcon = (ImageView) weatherView.findViewById(R.id.weatherIcon);
        final TextView hi = (TextView)weatherView.findViewById(R.id.hi);
        final TextView low = (TextView)weatherView.findViewById(R.id.low);


//            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, this);



        return weatherView;
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Log.d("latitude", String.valueOf(latitude));
        Log.d("longitude", String.valueOf(longitude));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

