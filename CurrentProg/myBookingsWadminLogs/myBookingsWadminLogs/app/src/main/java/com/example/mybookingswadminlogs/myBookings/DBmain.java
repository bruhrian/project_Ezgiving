package com.example.mybookingswadminlogs.myBookings;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBmain extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BookingApp.db";
    private static final int DATABASE_VERSION = 2; // INCREMENT VERSION

    public static final String TABLE_NAME = "Bookings";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_ORGANIZATION = "organization";
    public static final String COLUMN_TYPE2 = "COL_TYPE2"; // New column

    public DBmain(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_ORGANIZATION + " TEXT, " +
                COLUMN_TYPE2 + " TEXT DEFAULT 'Confirmed')"; // New column with default value
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Insert a record into the database
    public long insertRecord(String type, String organization) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_ORGANIZATION, organization);
        values.put(COLUMN_TYPE2, "Confirmed"); // Ensure every insert has "Confirmed" by default
        return db.insert(TABLE_NAME, null, values);
    }

    // Retrieve all records
    public Cursor getAllRecords() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}
