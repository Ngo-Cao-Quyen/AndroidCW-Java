package com.cw.androidcw1.Screen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.cw.androidcw1.Database.database_Expenses;
import com.cw.androidcw1.Model.Expenses;
import com.cw.androidcw1.R;
import com.cw.androidcw1.databinding.ActivityExpensesDetailsBinding;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ExpensesDetails_Activity extends AppCompatActivity {

    private ActivityExpensesDetailsBinding binding;// ánh xạ view binding cho ExpensesDetails_Activity layout (activity_expenses_details.xml) // map view binding for ExpensesDetails_Activity layout (activity_expenses_details.xml)
    private Expenses expenses;
    private database_Expenses database_expenes;
    private String[] expenseType = {"Food", "Transportation", "Accommodation", "Entertainment", "Other"};//mảng chứa các loại chi tiêu khác nhau để hiển thị lên spinner để người dùng chọn //array of different spend types to display to the spinner for the user to choose
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExpensesDetailsBinding.inflate(getLayoutInflater());// ánh xạ view binding cho ExpensesDetails_Activity layout (activity_expenses_details.xml) // map view binding for ExpensesDetails_Activity layout (activity_expenses_details.xml)
        setContentView(binding.getRoot());// ánh xạ view binding cho ExpensesDetails_Activity layout (activity_expenses_details.xml) // map view binding for ExpensesDetails_Activity layout (activity_expenses_details.xml)
        //khởi tạo database
        //initialize database
        database_expenes = new database_Expenses(this);
        //set adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, expenseType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// set adapter cho spinner
        binding.spnExpenseTypeEdit.setAdapter(adapter);// ánh xạ view binding cho ExpensesDetails_Activity layout (activity_expenses_details.xml) // map view binding for ExpensesDetails_Activity layout (activity_expenses_details.xml)
        expenses = (Expenses) getIntent().getSerializableExtra("Expenses");// lấy expense từ intent gửi qua và ép kiểu về Expenses (lấy từ Expenses_Activity) // get expense from the sent intent and cast the type to Expenses (taken from Expenses_Activity)
        if(expenses!=null){// nếu expense khác null thì hiển thị thông tin chi tiêu lên các view trong layout // if expense is not null, display spending information on views in layout
            binding.ExpenseAmountEdit.setText(String.valueOf(expenses.getAmount()));
            binding.ExpenseDateEdit.setText(expenses.getTime());
            binding.spnExpenseTypeEdit.setSelection(adapter.getPosition(expenses.getType()));
        }
        binding.btnExpenseEdit.setOnClickListener(v->addExpenses());// set sự kiện cho nút edit expense // set event for edit expense button
        binding.deleteExpensesDetails.setOnClickListener(v->deleteExpenses());// set sự kiện cho nút delete expense // set event for delete expense button
    }

    private void deleteExpenses() {
        //khởi tạo dialog
        //initialize dialog
        //sử dụng thư viện SweetAlertDialog để hiển thị dialog xác nhận xóa expense
        //use SweetAlertDialog library to display expense delete confirmation dialog
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText("Delete Expense")
                .setContentText("Are you sure you want to delete this expense?")
                .setConfirmText("Delete")
                .setCancelText("Cancel")
                .setConfirmClickListener(sweetAlertDialog1 -> {
                    database_expenes.deleteExpense(expenses.getId());
                    sweetAlertDialog1.dismissWithAnimation();
                    finish();
                })
                .setCancelClickListener(sweetAlertDialog1 -> sweetAlertDialog1.dismissWithAnimation())
                .show();
    }

    private void addExpenses() {// hàm thêm expense vào database // function to add expense to the database
        //check null
        if(binding.ExpenseAmountEdit.getText().toString().isEmpty()){
            binding.ExpenseAmountEdit.setError("Please enter amount");
            return;
        }
        if(binding.ExpenseDateEdit.getText().toString().isEmpty()){
            binding.ExpenseDateEdit.setError("Please enter date");
            return;
        }
        //get data
        Expenses expense = new Expenses();// khởi tạo expense mới để thêm vào database // create a new expense to add to the database
        expense.setAmount(Double.parseDouble(binding.ExpenseAmountEdit.getText().toString()));// set amount cho expense mới // set amount for new expense
        expense.setTime(binding.ExpenseDateEdit.getText().toString());
        expense.setType(binding.spnExpenseTypeEdit.getSelectedItem().toString());
        expense.setTripId(expenses.getTripId());
        expense.setId(expenses.getId());
        //add to database
        if(database_expenes.updateExpense(expense)){// nếu thêm thành công thì hiển thị dialog thông báo và finish activity
            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Success")
                    .setContentText("Update successfully")
                    .show();
        }

    }

}