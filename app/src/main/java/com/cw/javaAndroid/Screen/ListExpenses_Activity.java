package com.cw.javaAndroid.Screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.cw.javaAndroid.Adapter.Adapter_Expenses;
import com.cw.javaAndroid.Database.database_Expenses;
import com.cw.javaAndroid.Model.Expenses;
import com.cw.javaAndroid.databinding.ActivityListExpensesBinding;
import com.cw.javaAndroid.databinding.DialogAddExpensesBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class ListExpenses_Activity extends AppCompatActivity {
    private ActivityListExpensesBinding binding;//initialize binding variable to map views in layout activity_all_expenses
    private int tripID;
    private database_Expenses database_expenses;
    private Adapter_Expenses adapter_expenses;
    private List<Expenses> expensesList;
    private String[] expenseType = {"Food", "Travel", "Other"};//array of different spend types to display to the spinner for the user to choose

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListExpensesBinding.inflate(getLayoutInflater());//assign binding variable to layout activity_all_expenses using inflate method
        setContentView(binding.getRoot());//set view for activity using getRoot() method of binding variable
        tripID = getIntent().getIntExtra("TripID", 0);//get tripID data from sent intent
        expensesList = new ArrayList<>();//initialize the expenses List array to select Expenses objects
        binding.AddExpense.setOnClickListener(v -> addExpenses());// assign click event to floating action button to open dialog
        binding.deleteAllExpenses.setOnClickListener(v -> deleteAllExpenses());//assign click event to delete all button


    }

    private void deleteAllExpenses() {//method to delete all
        database_expenses = new database_Expenses(this);//initialize database
        //use SweetAlert library to display delete all confirmation dialog
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);//initialize warning dialog(WARNING_TYPE) to confirm delete all
        sweetAlertDialog.setTitleText("Delete all expenses")
                .setContentText("Are you sure you want to delete all expenses?")
                .setConfirmText("Delete")
                .setCancelText("Cancel")
                .setConfirmClickListener(sweetAlertDialog1 -> {//if the user chooses to delete, delete all
                    database_expenses.deleteAllExpenses(tripID);//call the database's delete All Expenses() method to delete all
                    sweetAlertDialog1.dismissWithAnimation();//close dialog
                    //use Toasty library to display successful deletion of all spends
                    Toasty.success(this, "Delete all expenses successfully", Toasty.LENGTH_SHORT).show();//show delete successful message
                    finish();
                })
                .setCancelClickListener(sweetAlertDialog1 -> sweetAlertDialog1.dismissWithAnimation())//if the user chooses to cancel, close the dialog
                .show();
    }

    private void addExpenses() {//method to open dialog add new expenditure
        //initialize dialog add new expenditure
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, expenseType);//initialize adapter to display expense types on spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//set layout for spinner when opened
        //dialog
        BottomSheetDialog dialog = new BottomSheetDialog(this);//initialize dialog bottom sheet to display more dialog
        DialogAddExpensesBinding dialogBinding = DialogAddExpensesBinding.inflate(getLayoutInflater());//initialize dialogBinding variable to map views in layout dialog_add_expenses using inflate method
        dialog.setContentView(dialogBinding.getRoot());//set view for dialog using getRoot() method of dialogBinding variable
        //set spinner
        dialogBinding.spnExpenseType.setAdapter(adapter);//assign adapter to spinner to display expense type on spinner
        dialogBinding.spnExpenseType.setSelection(0);//set default position for spinner is first position
        dialogBinding.ExpenseDate.setOnClickListener(new View.OnClickListener() {// assign click event to edit date
            @Override
            public void onClick(View view) {
                // use the MaterialDatePicker library to display the date selection dialog (added to the library in the project's theme with the name material)
                MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();//initialize builder to display date selection dialog
                builder.setTitleText("Select a date");
                MaterialDatePicker materialDatePicker = builder.build();
                materialDatePicker.show(getSupportFragmentManager(),"DATE_PICKER");
                materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                    //convert date to string
                    String myFormat = "dd/MM/yyyy";// format date, month, year
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);// format date, month, year
                    dialogBinding.ExpenseDate.setText(sdf.format(selection));//set text for edit date
                });
            }
        });
        dialogBinding.btnAddExpense.setOnClickListener(new View.OnClickListener() {//assign click event for add button
            public void onClick(View view) {
                if (dialogBinding.ExpenseAmount.getText().toString().isEmpty()) {//if the edit text is empty, the message will be displayed
                    dialogBinding.ExpenseAmount.setError("Please enter expense amount");//show error message for edit text expense amount
                    return;//exit method
                }
                if (dialogBinding.ExpenseDate.getText().toString().isEmpty()) {
                    dialogBinding.ExpenseDate.setError("Please enter expense description");
                    return;
                }
                Expenses expenses = new Expenses();//initialize the expenses object
                expenses.setTripId(tripID);//assign trip id to expenses object
                expenses.setType(dialogBinding.spnExpenseType.getSelectedItem().toString());//assign the expense type to the expenses object
                expenses.setAmount(Double.parseDouble(dialogBinding.ExpenseAmount.getText().toString()));//assign the spending amount to the expenses object
                expenses.setTime(dialogBinding.ExpenseDate.getText().toString());//assign date to expenses object
                database_expenses = new database_Expenses(ListExpenses_Activity.this);//initialize database
                if(database_expenses.addExpense(expenses)){//If adding is successful, a message will be displayed
                    //display a message of successful addition and close the dialog (with Toasty library named toasty)
                    Toasty.success(getApplicationContext(), "Add expense successfully", Toasty.LENGTH_SHORT).show();
                    getData();//call getData() method to get data from database
                }
                else{//if add fails, show message
                    Toasty.error(getApplicationContext(), "Add expense failed", Toasty.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //getdata
    private void getData() {//method to get data from database and display to recyclerview
        //initialize database
        database_expenses = new database_Expenses(this);
        expensesList = database_expenses.getAllExpenses(tripID);//get data from database
        adapter_expenses = new Adapter_Expenses(this, expensesList);//initialize adapter
        binding.revExpenses.setAdapter(adapter_expenses);
        binding.revExpenses.setLayoutManager(new LinearLayoutManager(this));//set layout for recyclerview
    }
    @Override
    protected void onResume() {//method to be called when the activity is started
        super.onResume();
        getData();//call getData() method to get data from database
    }
}