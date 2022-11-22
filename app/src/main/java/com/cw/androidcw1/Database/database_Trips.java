package com.cw.androidcw1.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.cw.androidcw1.Database.Constant.*;

import androidx.annotation.Nullable;

import com.cw.androidcw1.Model.Trips;

import java.util.ArrayList;
import java.util.List;

public class database_Trips extends SQLiteOpenHelper {
    public database_Trips(@Nullable Context context) {
        super(context,DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_TRIPS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE_TRIPS);
        onCreate(sqLiteDatabase);
    }
    //add new trip to database and return the id of the new trip in database and return true if success and false if not success
    //thêm một trip vào database và trả về id của trip mới trong database và trả về true nếu thành công và false nếu không thành công
    public boolean addTrip(Trips trip){
        SQLiteDatabase db = this.getWritableDatabase();//tạo database để ghi
        ContentValues values = new ContentValues();//tạo contentvalues để lưu dữ liệu
        values.put(COLUMN_NAME, trip.getName());
        values.put(COLUMN_DESTINATION, trip.getDestination());
        values.put(COLUMN_DATE, trip.getDate());
        values.put(COLUMN_REQUIREASSESEMENT, trip.getRequireAssessement());
        values.put(COLUMN_DESCRIPTION, trip.getDescription());
        long result = db.insert(TABLE_TRIPS, null, values);//thêm dữ liệu vào database và trả về id của dữ liệu mới trong database nếu thành công
        db.close();//đóng database
        if(result == -1){//nếu id = -1 thì không thành công
            return false;//trả về false
        }else{//nếu id khác -1 thì thành công
            return true;//trả về true
        }
    }
    //select all trips from database and return them as a list
    //lấy tất cả các trip từ database và trả về dưới dạng list
    public List<Trips> getAllTrips(){//trả về list các trip
        String[] columns = {//tạo mảng các cột cần lấy dữ liệu
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_DESTINATION,
                COLUMN_DATE,
                COLUMN_REQUIREASSESEMENT,
                COLUMN_DESCRIPTION
        };
        String sortOrder = COLUMN_NAME + " ASC";//sắp xếp theo tên tăng dần
        List<Trips> tripList = new ArrayList<Trips>();//    tạo list để lưu các trip
        SQLiteDatabase db = this.getReadableDatabase();//tạo database để đọc
        Cursor cursor = db.query(TABLE_TRIPS, columns, null, null, null, null, sortOrder);//lấy dữ liệu từ database và lưu vào cursor theo thứ tự sắp xếp
        if(cursor.moveToFirst()){//nếu cursor có dữ liệu
            do{//lặp lại
                Trips trip = new Trips();//tạo trip mới
                trip.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID))));//lấy id của trip từ cursor và lưu vào trip mới
                trip.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
                trip.setDestination(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESTINATION)));
                trip.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
                trip.setRequireAssessement(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REQUIREASSESEMENT)));
                trip.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                tripList.add(trip);//thêm trip vào list
            }while(cursor.moveToNext());// đến khi cursor không còn dữ liệu
        }
        cursor.close();//đóng cursor
        db.close();//đóng database
        return tripList;//trả về list các trip
    }
    //delete trip from database by id and return true if success and false if not success
    //xóa trip từ database theo id và trả về true nếu thành công và false nếu không thành công
    public boolean deleteTrip(int id){//truyền vào id của trip cần xóa
        SQLiteDatabase db = this.getWritableDatabase();//tạo database để ghi
        return db.delete(TABLE_TRIPS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}) > 0;//xóa trip theo id và trả về true nếu thành công và false nếu không thành công
    }
    //delete all trips from database and return true if success and false if not success
    //xóa tất cả các trip từ database và trả về true nếu thành công và false nếu không thành công
    public boolean deleteAllTrips(){//trả về true nếu thành công và false nếu không thành công
        SQLiteDatabase db = this.getWritableDatabase();//tạo database để ghi
        return db.delete(TABLE_TRIPS, null, null) > 0;//xóa tất cả các trip và trả về true nếu thành công và false nếu không thành công
    }

    //update trip in database by id and return true if success and false if not success
    //cập nhật trip trong database theo id và trả về true nếu thành công và false nếu không thành công
    public boolean updateTrip(Trips trip){//truyền vào trip cần cập nhật
        SQLiteDatabase db = this.getWritableDatabase();//tạo database để ghi
        ContentValues values = new ContentValues();//tạo một đối tượng ContentValues để lưu các giá trị cần cập nhật
        values.put(COLUMN_NAME, trip.getName());//lấy tên của trip và lưu vào ContentValues
        values.put(COLUMN_DESTINATION, trip.getDestination());
        values.put(COLUMN_DATE, trip.getDate());
        values.put(COLUMN_REQUIREASSESEMENT, trip.getRequireAssessement());
        values.put(COLUMN_DESCRIPTION, trip.getDescription());
        int rowsUpdated = db.update(TABLE_TRIPS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(trip.getId())});//cập nhật trip theo id và trả về số dòng được cập nhật nếu thành công và 0 nếu không thành công
        db.close();//đóng database
        if(rowsUpdated > 0){//nếu số dòng được cập nhật > 0
            return true;//trả về true
        }else{//nếu không
            return false;//trả về false
        }
    }
    //search trips
    public List<Trips> search(String name){//hàm tìm kiếm dữ liệu trong bảng trips trong database (truyền vào tên)
        SQLiteDatabase db = this.getWritableDatabase();//tạo database để ghi
        List<Trips> list = new ArrayList<>();//khởi tạo mảng list để lưu trữ dữ liệu
        Cursor c = db.rawQuery("select * from " + TABLE_TRIPS + " where " + COLUMN_NAME + " like ?", new String[]{"%" + name + "%"});//thực hiện truy vấn dữ liệu trong bảng trips trong database (truyền vào câu lệnh truy vấn, tên) và lưu vào con trỏ c
        if(c.moveToFirst()){//nếu con trỏ c chưa đến cuối
            do{//thực hiện lặp
                int id = c.getInt(0);//lấy dữ liệu id từ cột 0(được lấy từ con trỏ c)
                String name1 = c.getString(1);//lấy dữ liệu tên từ cột 1(được lấy từ con trỏ c)
                String destination = c.getString(2);//lấy dữ liệu địa điểm đến từ cột 2(được lấy từ con trỏ c)
                String date = c.getString(3);//lấy dữ liệu ngày đi từ cột 3(được lấy từ con trỏ c)
                String requireAssessement = c.getString(4);//lấy dữ liệu yêu cầu đánh giá từ cột 4(được lấy từ con trỏ c)
                String description = c.getString(5);//lấy dữ liệu mô tả từ cột 5(được lấy từ con trỏ c)
                Trips trips = new Trips(id, name1, destination, date, requireAssessement, description);//khởi tạo đối tượng trips
                list.add(trips);//thêm đối tượng trips vào mảng list
            }while (c.moveToNext());//thực hiện lặp
        }
        return list;//trả về mảng list
    }
}
