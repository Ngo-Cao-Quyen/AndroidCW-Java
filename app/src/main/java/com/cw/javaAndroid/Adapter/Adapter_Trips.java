package com.cw.javaAndroid.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cw.javaAndroid.Model.Trips;
import com.cw.javaAndroid.R;
import com.cw.javaAndroid.Screen.TripsDetails_Activity;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Trips extends RecyclerView.Adapter<Adapter_Trips.ViewHolder> {
    //build adapter for recyclerview trips to display list of trips
    // declare variables
    private Context context;
    private List<Trips> tripsList;
    // initialize adapter and pass context and list trips
    public Adapter_Trips(Context context, List<Trips> tripsList) {
        this.context = context;
        this.tripsList = tripsList;

    }
    // initialize viewholder and pass layout item trips
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_trips, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // assign data to views in item trips
        Trips trips = tripsList.get(position);// get trips at position
        holder.bind(trips, position);// call bind function to assign data to views in item trips
    }

    @Override
    public int getItemCount() {
        return tripsList.size();
    }

    public void search(ArrayList<Trips> tripsList) {
        // search function to search trips by name
        this.tripsList = tripsList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {// initialize viewholder
        // declare views in item trips
        private TextView stt,Name, Destination, Date, RequireAssessment;
        // initialize viewholder and pass view item trips
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.name_trips_item);
            Destination = itemView.findViewById(R.id.Destination_trips_item);
            Date = itemView.findViewById(R.id.date_trips_item);
            RequireAssessment = itemView.findViewById(R.id.Assessement_trips_item);
            stt = itemView.findViewById(R.id.stt_trips_item);
        }
        // bind function to assign data to views in item trips
        public void bind(Trips trips, int position) {// pass trips and the position of trips in the list
            stt.setText(String.valueOf(position+1));
            Name.setText(trips.getName());
            Destination.setText(trips.getDestination());
            Date.setText(trips.getDate());
            RequireAssessment.setText("Require Assessment: "+trips.getRequireAssessement());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // when you click on the trip item, it will switch to the trips details screen
                    Intent intent = new Intent(context, TripsDetails_Activity.class);// initialize intent
                    intent.putExtra("Trip", trips);
                    context.startActivity(intent);
                }
            });
        }
    }
}
