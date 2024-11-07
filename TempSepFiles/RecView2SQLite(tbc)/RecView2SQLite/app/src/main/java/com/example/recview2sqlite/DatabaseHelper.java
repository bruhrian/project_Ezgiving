package com.example.recview2sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ImageDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Images";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_IMAGE = "image";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_IMAGE + " BLOB)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertImage(String name, byte[] imageData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("image", imageData);

        db.insert("Images", null, values);
        db.close();
    }

    // Helper method to convert image to byte array
    public byte[] imageToByteArray(String imagePath) {
        try {
            File file = new File(imagePath);
            FileInputStream fis = new FileInputStream(file);
            byte[] byteArray = new byte[(int) file.length()];
            fis.read(byteArray);
            fis.close();
            return byteArray;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}

