package com.jaellysbales.nowfeed;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.AlarmClock;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Created by c4q-rosmary on 6/27/15.
 */
public class AlarmCard {

    private static View alarmView;
    private static int hour;
    private static int minutes;
    private static TimePicker timePicker;

    public AlarmCard(View alarmView){
        this.alarmView = alarmView;
    }

    public static View createAlarmView(LayoutInflater inflater){
        alarmView = inflater.inflate(R.layout.alarm_card, null);

        ImageButton btnCreate = (ImageButton) alarmView.findViewById(R.id.create_alarm_btn);
        btnCreate.setOnClickListener(createAlarmListener);

        Button buttonView = (Button) alarmView.findViewById(R.id.view_alarms_button);
        buttonView.setOnClickListener(ViewOnClickListener);

        showNextAlarm();

        return alarmView;
    }

    public static View.OnClickListener createAlarmListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(alarmView.getContext());
            builder.setTitle("Add Alarm");
            builder.setMessage("Set Alarm Time");
            timePicker = new TimePicker(alarmView.getContext());
            timePicker.setCurrentMinute(00);
            timePicker.setCurrentHour(12);
            builder.setView(timePicker);

            //timePicker.setOnTimeChangedListener(onTimeChanged);
            builder.setPositiveButton("Add alarm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    hour = timePicker.getCurrentHour();
                    minutes = timePicker.getCurrentMinute();

                    createAlarm(view.getContext(), "New Alarm", hour, minutes);

                    showNextAlarm();
                }
            });

            builder.setNegativeButton("Cancel", null);

            builder.create().show();
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

    public static  View.OnClickListener ViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //Intent i = new Intent(AlarmClock.ACTION_SET_ALARM); //Works for s3

            Intent i = new Intent(AlarmClock.ACTION_SHOW_ALARMS);//works for s5
            view.getContext().startActivity(i);
        }
    };

    public static void showNextAlarm(){
        // this will show next upcoming active alarm
        String nextAlarm;
        TextView alarmsTextView = (TextView) alarmView.findViewById(R.id.alarms_text_view);

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            /** use getNextAlarmClock() **/
            AlarmManager am = (AlarmManager) alarmView.getContext().getSystemService(Context.ALARM_SERVICE);

            if(am.getNextAlarmClock() != null) {
                nextAlarm = am.getNextAlarmClock().toString();
            }else {
                nextAlarm = "no alarms active";
            }
        } else {
            /** use Settings.System.NEXT_ALARM_FORMATTED **/
            nextAlarm = Settings.System.getString(alarmView.getContext().getContentResolver(),
                    Settings.System.NEXT_ALARM_FORMATTED);
        }

        alarmsTextView.setText(nextAlarm);
    }




}
