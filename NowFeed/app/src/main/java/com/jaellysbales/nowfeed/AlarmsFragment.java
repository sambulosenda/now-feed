package com.jaellysbales.nowfeed;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.AlarmClock;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlarmsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlarmsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlarmsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private static int hour;
    private static int minutes;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AlarmsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlarmsFragment newInstance() {
        AlarmsFragment fragment = new AlarmsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public AlarmsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = null;
        view = inflater.inflate(R.layout.alarm_card, container, false);


        Button buttonSet = (Button) view.findViewById(R.id.set_alarm_button);
        buttonSet.setOnClickListener(SetOnClickListener);

        Button buttonView = (Button) view.findViewById(R.id.view_alarms_button);
        buttonView.setOnClickListener(ViewOnClickListener);


        //todo: create timePicker in dialogue box
        TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);
        timePicker.setCurrentMinute(00);
        timePicker.setCurrentHour(12);
        timePicker.setOnTimeChangedListener(onTimeChanged);

        Button buttonSelectTime = (Button) view.findViewById(R.id.select_time_button);
        buttonSelectTime.setOnClickListener(selectOnClickListener);

        // this will show next upcoming active alarm
        String nextAlarm = Settings.System.getString(view.getContext().getContentResolver(),
                Settings.System.NEXT_ALARM_FORMATTED);

        TextView alarmsTextview = (TextView) view.findViewById(R.id.alarms_text_view);
        alarmsTextview.setText("next alarm : " + nextAlarm);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    public static View.OnClickListener selectOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextView selectTimeTextView = (TextView) view.findViewById(R.id.select_time_text_view);
            TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);

            hour = timePicker.getCurrentHour();
            minutes = timePicker.getCurrentMinute();

            selectTimeTextView.setText("Select time - " + hour + ":" + minutes);

        }
    };

    public static TimePicker.OnTimeChangedListener onTimeChanged = new TimePicker.OnTimeChangedListener() {
        @Override
        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
            TextView selectTimeTextView = (TextView) view.findViewById(R.id.select_time_text_view);

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
