package com.example.locationtracker;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements LocationListener {

    public static int LOCATION_REQUST_CODE = 100;
    public static boolean TRACKING_LOCATION=false;
    private double latitude=0, longitude=0;
    LocationManager locationManager;
    Button trackingBtn,historyBtn;
    public static MapFragment fragment;
    LocationDB locationDB;
    List<LocationModel> locationModelList;
    static MyForegroundService myForegroundService;
    static int counter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //to hide ActionBar
        getSupportActionBar().hide();

        //setup buttons and initialize objects
        trackingBtn=findViewById(R.id.tracker_btn);
        historyBtn=findViewById(R.id.history_btn);
        myForegroundService=new MyForegroundService();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        boolean checkGPS = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // get network provider status
        boolean checkNetwork = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!checkGPS && !checkNetwork) {
            Toast.makeText(MainActivity.this, "No Service Provider is available", Toast.LENGTH_SHORT).show();
        }

        //to get the service is going on or not to set button text
        SharedPreferences sh = getSharedPreferences("LocationTracker", MODE_PRIVATE);
        TRACKING_LOCATION = sh.getBoolean("isTracking", false);

        trackingBtn.setText(TRACKING_LOCATION? "stop tracking":"start tracking");


        //button listeners
        trackingBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                TRACKING_LOCATION=!TRACKING_LOCATION;
                if(TRACKING_LOCATION){
                    Toast.makeText(MainActivity.this, "start tracking... tracked locations will inserted into table", Toast.LENGTH_SHORT).show();
                    //start tracking with foreground service
                    trackingBtn.setText("stop tracking");
                    storeTrackingStatus(TRACKING_LOCATION);
                    Intent service=new Intent(MainActivity.this,MyForegroundService.class);
                    service.setAction("START");
                    startForegroundService(service);
                    startTracking();
                }
                else{
                    Toast.makeText(MainActivity.this, "tracking stopped!", Toast.LENGTH_SHORT).show();
                    //stop tracking location and service
                    storeTrackingStatus(TRACKING_LOCATION);
                    trackingBtn.setText("start tracking");
                    stopTracking();
                }
            }
        });

        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get all locations data from table
                getLocationListInBackground();

            }
        });

        //getting database instance
        locationDB=LocationDB.getDatabase(getApplicationContext());
        //check for location permissions
        checkLocationPermission();

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 2, this);

    }

    //method to store is Tracking status in shared preferences
    private void storeTrackingStatus(boolean isTracking){
        SharedPreferences sharedPreferences = getSharedPreferences("LocationTracker", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putBoolean("isTracking",isTracking);
        myEdit.apply();
    }

    //method to start Location tracking
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startTracking() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        else {

            //get interval and accuracy settings from shared preferences
            SharedPreferences sh = getSharedPreferences("LocationTracker", MODE_PRIVATE);
            int interval = sh.getInt("interval", 0);
            int pos=sh.getInt("accuracy",0);

            Criteria criteria = new Criteria();
            switch (pos){
                case 0:
                    criteria.setAccuracy(Criteria.NO_REQUIREMENT);
                    break;
                case 1:
                    criteria.setAccuracy(Criteria.ACCURACY_FINE);
                    break;
                case 2:
                    criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                    break;
            }
            locationManager.getBestProvider(criteria,true);


            //Toast.makeText(getApplicationContext(),"interval in min: "+interval,Toast.LENGTH_SHORT).show();
            //start location manager updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, interval*1000, 2, this);

        }

    }

    //stop location tracking and service
    private void stopTracking(){
        try {
            locationManager.removeUpdates(this);
            Intent stopIntent = new Intent(MainActivity.this, MyForegroundService.class);
            stopIntent.setAction("STOP");
            startService(stopIntent);
        }
        catch (Exception e){
            Log.d("MainActivity",e.toString());
        }
    }


    //to check location permission is allowed
    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            //initialize fragment
            fragment = new MapFragment(18,78,false);

            Log.d("MainActivity","fragment created"+fragment);
            getSupportFragmentManager().beginTransaction().replace(R.id.map_container_frame, fragment).commit();

        } else {
            //request for permission
            requestForPermissions();
        }
    }

    //method to request permissions
    private void requestForPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(MainActivity.this, "please allow permissions to view location", Toast.LENGTH_SHORT).show();
                requestForPermissions();
            }
        }
    }


    // override Location Manager methods
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude=location.getLatitude();
        longitude=location.getLongitude();

        //show location on map
        fragment.showLocation(latitude,longitude);


        try {
            //update location details on notification service
            myForegroundService.updateContent(latitude,longitude);
        }
        catch (Exception e){
            Log.d("MainActivity","foreground service"+e.toString());
        }

        if(counter==0){
            locationManager.removeUpdates(this);
            counter++;
            Toast.makeText(getApplicationContext(),"current location",Toast.LENGTH_SHORT).show();
        }else {
            //insert location data into table
            insertLocationModel(latitude, longitude);
        }

    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }


    //method to insert location data into table
    public void insertLocationModel(double latitude, double longitude){

        String formattedDate;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            formattedDate = LocalDateTime.now().format(myFormatObj);
        }
        else {
            Date d1 = new Date();
            formattedDate=""+d1;
        }

        //initialize location model
        LocationModel locationModel=new LocationModel((Double) latitude,(Double) longitude, formattedDate);
        //insert location data into table from background thread
        addLocationInBackground(locationModel);

    }

    //insert location data into table from background thread
    public void addLocationInBackground(LocationModel location){
        ExecutorService executorService= Executors.newSingleThreadExecutor();

        Handler handler=new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                //inserting location into table
                locationDB.getLocationDAO().addLocation(location);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"Location added",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    //method to get all locations data from background thread
    public void getLocationListInBackground(){
        ExecutorService executorService= Executors.newSingleThreadExecutor();

        Handler handler=new Handler(Looper.getMainLooper());
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                //get all locations from table
                locationModelList=locationDB.getLocationDAO().getAllLocations();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //navigate to history activity after getting the locations list from table
                        navigateToHistory(locationModelList);
                    }
                });
            }
        });

    }

    //method to process locatoins list and navigate to history activity
    private void navigateToHistory(List<LocationModel> list){
        ArrayList<LocationEntry> locationEntries=new ArrayList<>();
        for(LocationModel lm:list){
            LocationEntry e= new LocationEntry(lm.latitude,lm.langitude,lm.timestamp);
            if(!locationEntries.contains(e)) {
                locationEntries.add(e);
            }
        }

        Intent intent=new Intent(MainActivity.this,HistoryActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("locations",locationEntries);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}