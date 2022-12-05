package com.cw.javaAndroid.Screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.cw.javaAndroid.Database.database_Trips;
import com.cw.javaAndroid.Model.Trips;
import com.cw.javaAndroid.R;
import com.cw.javaAndroid.databinding.ActivityTripsDetailsBinding;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class TripsDetails_Activity extends AppCompatActivity {
    private ActivityTripsDetailsBinding binding;// map view binding for activity trips details layout (activity_trips_details.xml)
    private database_Trips databaseTrips;// initialize database
    private Trips trips;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTripsDetailsBinding.inflate(getLayoutInflater());// map view binding for activity trips details layout (activity_trips_details.xml)
        setContentView(binding.getRoot());// set content view for activity trips details layout (activity_trips_details.xml)

        binding.btnTripEdit.setOnClickListener(v->editTrips());// click event on edit trip button
        binding.deleteTrips.setOnClickListener(v->deleteTrip());// click event delete trip button
        binding.btnShowExpensesTripEdit.setOnClickListener(v->showExpenses());// event click the button show expenses
        trips = (Trips) getIntent().getSerializableExtra("Trip");// get trips from the sent intent and force the type to Trips
        if(trips!=null){// if trips is not null, then set data for the views in activity trips details layout (activity_trips_details.xml)
            binding.TripNameEdit.setText(trips.getName());
            binding.TripDestinationEdit.setText(trips.getDestination());
            binding.TripDateEdit.setText(trips.getDate());
            binding.TripDescriptionEdit.setText(trips.getDescription());
            // set radio button according to the trip data obtained from the sent intent (Yes or No)
            binding.radioGroupAssessmentEdit.check(trips.getRequireAssessement().equals("Yes")?R.id.Yes_Edit:R.id.No_Edit);
        }
    }

    private void showExpenses() {//show expenses
        Toasty.info(this,"Show Expenses",Toasty.LENGTH_SHORT).show();//show notification
        Intent intent = new Intent(this, ListExpenses_Activity.class);
        intent.putExtra("TripID",trips.getId());//send trip id via activity all expenses
        startActivity(intent);
    }

    private void deleteTrip() {//delete trip
        //initialize database
        databaseTrips = new database_Trips(this);
        //show delete confirmation dialog
        //use sweet alert dialog library to display delete confirmation dialog
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)//
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this file!")
                .setConfirmText("Yes, Delete it")
                .setConfirmClickListener(sweetAlertDialog -> {//Yes button click event
                    databaseTrips.deleteTrip(trips.getId());//delete trips by id
                    Toasty.success(this, "Delete Success", Toasty.LENGTH_SHORT).show();//display notification
                    sweetAlertDialog.dismissWithAnimation();// close dialog
                    finish();// close activity
                })
                .setCancelButton("No,Cancel", SweetAlertDialog::dismissWithAnimation)
                .show();
    }

    private void editTrips() {//edit trips
        //check null
        if (binding.TripNameEdit.getText().toString().isEmpty()){//if trip name null -> display notification
            binding.TripNameEdit.setError("Please enter trip name");
            return;
        }

        if (binding.TripDestinationEdit.getText().toString().isEmpty()){//if destination null -> display notification
            binding.TripDestinationEdit.setError("Please enter destination");
            return;
        }

        if (binding.TripDateEdit.getText().toString().isEmpty()){//if date null -> display notification
            binding.TripDateEdit.setError("Please enter date");
            return;
        }

        if (binding.TripDescriptionEdit.getText().toString().isEmpty()){//if description null -> display notification
            binding.TripDescriptionEdit.setError("Please enter description");
            return;
        }
        Trips trip = new Trips();//initialize new trip
        trip.setId(trips.getId());
        trip.setName(binding.TripNameEdit.getText().toString());
        trip.setDestination(binding.TripDestinationEdit.getText().toString());
        trip.setDate(binding.TripDateEdit.getText().toString());
        trip.setDescription(binding.TripDescriptionEdit.getText().toString());
        // get data from radio button (Yes or No) if checked radio button Yes then set Yes if checked radio button No then set No
        trip.setRequireAssessement(binding.radioGroupAssessmentEdit.getCheckedRadioButtonId() == R.id.Yes_Edit ? "Yes" : "No");
        databaseTrips = new database_Trips(this);//initialize database
        if(databaseTrips.updateTrip(trip)){//if the update trips is successful, a message will be displayed
            //success update
            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Success")
                    .setContentText("Update trip successfully")
                    .show();
        }
    }
}