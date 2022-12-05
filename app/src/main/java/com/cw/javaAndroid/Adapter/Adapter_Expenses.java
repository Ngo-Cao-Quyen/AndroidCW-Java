package com.cw.javaAndroid.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cw.javaAndroid.Model.Expenses;
import com.cw.javaAndroid.R;
import com.cw.javaAndroid.Screen.ExpensesDetails_Activity;

import java.util.List;

public class Adapter_Expenses extends RecyclerView.Adapter<Adapter_Expenses.ViewHolder> {
    // build adapter for recyclerview expenses to display list of expenses
    // declare variables
    private Context context;
    private List<Expenses> expensesList;
    // initialize adapter and pass in context and list of expenses
    public Adapter_Expenses(Context context, List<Expenses> expensesList) {
        this.context = context;
        this.expensesList = expensesList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // initialize viewholder and pass layout item expenses
        View view = LayoutInflater.from(context).inflate(R.layout.item_expense, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // assign data to views in item expenses
        Expenses expenses = expensesList.get(position);// get expenses at position
        holder.stt.setText(String.valueOf(position + 1));
        holder.Type.setText(expenses.getType());
        holder.Amount.setText(String.valueOf(expenses.getAmount()));
        holder.Time.setText(expenses.getTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // when clicking on the expenses item, it will switch to the expenses detail screen
                Intent intent = new Intent(context, ExpensesDetails_Activity.class);// initialize intent
                intent.putExtra("Expenses", expenses);// pass expenses to intent
                context.startActivity(intent);// switch to expenses detail screen
            }
        });
    }

    @Override
    public int getItemCount() {
        return expensesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // declare the views in item expenses
        private TextView Type, Amount, Time,stt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Type = itemView.findViewById(R.id.name_trip_expense);
            Amount = itemView.findViewById(R.id.destination_expense);
            Time = itemView.findViewById(R.id.date_expense);
            stt = itemView.findViewById(R.id.stt);
        }
    }
}