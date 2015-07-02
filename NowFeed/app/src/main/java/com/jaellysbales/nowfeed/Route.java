package com.jaellysbales.nowfeed;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by jaellysbales on 6/29/15.
 */
public class Route {
    private double boundsNortheastLat;
    private double boundsNortheastLng;
    private double boundsSouthwestLat;
    private double boundsSouthwestLng;
    private String distance;
    private String duration;
    private String startAddress;
    private String endAddress;
    private String polylinePoints;
    private List<LatLng> pointsOnPath;
    private LatLng endPoint;
    // TODO: Parse and handle subway icons


    public Route(double boundsNortheastLat, double boundsNortheastLng, double boundsSouthwestLat,
                 double boundsSouthwestLng, String distance, String duration, String startAddress,
                 String endAddress, String polylinePoints, List<LatLng> pointsOnPath, LatLng endPoint) {
        this.boundsNortheastLat = boundsNortheastLat;
        this.boundsNortheastLng = boundsNortheastLng;
        this.boundsSouthwestLat = boundsSouthwestLat;
        this.boundsSouthwestLng = boundsSouthwestLng;
        this.distance = distance;
        this.duration = duration;
        this.startAddress = startAddress;
        this.endAddress = endAddress;
        this.polylinePoints = polylinePoints;
        this.pointsOnPath = pointsOnPath;
        this.endPoint = endPoint;
    }

    public double getBoundsNortheastLat() {
        return boundsNortheastLat;
    }

    public double getBoundsNortheastLng() {
        return boundsNortheastLng;
    }

    public double getBoundsSouthwestLat() {
        return boundsSouthwestLat;
    }

    public double getBoundsSouthwestLng() {
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

    public String getEndAddress() {
        return endAddress;
    }

    public String getPolylinePoints() {
        return polylinePoints;
    }

    public List<LatLng> getPointsOnPath() {
        return pointsOnPath;
    }

    public LatLng getEndPoint() {
        return endPoint;
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
                ", endAddress='" + endAddress + '\'' +
                ", polylinePoints='" + polylinePoints + '\'' +
                ", pointsOnPath=" + pointsOnPath +
                ", endPoint=" + endPoint +
                '}';
    }
}
