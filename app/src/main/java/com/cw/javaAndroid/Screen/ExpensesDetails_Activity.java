package com.cw.javaAndroid.Screen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.cw.javaAndroid.Database.database_Expenses;
import com.cw.javaAndroid.Model.Expenses;
import com.cw.javaAndroid.databinding.ActivityExpensesDetailsBinding;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ExpensesDetails_Activity extends AppCompatActivity {

    private ActivityExpensesDetailsBinding binding;// map view binding for ExpensesDetails_Activity layout (activity_expenses_details.xml)
    private Expenses expenses;
    private database_Expenses database_expenses;
    private String[] expenseType = {"Food", "Travel", "Other"};//array of different spend types to display to the spinner for the user to choose
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExpensesDetailsBinding.inflate(getLayoutInflater());// map view binding for ExpensesDetails_Activity layout (activity_expenses_details.xml)
        setContentView(binding.getRoot());// map view binding for ExpensesDetails_Activity layout (activity_expenses_details.xml)
        //initialize database
        database_expenses = new database_Expenses(this);
        //set adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, expenseType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// set adapter for spinner
        binding.spnExpenseTypeEdit.setAdapter(adapter);// map view binding for ExpensesDetails_Activity layout (activity_expenses_details.xml)
        expenses = (Expenses) getIntent().getSerializableExtra("Expenses");// get expense from the sent intent and cast the type to Expenses (taken from Expenses_Activity)
        if(expenses!=null){// if expense is not null, display spending information on views in layout
            binding.ExpenseAmountEdit.setText(String.valueOf(expenses.getAmount()));
            binding.ExpenseDateEdit.setText(expenses.getTime());
            binding.spnExpenseTypeEdit.setSelection(adapter.getPosition(expenses.getType()));
        }
        binding.btnExpenseEdit.setOnClickListener(v->editExpenses());// set event for edit expense button
        binding.deleteExpensesDetails.setOnClickListener(v->deleteExpenses());// set event for delete expense button
    }

    private void deleteExpenses() {
        //initialize dialog
        //use SweetAlertDialog library to display expense delete confirmation dialog
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText("Delete Expense")
                .setContentText("Are you sure you want to delete this expense?")
                .setConfirmText("Delete")
                .setCancelText("Cancel")
                .setConfirmClickListener(sweetAlertDialog1 -> {
                    database_expenses.deleteExpense(expenses.getId());
                    sweetAlertDialog1.dismissWithAnimation();
                    finish();
                })
                .setCancelClickListener(sweetAlertDialog1 -> sweetAlertDialog1.dismissWithAnimation())
                .show();
    }

    private void editExpenses() {// function to add expense to the database
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
        Expenses expense = new Expenses();// create a new expense to add to the database
        expense.setAmount(Double.parseDouble(binding.ExpenseAmountEdit.getText().toString()));// set amount for new expense
        expense.setTime(binding.ExpenseDateEdit.getText().toString());
        expense.setType(binding.spnExpenseTypeEdit.getSelectedItem().toString());
        expense.setTripId(expenses.getTripId());
        expense.setId(expenses.getId());
        //add to database
        if(database_expenses.updateExpense(expense)){// if added successfully, display the notification dialog and finish the activity
            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Success")
                    .setContentText("Update successfully")
                    .show();
        }

    }

}