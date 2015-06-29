package com.jaellysbales.nowfeed;

import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created by jaellysbales on 6/29/15.
 */
public class Route {
    private float boundsNortheast;
    private float boundsSouthwest;
    private LatLngBounds latLngBounds;
    private String startAddress;
    private String endAddress;
    private String polylinePoints;
    private String tripDuration;
    // TODO: Parse and handle subway icons

    public Route(float boundsNortheast, float boundsSouthwest, LatLngBounds latLngBounds,
                 String startAddress, String endAddress, String polylinePoints,
                 String tripDuration) {
        this.boundsNortheast = boundsNortheast;
        this.boundsSouthwest = boundsSouthwest;
        this.latLngBounds = latLngBounds;
        this.startAddress = startAddress;
        this.endAddress = endAddress;
        this.polylinePoints = polylinePoints;
        this.tripDuration = tripDuration;
    }

    public float getBoundsNortheast() {
        return boundsNortheast;
    }

    public float getBoundsSouthwest() {
        return boundsSouthwest;
    }

    public LatLngBounds getLatLngBounds() {
        return latLngBounds;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public String getPolylinePoints() {
        return polylinePoints;
    }

    public String getTripDuration() {
        return tripDuration;
    }

    @Override
    public String toString() {
        return "DirectionsParser{" +
                "boundsNortheast=" + boundsNortheast +
                ", boundsSouthwest=" + boundsSouthwest +
                ", latLngBounds=" + latLngBounds +
                ", startAddress='" + startAddress + '\'' +
                ", endAddress='" + endAddress + '\'' +
                ", polylinePoints='" + polylinePoints + '\'' +
                ", tripDuration='" + tripDuration + '\'' +
                '}';
    }
}
