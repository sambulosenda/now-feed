package com.jaellysbales.nowfeed;

/**
 * Created by jaellysbales on 6/29/15.
 */
public class Route {
    private String boundsNortheastLat;
    private String boundsNortheastLng;
    private String boundsSouthwestLat;
    private String boundsSouthwestLng;
    private String distance;
    private String duration;
    private String startAddress;
    private String startLocation;
    private String endAddress;
    private String endLocation;
    private String polylinePoints;
    // TODO: Parse and handle subway icons


    public Route(String boundsNortheastLat, String boundsNortheastLng, String boundsSouthwestLat,
                 String boundsSouthwestLng, String distance, String duration, String startAddress,
                 String startLocation, String endAddress, String endLocation, String polylinePoints) {
        this.boundsNortheastLat = boundsNortheastLat;
        this.boundsNortheastLng = boundsNortheastLng;
        this.boundsSouthwestLat = boundsSouthwestLat;
        this.boundsSouthwestLng = boundsSouthwestLng;
        this.distance = distance;
        this.duration = duration;
        this.startAddress = startAddress;
        this.startLocation = startLocation;
        this.endAddress = endAddress;
        this.endLocation = endLocation;
        this.polylinePoints = polylinePoints;
    }

    public String getBoundsNortheastLat() {
        return boundsNortheastLat;
    }

    public String getBoundsNortheastLng() {
        return boundsNortheastLng;
    }

    public String getBoundsSouthwestLat() {
        return boundsSouthwestLat;
    }

    public String getBoundsSouthwestLng() {
        return boundsSouthwestLng;
    }

    public String getDistance() {
        return distance;
    }

    public String getDuration() {
        return duration;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public String getPolylinePoints() {
        return polylinePoints;
    }

    @Override
    public String toString() {
        return "Route{" +
                "boundsNortheastLat='" + boundsNortheastLat + '\'' +
                ", boundsNortheastLng='" + boundsNortheastLng + '\'' +
                ", boundsSouthwestLat='" + boundsSouthwestLat + '\'' +
                ", boundsSouthwestLng='" + boundsSouthwestLng + '\'' +
                ", distance='" + distance + '\'' +
                ", duration='" + duration + '\'' +
                ", startAddress='" + startAddress + '\'' +
                ", startLocation='" + startLocation + '\'' +
                ", endAddress='" + endAddress + '\'' +
                ", endLocation='" + endLocation + '\'' +
                ", polylinePoints='" + polylinePoints + '\'' +
                '}';
    }
}
