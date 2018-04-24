package com.example.dtwal.mapsapp;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by dtwal on 4/16/2018.
 */

public class Point {

    Double latitude, longitude;

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
