package com.cw.androidcw1.Screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import com.cw.androidcw1.Adapter.Adapter_Trips;
import com.cw.androidcw1.Database.database_Trips;
import com.cw.androidcw1.Model.Trips;
import com.cw.androidcw1.R;
import com.cw.androidcw1.databinding.ActivityMainBinding;
import com.cw.androidcw1.databinding.DialogAddTripsBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class ListTrips_Activity extends AppCompatActivity {
    private ActivityMainBinding binding;// ánh xạ view binding cho activity main layout (activity_main.xml)
    private database_Trips database_trips;// khai báo database
    private Trips trips;
    private Adapter_Trips adapter_trips;
    private List<Trips> tripsList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());// ánh xạ view binding cho activity main layout (activity_main.xml)
        setContentView(binding.getRoot());// set content view cho activity main layout (activity_main.xml)
        binding.revTrip.setLayoutManager(new LinearLayoutManager(this));// thiết lập layout manager cho recycler view
        binding.AddTrip.setOnClickListener(v->dialogAddTrips());// sự kiện click vào floating action button
        binding.deleteAllTrips.setOnClickListener(v->deleteAllTrips());// sự kiện click vào button delete all trips
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

    private void deleteAllTrips() {// hàm xóa tất cả các trips trong database
        //dialog delete all trips
        //sử dụng thư viện sweet alert dialog để hiển thị dialog xác nhận xóa tất cả các trips
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)//sử dụng thư viện sweetalert để hiển thị dialog
                .setTitleText("Are you sure?")//tiêu đề
                .setContentText("Won't be able to recover this file!")//nội dung
                .setConfirmText("Yes, Delete")//nút xác nhận
                .setConfirmClickListener(sweetAlertDialog -> {//sự kiện khi click vào nút xác nhận
                    database_trips.deleteAllTrips();//xóa tất cả các trips
                    tripsList.clear();//xóa list trips
                    adapter_trips.notifyDataSetChanged();//cập nhật lại adapter
                    sweetAlertDialog.dismissWithAnimation();//đóng dialog
                })
                .setCancelButton("No, Cancel", SweetAlertDialog::dismissWithAnimation)//nút hủy và sự kiện khi click vào nút hủy
                .show();
    }

    private void dialogAddTrips() {
        //dialog add trips
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);//khởi tạo bottom sheet dialog để hiển thị dialog
        DialogAddTripsBinding binding = DialogAddTripsBinding.inflate(getLayoutInflater());//ánh xạ view binding cho dialog add trips layout (dialog_add_trips.xml)
        bottomSheetDialog.setContentView(binding.getRoot());//set content view cho dialog add trips layout (dialog_add_trips.xml)
        //check null data
        binding.TripDateAdd.setOnClickListener(v->{//sự kiện click vào edit text trip date
            //sử dụng thư viện material date picker để hiển thị dialog chọn ngày tháng năm (để sử dụng thư viện này cần thêm materialCalendarStyle vào theme.xml)
            MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();//khởi tạo builder để hiển thị date picker
            builder.setTitleText("Select a date");
            MaterialDatePicker materialDatePicker = builder.build();
            materialDatePicker.show(getSupportFragmentManager(),"DATE_PICKER");
            materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                //convert date to string
                String myFormat = "dd/MM/yyyy";// định dạng ngày tháng năm
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);// định dạng ngày tháng năm
                binding.TripDateAdd.setText(sdf.format(selection));
            });
        });
        //add trips to database
        binding.btnAddTripAdd.setOnClickListener(v->{//sự kiện click vào button add trip
            if(binding.TripNameAdd.getText().toString().isEmpty()){//kiểm tra dữ liệu nhập vào có rỗng hay không (nếu rỗng thì hiển thị thông báo)
                binding.TripNameAdd.setError("Please enter trip name");//hiển thị thông báo lỗi
            }else if(binding.TripDestinationAdd.getText().toString().isEmpty()){
                binding.TripDestinationAdd.setError("Please enter trip destination");
            }else if(binding.TripDescriptionAdd.getText().toString().isEmpty()){
                binding.TripDescriptionAdd.setError("Please enter trip description");
            }else if(binding.TripDateAdd.getText().toString().isEmpty()){
                binding.TripDateAdd.setError("Please enter trip date");
            }else{
                //insert data
                trips = new Trips();//khởi tạo trips
                trips.setName(binding.TripNameAdd.getText().toString());//lấy dữ liệu từ edit text trip name
                trips.setDestination(binding.TripDestinationAdd.getText().toString());
                trips.setDescription(binding.TripDescriptionAdd.getText().toString());
                trips.setDate(binding.TripDateAdd.getText().toString());
                trips.setRequireAssessement(binding.radioGroupAssessmentAdd.getCheckedRadioButtonId() == R.id.Yes ? "Yes" : "No");//lấy dữ liệu từ radio button assessment (nếu radio button yes được chọn thì lấy dữ liệu là yes, nếu radio button no được chọn thì lấy dữ liệu là no)
                if(database_trips.addTrip(trips)){//thêm dữ liệu vào database
                    //sử dụng thư viện toasty để hiển thị thông báo (để sử dụng thư viện này cần thêm toasty vào build.gradle)
                    Toasty.success(this, "Success!", Toast.LENGTH_SHORT, true).show();
                    getData();//lấy dữ liệu từ database
                }
                else {
                    Toasty.error(this, "Failed!", Toast.LENGTH_SHORT, true).show();
                }
                bottomSheetDialog.dismiss();//đóng dialog
            }
        });
        binding.btnCancelTripAdd.setOnClickListener(v->bottomSheetDialog.dismiss());//sự kiện click vào button cancel
        bottomSheetDialog.show();//hiển thị dialog
    }

    //getdata
    public void getData(){
        tripsList.clear();
        tripsList = database_trips.getAllTrips();//lấy dữ liệu từ database
        if(tripsList.size() > 0){//nếu dữ liệu lấy được từ database lớn hơn 0
            Log.d("tesst",tripsList.get(0).getName());
            adapter_trips = new Adapter_Trips(this,tripsList);//khởi tạo adapter trips
            binding.revTrip.setAdapter(adapter_trips);//set adapter cho recycler view trip
        }else{
            adapter_trips = new Adapter_Trips(this,tripsList);
            binding.revTrip.setAdapter(adapter_trips);
        }
    }
    private void search(String s){
        List<Trips> tripsList = database_trips.search(s);//lấy dữ liệu từ database
        if(tripsList.size() > 0){//nếu dữ liệu lấy được từ database lớn hơn 0
            adapter_trips = new Adapter_Trips(this,tripsList);//khởi tạo adapter trips
            binding.revTrip.setAdapter(adapter_trips);//set adapter cho recycler view trip
        }
    }
    @Override
    protected void onResume() {//sự kiện khi activity được khởi động
        super.onResume();
        getData();//lấy dữ liệu từ database
    }
}