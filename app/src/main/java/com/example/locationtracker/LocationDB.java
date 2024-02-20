package com.example.locationtracker;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {LocationModel.class}, version = 3)
public abstract class LocationDB extends RoomDatabase {

    private static volatile LocationDB locationDB;
    public abstract LocationDAO getLocationDAO();

    //static method to return database instance
    static LocationDB getDatabase(final Context context) {
        if (locationDB == null) {
            synchronized (LocationDB.class) {
                if (locationDB == null) {
                    locationDB = Room.databaseBuilder(context.getApplicationContext(),
                            LocationDB.class, "student_database")
                            .build();
                }
            }
        }
        return locationDB;
    }
}
