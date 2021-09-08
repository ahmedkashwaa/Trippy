package com.example.Project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHistory extends SQLiteOpenHelper {
    public static final String TITLE = "title";
    public static final String START_POINT = "startPoint";
    public static final String END_POINT = "endPoint";
    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String STATUS = "status";
    public static final String NOTES = "NOTES";


    public DBHistory(@Nullable Context context) {
        super(context, "History.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       // String statment = "CREATE TABLE TRIPS(" + "_id " + "INTEGER PRIMARY KEY AUTOINCREMENT ,"+ TITLE + " TEXT,"+ " " + START_POINT + " " +" TEXT,"+ END_POINT + " TEXT," + " " + DATE + " " +" TEXT," + " " + TIME +" TEXT,"+ " " + STATUS +" TEXT )";
        String statment = "CREATE TABLE TRIPS(" + "_id " + "INTEGER PRIMARY KEY AUTOINCREMENT ,"+ TITLE + " TEXT,"+ " " + START_POINT + " " +" TEXT,"+ END_POINT + " TEXT," + " " + DATE + " " +" TEXT," + " " + TIME +" TEXT,"+ " " + STATUS +" TEXT, "+" "+ NOTES + " TEXT)";

        db.execSQL(statment);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
