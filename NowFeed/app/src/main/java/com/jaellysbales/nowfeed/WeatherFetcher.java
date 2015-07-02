package com.jaellysbales.nowfeed;

/**
 * Created by charlynbuchanan on 7/2/15.
 */

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class WeatherFetcher {
    public WeatherFetcher() {
        new AsyncClass().execute();
    }


    class AsyncClass extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            Log.v("JSON", "doInBackground");
            String result = "";

            String jsonUrl = "http://api.openweathermap.org/data/2.5/weather?zip=11429,us";
            URL url = null;
            try {
                url = new URL(jsonUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.connect();
                if ((connection != null) || (url != null)) {
                    Log.v("status: ", "CONNECTED");
                }
                InputStream in = new BufferedInputStream(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                result = stringBuilder.toString();
                Log.d("json: ", result);
                connection.disconnect();


            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e("JSON", "Malformed url: " + e.toString());

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("JSON", "IOException url: " + e.toString());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.v("JSON", s);
            jsonString = s;
            getDescription();
            getTemp();
            getHumidity();
            getCity();
            getMaxTemp();
            getMinTemp();
        }


    }

    private static String jsonString = "";

    public static String getJsonString() {
        return jsonString;
    }

    public static int getHumidity() {
        int humidity = -1;
        String json = getJsonString();
        try {
            JSONObject object = new JSONObject(json);
            JSONObject main = object.getJSONObject("main");
            humidity = main.getInt("humidity");
            Log.v("humidity", String.valueOf(humidity));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return humidity;

    }

    //Get Description
    public static String getDescription() {
        String description = "";

        String json = getJsonString();
        Log.v("getJsonString", json);
        try {
            JSONObject object = new JSONObject(json);
            JSONArray weather = object.getJSONArray("weather");
            JSONObject first = (JSONObject) weather.get(0);
            description = first.getString("description");
            Log.v("description", description);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return description;
    }

    //Get Current Temperature
    public static int getTemp() {
        int temp = 0;
        String json = getJsonString();
//      F = 9/5(K - 273) + 32
        try {
            JSONObject object = new JSONObject(json);
            JSONObject main = object.getJSONObject("main");
            Double tempK = main.getDouble("temp");
            Double temperature = ((1.8 * (tempK - 273)) + 32);
            double tempUnrounded = Double.valueOf((int) Math.round(temperature));
            temp = (int) tempUnrounded;
            Log.v("temp: ", String.valueOf(temp));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return temp;
    }

    //Get City
    public static String getCity() {
        String city = "";
        String json = getJsonString();

        try {
            JSONObject object = new JSONObject(json);
            city = object.getString("name");
            Log.v("city: ", city);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return city;
    }

    //Get MaxTemp
    public static Double getMaxTemp() {
        Double maxTemp = 0.0;
        String json = getJsonString();
        try {
            JSONObject object = new JSONObject(json);
            JSONObject main = object.getJSONObject("main");
            Double maxTempK = main.getDouble("temp_max");
            Double maxTempUnrounded = ((9 / 5) * (maxTempK - 273) + 32);
            maxTemp = Double.valueOf((int) Math.round(maxTempUnrounded));

            Log.v("maxTemp= ", String.valueOf(maxTemp));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return maxTemp;
    }

    //Get minTemp
    public static Double getMinTemp() {
        String json = getJsonString();
        Double minTemp = 0.0;

        try {
            JSONObject object = new JSONObject(json);
            JSONObject main = object.getJSONObject("main");
            Double minTempK = main.getDouble("temp_min");
            double minTempUnrounded = ((9 / 5) * (minTempK - 273) + 32);
            minTemp = Double.valueOf((int) Math.round(minTempUnrounded));
            Log.v("minTemp= ", String.valueOf(minTemp));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return minTemp;
    }
}










