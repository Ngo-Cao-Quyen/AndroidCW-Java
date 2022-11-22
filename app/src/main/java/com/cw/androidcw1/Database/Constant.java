package com.cw.androidcw1.Database;

public class Constant {
        //khai báo các biến để truy vấn database và các biến khác cần thiết cho chương trình
        public static final String DATABASE_NAME = "trips.db";
        public static final int DATABASE_VERSION = 2;
        public static final String TABLE_TRIPS = "trips";
        public static final String TABLE_EXPENSES = "expenses";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESTINATION = "destination";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_REQUIREASSESEMENT = "requireAssessement";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_TRIPID = "tripId";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_TIME = "time";
        //tạo câu lệnh tạo bảng trips
        public static final String CREATE_TABLE_TRIPS = "CREATE TABLE " + TABLE_TRIPS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_DESTINATION + " TEXT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_REQUIREASSESEMENT + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT"
                + ")";
        //tạo câu lệnh tạo bảng expenses
        public static final String CREATE_TABLE_EXPENSES = "CREATE TABLE " + TABLE_EXPENSES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + COLUMN_TRIPID + " INTEGER,"
                + COLUMN_TYPE + " TEXT,"
                + COLUMN_AMOUNT + " REAL,"
                + COLUMN_TIME + " TEXT"
                + ")";
        //tạo câu lệnh tạo user ,hoten, email, taikhoan, matkhau,sdt
        public static final String CREATE_TABLE_USER = "CREATE TABLE user ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "hoten TEXT,"
                + "email TEXT,"
                + "taikhoan TEXT,"
                + "matkhau TEXT,"
                + "sdt TEXT"
                + ")";
        //câu lệnh xóa bảng trips(để dùng cho hàm onUpgrade) nếu bảng trips đã tồn tại
        public static final String DROP_TABLE_TRIPS = "DROP TABLE IF EXISTS " + TABLE_TRIPS;
        //câu lệnh xóa bảng expenses(để dùng cho hàm onUpgrade) nếu bảng expenses đã tồn tại
        public static final String DROP_TABLE_EXPENSES = "DROP TABLE IF EXISTS " + TABLE_EXPENSES;
        //câu lệnh xóa bảng user(để dùng cho hàm onUpgrade) nếu bảng user đã tồn tại
        public static final String DROP_TABLE_USER = "DROP TABLE IF EXISTS user";
}
