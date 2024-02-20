package com.example.locationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HistoryActivity extends AppCompatActivity {

    private LocationListAdapter locationListAdapter;
    private RecyclerView recyclerView;
    private LocationDB ldb;
    private ArrayList<LocationEntry> locationEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView=findViewById(R.id.recyclerview);

        //Back button on ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //initializing location list from bundle
        Bundle bundleObject=getIntent().getExtras();
        locationEntries= (ArrayList<LocationEntry>) bundleObject.getSerializable("locations");

        //initializing Database object
        ldb=LocationDB.getDatabase(getApplicationContext());

        //setting adapter to recyclerview
        locationListAdapter = new LocationListAdapter(locationEntries, HistoryActivity.this);
        recyclerView.setAdapter(locationListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));

        //TouchHelper to perform swipe operation on item of recyclerview
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

    }

    //method to perform Back operation
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //TouchHelper callback implementation on Swipe Left
    ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            LocationEntry le=locationListAdapter.getLoacationAt(viewHolder.getAdapterPosition());
            LocationModel lm=new LocationModel(le.latitude,le.langitude,le.timestamp);
            try {
                //delete the current list item from recycler view in background thread
                deleteInBackground(lm);
            }
            catch (Exception e){
                Log.d("HistoryActivity",e.toString());
            }

            //refresh recyclerview
            locationEntries.remove(le);
            locationListAdapter.notifyDataSetChanged();
        }
    };


    //method to perform deletion in background thread
    public void deleteInBackground(LocationModel location){
        ExecutorService executorService= Executors.newSingleThreadExecutor();

        Handler handler=new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                ldb.getLocationDAO().delete(location.timestamp);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //after performing deletion of list item
                        Toast.makeText(HistoryActivity.this,"Location Deleted",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    //method to perform delete all data from table
    public void deleteAllInBackground(){
        ExecutorService executorService= Executors.newSingleThreadExecutor();

        Handler handler=new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                //delete all data from table
                ldb.getLocationDAO().deleteAll();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //update location list adapter
                        locationEntries.removeAll(locationEntries);
                        locationListAdapter.notifyDataSetChanged();
                        Toast.makeText(HistoryActivity.this,"All Locations Deleted",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    //implemented methods for menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteAllLocations:
                if(locationEntries.size()>0) {
                    //delete all locations from table
                    deleteAllInBackground();
                }
                else{
                    //when location list is empty. There are no locations to delete
                    Toast.makeText(HistoryActivity.this, "No items to delete", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.showAllInMapPolyline:
                if(locationEntries.size()>0) {
                    //start Drawing Polylines
                    startDrawingPolyLine();
                }
                else{
                    //when location list is empty. There are no locations to display polylines
                    Toast.makeText(HistoryActivity.this, "No locations to draw polylines", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.settings:
                //starting Settings Activity
                Intent intent=new Intent(HistoryActivity.this,SettingsActivity.class);
                startActivity(intent);
                return true;

            default:
                 return super.onOptionsItemSelected(item);
        }
    }

    //method to draw polylines using locations list
    public void startDrawingPolyLine(){

        Intent intent=new Intent(HistoryActivity.this,MapActivity.class);
        intent.putExtra("isPolyline",true);
        Bundle bundle=new Bundle();
        bundle.putSerializable("locationlist",locationEntries);
        intent.putExtras(bundle);
        startActivity(intent);

    }
}