package com.example.locationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity{//} implements LocationListener {

    public static MapFragment fragment;
    private double latitude=0, longitude=0;
    private boolean isPolyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        //to show back icon on Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        isPolyline=getIntent().getBooleanExtra("isPolyline",false);
        latitude=getIntent().getDoubleExtra("lat",0);
        longitude=getIntent().getDoubleExtra("lang",0);

        //initialize fragment
        fragment = new MapFragment(latitude,longitude,isPolyline);

        if(isPolyline) {
            //passing bundle of location list to show polylines on map
            Bundle bundleObject=getIntent().getExtras();
            fragment.setArguments(bundleObject);
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.map_frame, fragment).commit();

    }

    //method to perform back operation
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}