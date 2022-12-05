package com.cw.javaAndroid.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.cw.javaAndroid.Model.Trips;

import java.util.ArrayList;
import java.util.List;

public class database_Trips extends SQLiteOpenHelper {
    public database_Trips(@Nullable Context context) {
        super(context, Constant.DATABASE_NAME , null, Constant.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Constant.CREATE_TABLE_TRIPS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(Constant.DROP_TABLE_TRIPS);
        onCreate(sqLiteDatabase);
    }
    //add new trip to database and return the id of the new trip in database and return true if success and false if not success
    public boolean addTrip(Trips trip){
        SQLiteDatabase db = this.getWritableDatabase();//create database to write
        ContentValues values = new ContentValues();//create contentvalues to save
        values.put(Constant.COLUMN_NAME, trip.getName());
        values.put(Constant.COLUMN_DESTINATION, trip.getDestination());
        values.put(Constant.COLUMN_DATE, trip.getDate());
        values.put(Constant.COLUMN_REQUIREASSESEMENT, trip.getRequireAssessement());
        values.put(Constant.COLUMN_DESCRIPTION, trip.getDescription());
        //add data to database and return id of new data in database if successful
        long result = db.insert(Constant.TABLE_TRIPS, null, values);
        db.close();//close database
        if(result == -1){//nếu id = -1 -> unsuccessful -> return false
            return false;
        }else{//nếu id khác -1 -> successful -> return true
            return true;
        }
    }
    //select all trips from database and return them as a list
    public List<Trips> getAllTrips(){//return list trip
        String[] columns = {//create array of columns to get data
                Constant.COLUMN_ID,
                Constant.COLUMN_NAME,
                Constant.COLUMN_DESTINATION,
                Constant.COLUMN_DATE,
                Constant.COLUMN_REQUIREASSESEMENT,
                Constant.COLUMN_DESCRIPTION
        };
        String sortOrder = Constant.COLUMN_NAME + " ASC";//sort by name ascending
        List<Trips> tripList = new ArrayList<Trips>();//create a list to save trips
        SQLiteDatabase db = this.getReadableDatabase();//create database to read
        // get data from database and save to cursor in sort order
        Cursor cursor = db.query(Constant.TABLE_TRIPS, columns, null, null, null, null, sortOrder);
        if(cursor.moveToFirst()){//if the cursor has data
            do{//repeat
                Trips trip = new Trips();//create new trip
                trip.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(Constant.COLUMN_ID))));//get the id of the trip from the cursor and save it in a new trip
                trip.setName(cursor.getString(cursor.getColumnIndexOrThrow(Constant.COLUMN_NAME)));
                trip.setDestination(cursor.getString(cursor.getColumnIndexOrThrow(Constant.COLUMN_DESTINATION)));
                trip.setDate(cursor.getString(cursor.getColumnIndexOrThrow(Constant.COLUMN_DATE)));
                trip.setRequireAssessement(cursor.getString(cursor.getColumnIndexOrThrow(Constant.COLUMN_REQUIREASSESEMENT)));
                trip.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(Constant.COLUMN_DESCRIPTION)));
                tripList.add(trip);//add trip to list
            }while(cursor.moveToNext());//until the cursor has no data
        }
        cursor.close();//close cursor
        db.close();//close database
        return tripList;//returns a list of trips
    }
    //delete trip from database by id and return true if success and false if not success
    public boolean deleteTrip(int id){//pass in the id of the trip to delete
        SQLiteDatabase db = this.getWritableDatabase();//create database to write
        return db.delete(Constant.TABLE_TRIPS, Constant.COLUMN_ID + " = ?", new String[]{String.valueOf(id)}) > 0;//delete trip by id and return true if successful and false if failed
    }
    //delete all trips from database and return true if success and false if not success
    public boolean deleteAllTrips(){//return true if successful and false if failed
        SQLiteDatabase db = this.getWritableDatabase();//create database to write
        return db.delete(Constant.TABLE_TRIPS, null, null) > 0;//delete all trips and return true if successful and false if failed
    }

    //update trip in database by id and return true if success and false if not success
    public boolean updateTrip(Trips trip){//pass in trip to update
        SQLiteDatabase db = this.getWritableDatabase();//create database to write
        ContentValues values = new ContentValues();//create a ContentValues object to store the values to be updated
        values.put(Constant.COLUMN_NAME, trip.getName());//get the name of the trip and save it to ContentValues
        values.put(Constant.COLUMN_DESTINATION, trip.getDestination());
        values.put(Constant.COLUMN_DATE, trip.getDate());
        values.put(Constant.COLUMN_REQUIREASSESEMENT, trip.getRequireAssessement());
        values.put(Constant.COLUMN_DESCRIPTION, trip.getDescription());
        int rowsUpdated = db.update(Constant.TABLE_TRIPS, values, Constant.COLUMN_ID + " = ?", new String[]{String.valueOf(trip.getId())});//update trip by id and return updated line number if successful and 0 if failed
        db.close();//close database
        if(rowsUpdated > 0){//if updated line count > 0 -> return true
            return true;
        }else{//opposite -> return false
            return false;
        }
    }
    //search trips
    public List<Trips> search(String name){//function to search for data in the trips table in the database (pass in the name)
        SQLiteDatabase db = this.getWritableDatabase();//create database to write
        List<Trips> list = new ArrayList<>();//initialize list array to store data
        //perform a data query in the trips table in the database (pass in the query statement, name) and save it to the cursor
        Cursor c = db.rawQuery("select * from " + Constant.TABLE_TRIPS + " where " + Constant.COLUMN_NAME + " like ?", new String[]{"%" + name + "%"});
        if(c.moveToFirst()){//if the pointer cursor is not at the end
            do{//thực hiện lặp
                int id = c.getInt(0);// get id data from column 0
                String name1 = c.getString(1);// get name data from column 1
                String destination = c.getString(2);// get name data from column 2
                String date = c.getString(3);// get name data from column 3
                String requireAssessement = c.getString(4);// get name data from column 4
                String description = c.getString(5);// get name data from column 5
                Trips trips = new Trips(id, name1, destination, date, requireAssessement, description);//initialize trips object
                list.add(trips);//add trips object to list array
            }while (c.moveToNext());//execute iteration
        }
        return list;//return array list
    }
}
