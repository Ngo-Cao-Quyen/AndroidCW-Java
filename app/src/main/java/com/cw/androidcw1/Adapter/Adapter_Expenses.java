package com.cw.androidcw1.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cw.androidcw1.Model.Expenses;
import com.cw.androidcw1.R;
import com.cw.androidcw1.Screen.ExpensesDetails_Activity;

import java.util.List;

public class Adapter_Expenses extends RecyclerView.Adapter<Adapter_Expenses.ViewHolder> {
    //xây dựng adapter cho recyclerview expenses để hiển thị danh sách các expenses
    // build adapter for recyclerview expenses to display list of expenses
    // khai báo các biến
    // declare variables
    private Context context;
    private List<Expenses> expensesList;

    // khởi tạo adapter và truyền vào context và list expenses
    // initialize adapter and pass in context and list of expenses
    public Adapter_Expenses(Context context, List<Expenses> expensesList) {
        this.context = context;
        this.expensesList = expensesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // khởi tạo viewholder và truyền vào layout item expenses
        // initialize viewholder and pass layout item expenses
        View view = LayoutInflater.from(context).inflate(R.layout.item_expense, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // gán dữ liệu cho các view trong item expenses
        // assign data to views in item expenses
        Expenses expenses = expensesList.get(position);// lấy expenses tại vị trí position // get expenses at position
        holder.stt.setText(String.valueOf(position + 1));
        holder.Type.setText(expenses.getType());
        holder.Amount.setText(String.valueOf(expenses.getAmount()));
        holder.Time.setText(expenses.getTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // khi click vào item expenses thì sẽ chuyển sang màn hình chi tiết expenses
                // when clicking on the expenses item, it will switch to the expenses detail screen
                Intent intent = new Intent(context, ExpensesDetails_Activity.class);// khởi tạo intent  // initialize intent
                intent.putExtra("Expenses", expenses);// truyền expenses vào intent  // pass expenses to intent
                context.startActivity(intent);// chuyển sang màn hình chi tiết expenses  // switch to expenses detail screen
            }
        });
    }

    @Override
    public int getItemCount() {
        return expensesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // khai báo các view trong item expenses
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