package com.cw.androidcw1.Screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.cw.androidcw1.Adapter.Adapter_Expenses;
import com.cw.androidcw1.Database.database_Expenses;
import com.cw.androidcw1.Model.Expenses;
import com.cw.androidcw1.databinding.ActivityListExpensesBinding;
import com.cw.androidcw1.databinding.DialogAddExpensesBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class ListExpenses_Activity extends AppCompatActivity {
    private ActivityListExpensesBinding binding;//khởi tạo biến binding để ánh xạ các view trong layout activity_all_expenses
    private int tripID;
    private database_Expenses database_expenes;
    private Adapter_Expenses adapter_expenses;
    private List<Expenses> expensesList;
    private String[] expenseType = {"Food", "Travel", "Other"};//mảng chứa các loại chi tiêu khác nhau để hiển thị lên spinner để người dùng chọn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListExpensesBinding.inflate(getLayoutInflater());//gán biến binding với layout activity_all_expenses bằng phương thức inflate
        setContentView(binding.getRoot());//set view cho activity bằng phương thức getRoot() của biến binding
        tripID = getIntent().getIntExtra("TripID", 0);//lấy dữ liệu tripID từ intent gửi qua
        expensesList = new ArrayList<>();//khởi tạo mảng expensesList để chứa các đối tượng Expenses
        binding.AddExpense.setOnClickListener(v -> addExpenses());//gán sự kiện click cho floating action button để mở dialog thêm chi tiêu
        binding.deleteAllExpenses.setOnClickListener(v -> deleteAllExpenses());//gán sự kiện click cho button xóa tất cả các chi tiêu


    }

    private void deleteAllExpenses() {//phương thức xóa tất cả các chi tiêu
        database_expenes = new database_Expenses(this);//khởi tạo database
        //sử dụng thư viện SweetAlert để hiển thị dialog xác nhận xóa tất cả các chi tiêu
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);//khởi tạo dialog cảnh báo với loại là cảnh báo (WARNING_TYPE) để xác nhận xóa tất cả các chi tiêu
        sweetAlertDialog.setTitleText("Delete all expenses")
                .setContentText("Are you sure you want to delete all expenses?")
                .setConfirmText("Delete")
                .setCancelText("Cancel")
                .setConfirmClickListener(sweetAlertDialog1 -> {//nếu người dùng chọn xóa thì thực hiện xóa tất cả các chi tiêu
                    database_expenes.deleteAllExpenses(tripID);//gọi phương thức deleteAllExpenses() của database để xóa tất cả các chi tiêu
                    sweetAlertDialog1.dismissWithAnimation();//đóng dialog
                    //sử dụng thư viện Toasty để hiển thị thông báo xóa thành công tất cả các chi tiêu
                    Toasty.success(this, "Delete all expenses successfully", Toasty.LENGTH_SHORT).show();//hiển thị thông báo xóa thành công
                    finish();
                })
                .setCancelClickListener(sweetAlertDialog1 -> sweetAlertDialog1.dismissWithAnimation())//nếu người dùng chọn hủy thì đóng dialog
                .show();
    }

    private void addExpenses() {//phương thức mở dialog thêm chi tiêu mới
        //khởi tạo dialog thêm chi tiêu mới
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, expenseType);//khởi tạo adapter để hiển thị các loại chi tiêu lên spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//set layout cho spinner khi mở ra
        //dialog
        BottomSheetDialog dialog = new BottomSheetDialog(this);//khởi tạo dialog bottom sheet để hiển thị dialog thêm chi tiêu
        DialogAddExpensesBinding dialogBinding = DialogAddExpensesBinding.inflate(getLayoutInflater());//khởi tạo biến dialogBinding để ánh xạ các view trong layout dialog_add_expenses bằng phương thức inflate
        dialog.setContentView(dialogBinding.getRoot());//set view cho dialog bằng phương thức getRoot() của biến dialogBinding
        //set spinner
        dialogBinding.spnExpenseType.setAdapter(adapter);//gán adapter cho spinner để hiển thị các loại chi tiêu lên spinner
        dialogBinding.spnExpenseType.setSelection(0);//set vị trí mặc định cho spinner là vị trí đầu tiên
        dialogBinding.ExpenseDate.setOnClickListener(new View.OnClickListener() {//gán sự kiện click cho edit text ngày chi tiêu
            @Override
            public void onClick(View view) {
                //sử dụng thư viện MaterialDatePicker để hiển thị dialog chọn ngày chi tiêu(thêm vào thư viện trong theme của project với tên là material)
                MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();//khởi tạo builder để hiển thị dialog chọn ngày chi tiêu
                builder.setTitleText("Select a date");
                MaterialDatePicker materialDatePicker = builder.build();
                materialDatePicker.show(getSupportFragmentManager(),"DATE_PICKER");
                materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                    //convert date to string
                    String myFormat = "dd/MM/yyyy";// định dạng ngày tháng năm
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);// định dạng ngày tháng năm
                    dialogBinding.ExpenseDate.setText(sdf.format(selection));//set text cho edit text ngày chi tiêu
                });
            }
        });
        dialogBinding.btnAddExpense.setOnClickListener(new View.OnClickListener() {//gán sự kiện click cho button thêm chi tiêu
            @Override
            public void onClick(View view) {
                if (dialogBinding.ExpenseAmount.getText().toString().isEmpty()) {//nếu edit text số tiền chi tiêu rỗng thì hiển thị thông báo
                    dialogBinding.ExpenseAmount.setError("Please enter expense amount");//hiển thị thông báo lỗi cho edit text số tiền chi tiêu
                    return;//thoát khỏi phương thức
                }
                if (dialogBinding.ExpenseDate.getText().toString().isEmpty()) {
                    dialogBinding.ExpenseDate.setError("Please enter expense description");
                    return;
                }
                Expenses expenses = new Expenses();//khởi tạo đối tượng expenses
                expenses.setTripId(tripID);//gán id chuyến đi cho đối tượng expenses
                expenses.setType(dialogBinding.spnExpenseType.getSelectedItem().toString());//gán loại chi tiêu cho đối tượng expenses
                expenses.setAmount(Double.parseDouble(dialogBinding.ExpenseAmount.getText().toString()));//gán số tiền chi tiêu cho đối tượng expenses
                expenses.setTime(dialogBinding.ExpenseDate.getText().toString());//gán ngày chi tiêu cho đối tượng expenses
                database_expenes = new database_Expenses(ListExpenses_Activity.this);//khởi tạo database
                if(database_expenes.addExpense(expenses)){//nếu thêm chi tiêu thành công thì hiển thị thông báo
                    //hiển thị thông báo thêm chi tiêu thành công và đóng dialog(bằng thư viện Toasty với tên là toasty)
                    Toasty.success(getApplicationContext(), "Add expense successfully", Toasty.LENGTH_SHORT).show();
                    getData();//gọi phương thức getData() để lấy dữ liệu chi tiêu từ database
                }
                else{//nếu thêm chi tiêu thất bại thì hiển thị thông báo
                    Toasty.error(getApplicationContext(), "Add expense failed", Toasty.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //getdata
    private void getData() {//phương thức lấy dữ liệu chi tiêu từ database và hiển thị lên recyclerview
        //khởi tạo database
        database_expenes = new database_Expenses(this);
        expensesList = database_expenes.getAllExpenses(tripID);//lấy dữ liệu chi tiêu từ database
        adapter_expenses = new Adapter_Expenses(this, expensesList);//khởi tạo adapter
        binding.revExpenses.setAdapter(adapter_expenses);
        binding.revExpenses.setLayoutManager(new LinearLayoutManager(this));//set layout cho recyclerview
    }
    @Override
    protected void onResume() {//phương thức được gọi khi activity được khởi động
        super.onResume();
        getData();//gọi phương thức getData() để lấy dữ liệu chi tiêu từ database
    }
}