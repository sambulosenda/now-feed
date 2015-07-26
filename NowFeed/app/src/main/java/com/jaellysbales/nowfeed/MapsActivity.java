package com.jaellysbales.nowfeed;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.jaellysbales.nowfeed.db.TaskContract;
import com.jaellysbales.nowfeed.db.TaskDBHelper;

import java.util.List;

/**
 * Created by jaellysbales on 6/23/15.
 */

public class MapsActivity extends FragmentActivity {


    private List<Route> routes;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


//        ListView cardListView = (ListView) findViewById(R.id.card_list_view);
//        cardListView.setAdapter(new CardAdapter(MapsActivity.this));


        //populating other views into linearlayout
//        LinearLayout cardsLayout = (LinearLayout) findViewById(R.id.cards_stored_linear_layout);
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        cardsLayout.addView(TodoCardTwo.createTodoTwoView(inflater));
//        cardsLayout.addView(AlarmCard.createAlarmView(inflater));
//        //cardsLayout.addView(TodoCard.createTodoView(inflater)); maybe call this a notepad feature,
//        cardsLayout.addView(WeatherCard.createWeatherView(inflater));

        //Weather Stuff
//        final WeatherFetcher weatherFetcher = new WeatherFetcher();
//        final TextView weatherString = (TextView) findViewById(R.id.weatherString);
//        final TextView windData = (TextView) findViewById(R.id.windData);
//        final TextView humidityTv = (TextView) findViewById(R.id.humidity);
//        final TextView cityView = (TextView) findViewById(R.id.cityView);
//        final TextView degrees = (TextView) findViewById(R.id.degrees);
//        final ImageView weatherIcon = (ImageView) findViewById(R.id.weatherIcon);
//        final TextView low = (TextView) findViewById(R.id.low);

//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, this);


//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//
//                String temp;
//                temp = String.valueOf(weatherFetcher.getTemp());
//                Log.d("current temp:", String.valueOf(temp));
////                degrees.setText(temp + "°");
//                Log.d("temp: ", String.valueOf(temp));
//                degrees.setText("Foo");
//
//                String city = weatherFetcher.getCity();
//                Log.v("post city", city);
//                cityView.setText(city);
//
//                String desc = weatherFetcher.getDescription();
//                Log.d("description is: ", desc);
//                weatherString.setText(desc);
//
//                double maxTemp = weatherFetcher.getMaxTemp();
//                double minTemp = weatherFetcher.getMinTemp();
//                low.setText("Hi : " + (int) maxTemp + "° \n\nLow :" + (int) minTemp + "°");
//
//                double wind = weatherFetcher.getWind();
//                windData.setText("Wind: " + String.valueOf(wind) + "mph");
//
//                int humidity = weatherFetcher.getHumidity();
//                humidityTv.setText("Humidity: " + String.valueOf(humidity) + "%");
//
//                int id;
//                id = weatherFetcher.getId();
//                if ((id >= 200) || (id <= 232)) {
//                    weatherIcon.setBackgroundResource(R.drawable.thunderstorm);
//                }
//                if ((id >= 300) || (id <= 321)) {
//                    weatherIcon.setBackgroundResource(R.drawable.lightrain);
//                }
//                if ((id >= 500) || (id <= 531)) {
//                    weatherIcon.setBackgroundResource(R.drawable.moderaterain);
//                }
//                if ((id >= 600) || (id <= 622)) {
//                    weatherIcon.setBackgroundResource(R.drawable.snow);
//                }
//                if ((id >= 701) || (id <= 781)) {
//                    weatherIcon.setBackgroundResource(R.drawable.mist);
//                }
//            }
//        };
////        new Handler().postDelayed(runnable, 3000);

    }


    @Override
    protected void onResume() {
        super.onResume();
//        AlarmCard.showNextAlarm();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    //used for todocard
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