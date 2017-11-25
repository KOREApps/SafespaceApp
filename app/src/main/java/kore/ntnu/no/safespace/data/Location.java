package kore.ntnu.no.safespace.data;

import java.io.Serializable;

/**
 * Created by robert on 11/21/17.
 */

public class Location implements Serializable {

    private Double latitude;
    private Double longitude;
    private Integer accuracy;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location(double latitude, double longitude, int accuracy) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getAccuracy() {
        return accuracy;
    }
}
