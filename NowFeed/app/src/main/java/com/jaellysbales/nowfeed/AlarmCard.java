package com.jaellysbales.nowfeed;

import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Created by c4q-rosmary on 6/27/15.
 */
public class AlarmCard {

    private static View alarmView;
    private static int hour;
    private static int minutes;

    public AlarmCard(View alarmView){
        this.alarmView = alarmView;
    }

    public static View createAlarmView(LayoutInflater inflater){
        alarmView = inflater.inflate(R.layout.alarm_card, null);

        Button buttonSet = (Button) alarmView.findViewById(R.id.set_alarm_button);
        buttonSet.setOnClickListener(SetOnClickListener);

        Button buttonView = (Button) alarmView.findViewById(R.id.view_alarms_button);
        buttonView.setOnClickListener(ViewOnClickListener);


        //todo: create timePicker in dialogue box
        TimePicker timePicker = (TimePicker) alarmView.findViewById(R.id.time_picker);
        timePicker.setCurrentMinute(00);
        timePicker.setCurrentHour(12);
        timePicker.setOnTimeChangedListener(onTimeChanged);

        Button buttonSelectTime = (Button) alarmView.findViewById(R.id.select_time_button);
        buttonSelectTime.setOnClickListener(selectOnClickListener);

        // this will show next upcoming active alarm
        String nextAlarm = Settings.System.getString(alarmView.getContext().getContentResolver(),
                Settings.System.NEXT_ALARM_FORMATTED);

        TextView alarmsTextview = (TextView) alarmView.findViewById(R.id.alarms_text_view);
        alarmsTextview.setText("next alarm : " + nextAlarm);

        return alarmView;
    }

    public static View.OnClickListener selectOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextView selectTimeTextView = (TextView) alarmView.findViewById(R.id.select_time_text_view);
            TimePicker timePicker = (TimePicker) alarmView.findViewById(R.id.time_picker);

            hour = timePicker.getCurrentHour();
            minutes = timePicker.getCurrentMinute();

            selectTimeTextView.setText("Select time - " + hour + ":" + minutes);

        }
    };

    public static TimePicker.OnTimeChangedListener onTimeChanged = new TimePicker.OnTimeChangedListener() {
        @Override
        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
            TextView selectTimeTextView = (TextView) alarmView.findViewById(R.id.select_time_text_view);

            hour = hourOfDay;
            minutes = minute;

            selectTimeTextView.setText("Select time - " + hourOfDay + ":" + minute);

        }
    };

    public static void createAlarm(Context context, String message, int hour, int minutes) {
        Intent createAlarmIntent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes);
        if (createAlarmIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(createAlarmIntent);
        }
    }

    public static View.OnClickListener SetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            createAlarm(view.getContext(),"New Alarm", hour, minutes);

        }
    };

    public static  View.OnClickListener ViewOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
            view.getContext().startActivity(i);
        }
    };

}
