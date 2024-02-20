package com.example.locationtracker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.ViewHolder> {

    private ArrayList<LocationEntry> locationEntries;
    private Context context;

    public LocationListAdapter(ArrayList<LocationEntry> locationEntries, Context context) {
        //constructor to initialize location list and context
        this.locationEntries = locationEntries;
        this.context = context;
    }

    @NonNull
    @Override
    public LocationListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationListAdapter.ViewHolder holder, int position) {

        //setting location list item views
        LocationEntry locationEntry=locationEntries.get(holder.getAdapterPosition());
        holder.latitudeTextView.setText(locationEntry.getLatitude().toString());
        holder.langitudeTextView.setText(locationEntry.getLangitude().toString());

        String timestamp=locationEntry.getTimestamp();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String date_time[]=timestamp.split(" ");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate currentDate = LocalDate.parse(date_time[0],formatter);

            int day = currentDate.getDayOfMonth();
            Month month = currentDate.getMonth();
            int year = currentDate.getYear();

            holder.monthTextView.setText(month.toString());
            holder.yearTextView.setText(year+" ");
            holder.dateTextView.setText(day+" ");
            holder.timestampTextView.setText(date_time[1]);
        }
        else {
            holder.monthTextView.setVisibility(View.INVISIBLE);
            holder.yearTextView.setVisibility(View.INVISIBLE);
            holder.dateTextView.setVisibility(View.INVISIBLE);
            holder.timestampTextView.setText(timestamp);
        }

        //showing Map on location list item click event
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,MapActivity.class);
                intent.putExtra("isPolyline",false);
                intent.putExtra("lat",locationEntry.latitude);
                intent.putExtra("lang",locationEntry.langitude);
                context.startActivity(intent);
            }
        });
    }

    public LocationEntry getLoacationAt(int position){
        return locationEntries.get(position);
    }

    @Override
    public int getItemCount() {
        return locationEntries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView monthTextView,dateTextView,yearTextView,latitudeTextView,langitudeTextView,timestampTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            monthTextView=itemView.findViewById(R.id.month);
            dateTextView=itemView.findViewById(R.id.date);
            yearTextView=itemView.findViewById(R.id.year);
            latitudeTextView=itemView.findViewById(R.id.latitude_tv);
            langitudeTextView=itemView.findViewById(R.id.langitude_tv);
            timestampTextView=itemView.findViewById(R.id.time_tv);

        }
    }
}
