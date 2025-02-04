package com.example.mybookingswadminlogs.myBookings.adminlogs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminLogsDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AdminLogs.db";
    private static final int DATABASE_VERSION = 2; // Incremented version for schema changes
    private static final String TABLE_NAME = "logs";
    private static final String COL_ID = "ID";
    private static final String COL_USERNAME = "username";
    private static final String COL_TYPE = "type";
    private static final String COL_ORGANISATION = "organisation";
    private static final String COL_TYPE2 = "type2"; // New column

    public AdminLogsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db1) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT, " +
                COL_TYPE + " TEXT, " +
                COL_ORGANISATION + " TEXT, " +
                COL_TYPE2 + " TEXT DEFAULT 'confirmed' " + // New column with default value
                ")";
        db1.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COL_TYPE2 + " TEXT DEFAULT 'confirmed'");
        }
    }

    // Insert log with default 'confirmed' status
    public long insertLog(String username, String type, String organisation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_USERNAME, username);
        contentValues.put(COL_TYPE, type);
        contentValues.put(COL_ORGANISATION, organisation);
        contentValues.put(COL_TYPE2, "confirmed"); // Default value for confirmed
        return db.insert(TABLE_NAME, null, contentValues);
    }

    // Retrieve all logs
    public Cursor getAllLogs() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    // Update log type2 to "cancelled"
    public int updateLogToCancelled(String organisation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TYPE2, "cancelled");
        return db.update(TABLE_NAME, contentValues, COL_ORGANISATION + "=?", new String[]{organisation});
    }

    public int updateLogStatus(String username, String type, String organisation, String cancelled) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TYPE2, "Cancelled");

        return db.update(TABLE_NAME, values, COL_ORGANISATION + "=?", new String[]{organisation});
    }
}
