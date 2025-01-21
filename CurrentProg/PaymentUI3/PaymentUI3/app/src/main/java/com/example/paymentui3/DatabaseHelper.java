package com.example.paymentui3;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "charity.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "charities";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table query
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT NOT NULL);";
        db.execSQL(CREATE_TABLE);

        // Insert some sample data
        db.execSQL("INSERT INTO " + TABLE_NAME + " (name) VALUES ('None')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (name) VALUES ('Charity A')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (name) VALUES ('Charity B')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (name) VALUES ('Charity C')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method to retrieve charity names from the database
    public List<String> getCharityNames() {
        List<String> charities = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_NAME + " FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                charities.add(cursor.getString(0)); // Get the name column
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return charities;
    }
}
