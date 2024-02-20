package com.example.locationtracker;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "LocationModel")
public class LocationModel {

    @ColumnInfo(name = "location_id")
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "latitude")
    Double latitude;

    @ColumnInfo(name = "langitude")
    Double langitude;

    @ColumnInfo(name = "timestamp")
    String timestamp;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    public LocationModel(){

    }

    public LocationModel(Double lat,Double lang,String time){
        latitude=lat;
        langitude=lang;
        timestamp=time;
        id=0;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLangitude() {
        return langitude;
    }

    public void setLangitude(double langitude) {
        this.langitude = langitude;
    }
}
