package com.jaellysbales.nowfeed;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback,
        LocationProvider.LocationCallback {


    public static final String TAG = MapsActivity.class.getSimpleName();
    private static LocationProvider locationProvider;
    private static LatLng start = new LatLng(40.815009, -73.95929799999999); // start point for Directions API test
    private static LatLng end = new LatLng (40.742790, -73.935558); // end point for Directions API test

    private static TextView tv_card_map_title_minutes;
    private static TextView tv_card_map_title_destination;
    private static TextView tv_card_map_directions;

    private static String tripDuration;

    private GoogleMap googleMap;



    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MapsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapsFragment newInstance() {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MapsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //removed param stuff
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = null;
        view = inflater.inflate(R.layout.activity_maps, container, false);

        tv_card_map_title_minutes = (TextView) view.findViewById(R.id.tv_card_map_title_minutes);
        tv_card_map_title_destination = (TextView) view.findViewById(R.id.tv_card_map_title_destination);
        tv_card_map_directions = (TextView) view.findViewById(R.id.tv_card_map_directions);

        locationProvider = new LocationProvider(getActivity(), this);
        //DirectionsProvider directionsProvider = new DirectionsProvider(getActivity());

        //directionsProvider.makeUrl(start.latitude, start.longitude, end.latitude, end.longitude);

        // Launch intent for user to get directions from current location to destination
        tv_card_map_directions.setOnClickListener(tv_map_directions_listener);

        setUpMapIfNeeded();

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



    @Override
    public void handleNewLocation(Location location) {
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        // FIXME: This never starts the camera in correct position.
        if (googleMap != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);
    }

    public void setUpMapIfNeeded() {
        // Verify map has not already been instantiated.
        if (googleMap == null) {

            // Get maps system and view.
            MapFragment mapsFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.card_map_frag);
            mapsFragment.getMapAsync(this);
            // Verify map successfully obtained.
//            if (googleMap != null) {
//                MapsCard.loadMap();
//            }
        }
    }

    public static View.OnClickListener tv_map_directions_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String uri = "https://maps.google.com/maps?f=d&daddr=" +
                    Double.toString(end.latitude) + "," + Double.toString(end.longitude);
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            view.getContext().startActivity(i);
        }
    };

}
