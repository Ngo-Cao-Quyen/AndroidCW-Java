package com.cw.javaAndroid.Database;

public class Constant {
        //declare variables to query the database and other variables needed by the program
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
        //create a statement to create the trips . table
        public static final String CREATE_TABLE_TRIPS = "CREATE TABLE " + TABLE_TRIPS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_DESTINATION + " TEXT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_REQUIREASSESEMENT + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT"
                + ")";
        //create statement to create table expenses
        public static final String CREATE_TABLE_EXPENSES = "CREATE TABLE " + TABLE_EXPENSES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + COLUMN_TRIPID + " INTEGER,"
                + COLUMN_TYPE + " TEXT,"
                + COLUMN_AMOUNT + " REAL,"
                + COLUMN_TIME + " TEXT"
                + ")";
        // statement to delete the trips table (for onUpgrade function) if the trips table already exists
        public static final String DROP_TABLE_TRIPS = "DROP TABLE IF EXISTS " + TABLE_TRIPS;
        // statement to delete the expenses table (for onUpgrade function) if the expenses table already exists
        public static final String DROP_TABLE_EXPENSES = "DROP TABLE IF EXISTS " + TABLE_EXPENSES;
        // statement to delete the user table (for onUpgrade function) if the user table already exists
        public static final String DROP_TABLE_USER = "DROP TABLE IF EXISTS user";
}
