package com.cw.javaAndroid.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.cw.javaAndroid.Model.Expenses;

import java.util.ArrayList;
import java.util.List;

public class database_Expenses extends SQLiteOpenHelper {
    //create a database for expenses to store expenses
    public database_Expenses(@Nullable Context context) {
        super(context,"Expenses.db" , null, Constant.DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //create expenses table in database
        sqLiteDatabase.execSQL(Constant.CREATE_TABLE_EXPENSES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //delete the expenses table if it already exists and recreate the expenses table
        sqLiteDatabase.execSQL(Constant.DROP_TABLE_EXPENSES);
        onCreate(sqLiteDatabase);
    }


    //add new expense to database
    public boolean addExpense(Expenses expense){//add an expenses to the expenses database with the information of expenses
        SQLiteDatabase db = this.getWritableDatabase();//create database to write
        ContentValues values = new ContentValues();//create a values object to store the expenses information
        values.put(Constant.COLUMN_TRIPID, expense.getTripId());//save tripId of expenses
        values.put(Constant.COLUMN_TYPE, expense.getType());
        values.put(Constant.COLUMN_AMOUNT, expense.getAmount());
        values.put(Constant.COLUMN_TIME, expense.getTime());
        //add an expenses to the expenses database with the expenses information and save the result in result
        long result = db.insert(Constant.TABLE_EXPENSES, null, values);
        if (result == -1){
            return false;
        }else {
            return true;
        }
    }
    //select all expenses from database by trip id and return them as a list

    public List<Expenses> getAllExpenses(int tripId){// get all expenses from database by trip id and return as list of expenses
        String[] columns = {//create an array of columns to get the columns in the database
                Constant.COLUMN_ID,
                Constant.COLUMN_TRIPID,
                Constant.COLUMN_TYPE,
                Constant.COLUMN_AMOUNT,
                Constant.COLUMN_TIME
        };
        String sortOrder = Constant.COLUMN_ID + " ASC";//sort by id ascending to get expenses in chronological order
        List<Expenses> expenseList = new ArrayList<Expenses>();//create an expenses list to store the expenses
        SQLiteDatabase db = this.getReadableDatabase();//create database to read
        // get the expenses from the database by trip id and save it to the cursor to read the expenses
        Cursor cursor = db.query(Constant.TABLE_EXPENSES, columns, Constant.COLUMN_TRIPID + " = ? ", new String[]{String.valueOf(tripId)}, null, null, sortOrder);
        if(cursor.moveToFirst()){//if the cursor has data
            do{//loop
                Expenses expense = new Expenses();//create an expenses to store the information of expenses
                expense.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(Constant.COLUMN_ID))));//get the id of expenses and save it to the newly created expenses
                expense.setTripId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(Constant.COLUMN_TRIPID))));
                expense.setType(cursor.getString(cursor.getColumnIndexOrThrow(Constant.COLUMN_TYPE)));
                expense.setAmount(Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(Constant.COLUMN_AMOUNT))));
                expense.setTime(cursor.getString(cursor.getColumnIndexOrThrow(Constant.COLUMN_TIME)));
                expenseList.add(expense);// add the newly created expenses to the list of expenses
            }while(cursor.moveToNext());//loop until cursor has no data
        }
        cursor.close();//close cursor
        db.close();//close database
        return expenseList;
    }
    //update expense in database by id and return true if success and false if not success
    public boolean updateExpense(Expenses expense){//update an expenses in the database by id and return true if successful and false if failed
        SQLiteDatabase db = this.getWritableDatabase();//create database to write
        ContentValues values = new ContentValues();//create a values object to store the expenses information
        values.put(Constant.COLUMN_TRIPID, expense.getTripId());//save tripId of expenses
        values.put(Constant.COLUMN_TYPE, expense.getType());
        values.put(Constant.COLUMN_AMOUNT, expense.getAmount());
        values.put(Constant.COLUMN_TIME, expense.getTime());
        //update an expenses in the database by id and save the result in rowsUpdated to check if the update was successful
        int rowsUpdated = db.update(Constant.TABLE_EXPENSES, values, Constant.COLUMN_ID + " = ? ", new String[]{String.valueOf(expense.getId())});
        db.close();//close database
        if(rowsUpdated > 0){//if update is successful
            return true;
        }
        return false;
    }
    //delete expense from database by id and return true if success and false if not success
    public boolean deleteExpense(int id){
        SQLiteDatabase db = this.getWritableDatabase();//create database to write
        int rowsDeleted = db.delete(Constant.TABLE_EXPENSES, Constant.COLUMN_ID + " = ? ", new String[]{String.valueOf(id)});//delete an expenses in the database by id and save the result in rowsDeleted to check if the deletion is successful
        db.close();//close database
        if(rowsDeleted > 0){//if successful delete
            return true;
        }
        return false;
    }
    //delete all expenses from database by trip id and return true if success and false if not success
    public boolean deleteAllExpenses(int tripId){
        SQLiteDatabase db = this.getWritableDatabase();//create database to write
        //delete all expenses in the database by trip id and save the results in rowsDeleted to check if the deletion is successful
        int rowsDeleted = db.delete(Constant.TABLE_EXPENSES, Constant.COLUMN_TRIPID + " = ? ", new String[]{String.valueOf(tripId)});
        db.close();//close database
        if(rowsDeleted > 0){//if successful delete
            return true;
        }
        return false;
    }
}
