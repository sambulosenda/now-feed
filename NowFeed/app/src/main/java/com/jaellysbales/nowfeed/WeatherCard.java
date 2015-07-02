package com.jaellysbales.nowfeed;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
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
        final TextView degrees = (TextView) weatherView.findViewById(R.id.degrees);
        ImageView weatherIcon = (ImageView) weatherView.findViewById(R.id.weatherIcon);
        TextView dayOne = (TextView) weatherView.findViewById(R.id.dayOne);
        TextView dayTwo = (TextView) weatherView.findViewById(R.id.dayTwo);
        TextView dayThree = (TextView) weatherView.findViewById(R.id.dayThree);
        TextView dayFour = (TextView) weatherView.findViewById(R.id.dayFour);
        TextView dayFive = (TextView) weatherView.findViewById(R.id.dayFive);

//            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, this);


        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                String temp;
                temp = String.valueOf(weatherFetcher.getTemp());
                Log.d("current temp:", String.valueOf(temp));
                degrees.setText(temp + "°");

                String city = weatherFetcher.getCity();
                Log.v("post city", city);
                cityView.setText(city);

                String desc = weatherFetcher.getDescription();
                Log.d("description is: ", desc);
                weatherString.setText(desc);
            }
        };
        new Handler().postDelayed(runnable, 3000);


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

