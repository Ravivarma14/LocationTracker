package com.example.locationtracker;

import java.io.Serializable;

public class LocationEntry implements Serializable {

    Double latitude;
    Double langitude;
    String timestamp;

    public LocationEntry(Double latitude, Double langitude, String timestamp) {
        this.latitude = latitude;
        this.langitude = langitude;
        this.timestamp = timestamp;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLangitude() {
        return langitude;
    }

    public void setLangitude(Double langitude) {
        this.langitude = langitude;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
