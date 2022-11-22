package com.cw.androidcw1.Database;

import static com.cw.androidcw1.Database.Constant.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.cw.androidcw1.Model.Expenses;

import java.util.ArrayList;
import java.util.List;

public class database_Expenses extends SQLiteOpenHelper {
    //xây dựng database cho expenses để lưu các expenses
    public database_Expenses(@Nullable Context context) {
        super(context,"Expenses.db" , null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //tạo bảng expenses trong database
        sqLiteDatabase.execSQL(CREATE_TABLE_EXPENSES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //xóa bảng expenses nếu đã tồn tại và tạo lại bảng expenses
        sqLiteDatabase.execSQL(DROP_TABLE_EXPENSES);
        onCreate(sqLiteDatabase);
    }


    //add new expense to database
    //thêm một expenses vào database expenses với các thông tin của expenses
    public boolean addExpense(Expenses expense){// thêm một expenses vào database expenses với các thông tin của expenses
        SQLiteDatabase db = this.getWritableDatabase();//tạo database để ghi
        ContentValues values = new ContentValues();//tạo một đối tượng values để lưu các thông tin của expenses
        values.put(COLUMN_TRIPID, expense.getTripId());//lưu tripId của expenses
        values.put(COLUMN_TYPE, expense.getType());
        values.put(COLUMN_AMOUNT, expense.getAmount());
        values.put(COLUMN_TIME, expense.getTime());
        long result = db.insert(TABLE_EXPENSES, null, values);//thêm một expenses vào database expenses với các thông tin của expenses và lưu kết quả vào result
        if (result == -1){//nếu thêm không thành công
            return false;//trả về false
        }else {
            return true;
        }
    }
    //select all expenses from database by trip id and return them as a list
    //lấy tất cả các expenses từ database theo trip id và trả về dưới dạng list expenses
    public List<Expenses> getAllExpenses(int tripId){//lấy tất cả các expenses từ database theo trip id và trả về dưới dạng list expenses
        String[] columns = {//tạo một mảng các cột để lấy các cột trong database
                COLUMN_ID,
                COLUMN_TRIPID,
                COLUMN_TYPE,
                COLUMN_AMOUNT,
                COLUMN_TIME
        };
        String sortOrder = COLUMN_ID + " ASC";//sắp xếp theo id tăng dần để lấy các expenses theo thứ tự thời gian
        List<Expenses> expenseList = new ArrayList<Expenses>();//tạo một list expenses để lưu các expenses
        SQLiteDatabase db = this.getReadableDatabase();//tạo database để đọc
        Cursor cursor = db.query(TABLE_EXPENSES, columns, COLUMN_TRIPID + " = ? ", new String[]{String.valueOf(tripId)}, null, null, sortOrder);//lấy các expenses từ database theo trip id và lưu vào cursor để đọc các expenses
        if(cursor.moveToFirst()){//nếu cursor có dữ liệu
            do{//lặp lại
                Expenses expense = new Expenses();//tạo một expenses để lưu các thông tin của expenses
                expense.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID))));//lấy id của expenses và lưu vào expenses vừa tạo
                expense.setTripId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TRIPID))));
                expense.setType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE)));
                expense.setAmount(Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT))));
                expense.setTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME)));
                expenseList.add(expense);//thêm expenses vừa tạo vào list expenses
            }while(cursor.moveToNext());//lặp lại cho đến khi cursor không còn dữ liệu
        }
        cursor.close();//đóng cursor
        db.close();//đóng database
        return expenseList;//trả về list expenses
    }
    //update expense in database by id and return true if success and false if not success
    //cập nhật một expenses trong database theo id và trả về true nếu thành công và false nếu không thành công
    public boolean updateExpense(Expenses expense){//cập nhật một expenses trong database theo id và trả về true nếu thành công và false nếu không thành công
        SQLiteDatabase db = this.getWritableDatabase();//tạo database để ghi
        ContentValues values = new ContentValues();//tạo một đối tượng values để lưu các thông tin của expenses
        values.put(COLUMN_TRIPID, expense.getTripId());//lưu tripId của expenses
        values.put(COLUMN_TYPE, expense.getType());
        values.put(COLUMN_AMOUNT, expense.getAmount());
        values.put(COLUMN_TIME, expense.getTime());
        int rowsUpdated = db.update(TABLE_EXPENSES, values, COLUMN_ID + " = ? ", new String[]{String.valueOf(expense.getId())});//cập nhật một expenses trong database theo id và lưu kết quả vào rowsUpdated để kiểm tra xem có cập nhật thành công không
        db.close();//đóng database
        if(rowsUpdated > 0){//nếu cập nhật thành công
            return true;//trả về true
        }
        return false;//trả về false
    }
    //delete expense from database by id and return true if success and false if not success
    //xóa một expenses trong database theo id và trả về true nếu thành công và false nếu không thành công
    public boolean deleteExpense(int id){
        SQLiteDatabase db = this.getWritableDatabase();//tạo database để ghi
        int rowsDeleted = db.delete(TABLE_EXPENSES, COLUMN_ID + " = ? ", new String[]{String.valueOf(id)});//xóa một expenses trong database theo id và lưu kết quả vào rowsDeleted để kiểm tra xem có xóa thành công không
        db.close();//đóng database
        if(rowsDeleted > 0){//nếu xóa thành công
            return true;//trả về true
        }
        return false;
    }
    //delete all expenses from database by trip id and return true if success and false if not success
    //xóa tất cả các expenses trong database theo trip id và trả về true nếu thành công và false nếu không thành công
    public boolean deleteAllExpenses(int tripId){
        SQLiteDatabase db = this.getWritableDatabase();//tạo database để ghi
        int rowsDeleted = db.delete(TABLE_EXPENSES, COLUMN_TRIPID + " = ? ", new String[]{String.valueOf(tripId)});//xóa tất cả các expenses trong database theo trip id và lưu kết quả vào rowsDeleted để kiểm tra xem có xóa thành công không
        db.close();//đóng database
        if(rowsDeleted > 0){//nếu xóa thành công
            return true;//trả về true
        }
        return false;
    }
}
