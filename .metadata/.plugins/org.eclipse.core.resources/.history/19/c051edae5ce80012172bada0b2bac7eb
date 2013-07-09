package com.example.onesec.impl.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.onesec.impl.database.KitchenContract.SecondEntry;

public class KitchenDbHelper extends SQLiteOpenHelper {
	// If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";
    
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
        "CREATE TABLE " + SecondEntry.TABLE_NAME + " (" +
        SecondEntry._ID + " INTEGER PRIMARY KEY," +
        SecondEntry.COLUMN_NAME_SECOND_ID + TEXT_TYPE + COMMA_SEP +
        SecondEntry.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
        SecondEntry.COLUMN_NAME_THUMBNAIL_PATH + TEXT_TYPE + COMMA_SEP +
        SecondEntry.COLUMN_NAME_VIDEO_PATH + TEXT_TYPE +
        " )";

    private static final String SQL_DELETE_ENTRIES =
        "DROP TABLE IF EXISTS " + SecondEntry.TABLE_NAME;
    
    public KitchenDbHelper(Context context) {
    	super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
