package com.example.locationtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    private TextView decreaseBtn,increaseBtn,interval;
    Spinner accuracySpinner;
    private static int minutes=0;
    String[] accuracyArray = { "LOW","MEDIUM", "HIGH" };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //set back button on Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //initialize buttons and textviews
        decreaseBtn=findViewById(R.id.decrease);
        increaseBtn=findViewById(R.id.increase);
        interval=findViewById(R.id.interval_value);
        accuracySpinner=findViewById(R.id.spinner_accuracy);

        //setting interval value with sharedpreference setting
        SharedPreferences sh = getSharedPreferences("LocationTracker", MODE_PRIVATE);
        minutes = sh.getInt("interval", 0);
        interval.setText(""+minutes);

        //setup onclick listeners
        decreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minutes--;
                interval.setText(""+minutes);
                //update setting in sharedpreference
                updateData(minutes,"interval");
            }
        });

        increaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minutes++;
                interval.setText(""+minutes);
                //update setting in sharedpreference
                updateData(minutes,"interval");
            }
        });

        //setting up array adapter for spinner
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, accuracyArray);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        accuracySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateData(position,"accuracy");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        accuracySpinner.setAdapter(ad);

        //setting up spinner value from sharedpreference
        int position = sh.getInt("accuracy", 0);
        accuracySpinner.setSelection(position);
    }

    //method to update value of setting in sharedpreference
    public void updateData(int intervalMin, String key){
        SharedPreferences sharedPreferences = getSharedPreferences("LocationTracker", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putInt(key,intervalMin);
        myEdit.apply();
    }

    //method to perform back button action
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}