package com.example.onesec.impl.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.onesec.impl.database.KitchenContract.CakeEntry;

public class KitchenCakeDbHelper extends SQLiteOpenHelper {
	// If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Cakes.db";
    
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
        "CREATE TABLE " + CakeEntry.TABLE_NAME + " (" +
        CakeEntry._ID + " INTEGER PRIMARY KEY," +
        CakeEntry.COLUMN_NAME_CAKE_ID + TEXT_TYPE + COMMA_SEP +
        CakeEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
        CakeEntry.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
        CakeEntry.COLUMN_NAME_THUMBNAIL_PATH + TEXT_TYPE + COMMA_SEP +
        CakeEntry.COLUMN_NAME_VIDEO_PATH + TEXT_TYPE + COMMA_SEP +
        CakeEntry.COLUMN_NAME_BATTER_PATH + TEXT_TYPE +
        " )";

    private static final String SQL_DELETE_ENTRIES =
        "DROP TABLE IF EXISTS " + CakeEntry.TABLE_NAME;
    
    public KitchenCakeDbHelper(Context context) {
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
