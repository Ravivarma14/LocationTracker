package com.example.locationtracker;

import android.graphics.Color;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapFragment extends Fragment {

    private double cur_longitude, cur_latitude;
    private static GoogleMap googleMapInstance;
    private boolean isPolyline;
    private ArrayList<LocationEntry> locationEntries;

    public MapFragment(double latitude, double longitude,boolean ispoly){
        cur_longitude=longitude;
        cur_latitude=latitude;
        isPolyline=ispoly;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //initialize view
        View view=inflater.inflate(R.layout.fragment_map, container, false);

        if(isPolyline){
            //initilize location list to draw polylines on map
            Bundle bundle=getArguments();
            locationEntries=(ArrayList<LocationEntry>) bundle.getSerializable("locationlist");
        }

        //initialize map fragment
        SupportMapFragment supportMapFragment=(SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {

                //initialize map instance
                googleMapInstance=googleMap;

                if(isPolyline) {
                    //draw polylines on map
                    showPolyline(locationEntries);
                }
                else {
                    //show location on map
                    showLocation(cur_latitude, cur_longitude);
                }
            }
        });

        return view;
    }

    public static void showLocation(double lat, double lon){
        //showing current location
        Log.d("MapFragment","showing location:" + lat +","+lon);
        googleMapInstance.clear();
        //add location marker on map
        LatLng current_location=new LatLng(lat,lon);
        googleMapInstance.addMarker(new MarkerOptions().position(current_location).title("current location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        googleMapInstance.animateCamera(CameraUpdateFactory.newLatLngZoom(current_location,15));

    }

    //method to draw polylines on map
    public void showPolyline(ArrayList<LocationEntry> locationsList){

        //show initial location point on map
        LocationEntry le=locationsList.get(0);
        LatLng current_location=new LatLng(le.latitude,le.langitude);

        //bitmap markers for start, end, middle points
        BitmapDescriptor bd_start=BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
        BitmapDescriptor bd_end=BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
        BitmapDescriptor bd_middle=BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);

        for(int i=0;i<locationsList.size();i++){

            LocationEntry nxt=locationsList.get(i);
            LatLng nxt_location=new LatLng(nxt.latitude,nxt.langitude);
            MarkerOptions markerOptions=new MarkerOptions().position(nxt_location).title("current location");
            //set marker depend on start or end or middle points
            if(i==0){
                markerOptions.icon(bd_start);
            }
            else if (i==locationsList.size()-1){
                markerOptions.icon(bd_end);
            }
            else {
                markerOptions.icon(bd_middle);
            }
            //add marker on map
            googleMapInstance.addMarker(markerOptions);
            if(i+1 <locationsList.size()) {
                //draw polyline on map from current location to next location point on map
                googleMapInstance.addPolyline(new PolylineOptions()
                        .add(nxt_location, new LatLng(locationsList.get(i + 1).latitude, locationsList.get(i + 1).langitude)).width(3).color(Color.BLUE));
            }

        }

        //zooming the camera to the start point of polyline
        googleMapInstance.animateCamera(CameraUpdateFactory.newLatLngZoom(current_location,19));

    }
}