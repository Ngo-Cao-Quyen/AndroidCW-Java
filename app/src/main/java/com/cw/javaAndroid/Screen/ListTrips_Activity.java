package com.cw.javaAndroid.Screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import com.cw.javaAndroid.Adapter.Adapter_Trips;
import com.cw.javaAndroid.Database.database_Trips;
import com.cw.javaAndroid.Model.Trips;
import com.cw.javaAndroid.R;
import com.cw.javaAndroid.databinding.ActivityMainBinding;
import com.cw.javaAndroid.databinding.DialogAddTripsBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class ListTrips_Activity extends AppCompatActivity {
    private ActivityMainBinding binding;// map view binding for activity main layout (activity_main.xml)
    private database_Trips database_trips;// initialize database
    private Trips trips;
    private Adapter_Trips adapter_trips;
    private List<Trips> tripsList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());// map view binding for activity main layout (activity_main.xml)
        setContentView(binding.getRoot());// set content view for activity main layout (activity_main.xml)
        binding.revTrip.setLayoutManager(new LinearLayoutManager(this));// set layout manager for recycler view
        binding.AddTrip.setOnClickListener(v->dialogAddTrips());// click event on floating action button
        binding.deleteAllTrips.setOnClickListener(v->deleteAllTrips());// event click on delete all trips
        database_trips = new database_Trips(this);
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                search(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                search(s);
                return false;
            }
        });
    }

    private void deleteAllTrips() {// function to delete all trips in the database
        //dialog delete all trips
        //use sweet alert dialog library to display confirmation dialog to delete all trips
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)//use sweetalert library to display dialog
                .setTitleText("Are you sure?")//confirmation message
                .setContentText("Won't be able to recover this file!")//Consequence Notice
                .setConfirmText("Yes, Delete")//confirm button
                .setConfirmClickListener(sweetAlertDialog -> {//event when confirm button is clicked
                    database_trips.deleteAllTrips();//delete list trips
                    tripsList.clear();
                    adapter_trips.notifyDataSetChanged();//update again adapter
                    sweetAlertDialog.dismissWithAnimation();//close dialog
                })
                .setCancelButton("No, Cancel", SweetAlertDialog::dismissWithAnimation)//cancel button and the event when the cancel button is clicked
                .show();
    }

    private void dialogAddTrips() {
        //dialog add trips
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);//initialize bottom sheet dialog to display dialog
        DialogAddTripsBinding binding = DialogAddTripsBinding.inflate(getLayoutInflater());//mapping view binding for dialog add trips layout (dialog_add_trips.xml)
        bottomSheetDialog.setContentView(binding.getRoot());//set content view for dialog add trips layout (dialog_add_trips.xml)
        //check null data
        binding.TripDateAdd.setOnClickListener(v->{// click event edit text trip date
            //use the material date picker library to display the date picker dialog (to use this library need to add materialCalendarStyle to the theme.xml)
            MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();//initialize builder to display date picker
            builder.setTitleText("Select date");
            MaterialDatePicker materialDatePicker = builder.build();
            materialDatePicker.show(getSupportFragmentManager(),"DATE_PICKER");
            materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                //convert date to string
                String myFormat = "dd/MM/yyyy";// format day, month, year
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);// format day, month, year
                binding.TripDateAdd.setText(sdf.format(selection));
            });
        });
        //add trips to database
        binding.btnAddTripAdd.setOnClickListener(v->{// event click on add trip button
            if(binding.TripNameAdd.getText().toString().isEmpty()){//check if the input is empty (if empty, display a message)
                binding.TripNameAdd.setError("Please enter trip name");//show error message
            }else if(binding.TripDestinationAdd.getText().toString().isEmpty()){
                binding.TripDestinationAdd.setError("Please enter destination");
            }else if(binding.TripDescriptionAdd.getText().toString().isEmpty()){
                binding.TripDescriptionAdd.setError("Please enter description");
            }else if(binding.TripDateAdd.getText().toString().isEmpty()){
                binding.TripDateAdd.setError("Please enter date");
            }else{
                //insert data
                trips = new Trips();//initialize trips
                trips.setName(binding.TripNameAdd.getText().toString());//get data from edit text trip name
                trips.setDestination(binding.TripDestinationAdd.getText().toString());
                trips.setDescription(binding.TripDescriptionAdd.getText().toString());
                trips.setDate(binding.TripDateAdd.getText().toString());
                // get data from radio button assessment (if radio button yes is selected, get data as yes, if radio button no is selected, get data as no)
                trips.setRequireAssessement(binding.radioGroupAssessmentAdd.getCheckedRadioButtonId() == R.id.Yes ? "Yes" : "No");
                if(database_trips.addTrip(trips)){//thêm dữ liệu vào database
                    //use toasty library to display notifications (to use this library need to add toasty to build.gradle)
                    Toasty.success(this, "Success!", Toast.LENGTH_SHORT, true).show();
                    getData();//get data from database
                }
                else {
                    Toasty.error(this, "Failed!", Toast.LENGTH_SHORT, true).show();
                }
                bottomSheetDialog.dismiss();//close dialog
            }
        });
        binding.btnCancelTripAdd.setOnClickListener(v->bottomSheetDialog.dismiss());//event click to button cancel
        bottomSheetDialog.show();//display dialog
    }

    //getdata
    public void getData(){
        tripsList.clear();
        tripsList = database_trips.getAllTrips();//get data from database
        if(tripsList.size() > 0){//if the data retrieved from the database is greater than 0 -> code run
            Log.d("test",tripsList.get(0).getName());
            adapter_trips = new Adapter_Trips(this,tripsList);//initialize adapter trips
            binding.revTrip.setAdapter(adapter_trips);//set adapter for recycler view trip
        }else{//opposite condition -> code run
            adapter_trips = new Adapter_Trips(this,tripsList);
            binding.revTrip.setAdapter(adapter_trips);
        }
    }
    private void search(String s){
        List<Trips> tripsList = database_trips.search(s);//get data from database
        if(tripsList.size() > 0){//if the data retrieved from the database is greater than 0 -> code run
            adapter_trips = new Adapter_Trips(this,tripsList);//initialize adapter trips
            binding.revTrip.setAdapter(adapter_trips);//set adapter for recycler view trip
        }
    }
    @Override
    protected void onResume() {//event when activity is started
        super.onResume();
        getData();//get data from database
    }
}