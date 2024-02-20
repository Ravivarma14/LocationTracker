package com.example.locationtracker;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LocationDAO {

    //insert location data to table
    @Insert
    public void addLocation(LocationModel location);

    //delete whole data from location table
    @Query("DELETE FROM LocationModel")
    public void deleteAll();

    //delete location entry or row from table
    @Query("DELETE FROM LocationModel WHERE timestamp = :stamp")
    public void delete(String stamp);

    //get all the data from location table
    @Query("SELECT * FROM LocationModel")
    public List<LocationModel> getAllLocations();
}
