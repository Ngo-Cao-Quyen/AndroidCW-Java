package com.cw.androidcw1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cw.androidcw1.Model.Trips;
import com.cw.androidcw1.R;
import com.cw.androidcw1.Screen.TripsDetails_Activity;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Trips extends RecyclerView.Adapter<Adapter_Trips.ViewHolder> {
    //xây dựng adapter cho recyclerview trips để hiển thị danh sách các trips
    //build adapter for recyclerview trips to display list of trips
    // khai báo các biến
    // declare variables
    private Context context;
    private List<Trips> tripsList;

    // khởi tạo adapter và truyền vào context và list trips
    // initialize adapter and pass context and list trips
    public Adapter_Trips(Context context, List<Trips> tripsList) {
        this.context = context;
        this.tripsList = tripsList;

    }

    // khởi tạo viewholder và truyền vào layout item trips
    // initialize viewholder and pass layout item trips
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_trips, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // gán dữ liệu cho các view trong item trips
        // assign data to views in item trips
        Trips trips = tripsList.get(position);// lấy trips tại vị trí position // get trips at position
        holder.bind(trips, position);// gọi hàm bind để gán dữ liệu cho các view trong item trips // call bind function to assign data to views in item trips
    }

    @Override
    public int getItemCount() {
        return tripsList.size();
    }

    public void search(ArrayList<Trips> tripsList) {
        // hàm search để tìm kiếm trips theo tên
        // search function to search trips by name
        this.tripsList = tripsList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {// khởi tạo viewholder // initialize viewholder
        // khai báo các view trong item trips
        // declare views in item trips
        private TextView stt,Name, Destination, Date, RequireAssessment;
        // khởi tạo viewholder và truyền vào view item trips
        // initialize viewholder and pass view item trips
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.name_trips_item);
            Destination = itemView.findViewById(R.id.Destination_trips_item);
            Date = itemView.findViewById(R.id.date_trips_item);
            RequireAssessment = itemView.findViewById(R.id.Assessement_trips_item);
            stt = itemView.findViewById(R.id.stt_trips_item);
        }
        // hàm bind để gán dữ liệu cho các view trong item trips
        // bind function to assign data to views in item trips
        public void bind(Trips trips, int position) {// truyền vào trips và vị trí của trips trong list  // pass trips and the position of trips in the list
            stt.setText(String.valueOf(position+1));
            Name.setText(trips.getName());
            Destination.setText(trips.getDestination());
            Date.setText(trips.getDate());
            RequireAssessment.setText("Require Assessment: "+trips.getRequireAssessement());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // khi click vào item trips thì sẽ chuyển sang màn hình trips details
                    // when you click on the trip item, it will switch to the trips details screen
                    Intent intent = new Intent(context, TripsDetails_Activity.class);// khởi tạo intent  // initialize intent
                    intent.putExtra("Trip", trips);
                    context.startActivity(intent);
                }
            });
        }
    }
}
