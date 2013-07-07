package com.example.onesec;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.onesec.impl.database.KitchenContract.SecondEntry;
import com.example.onesec.impl.database.KitchenDbHelper;
import com.example.onesec.impl.second.Second;
import com.example.onesec.impl.util.Utilities;


public class Kitchen {
	public static Long saveSecondToLocalDb(Context context, Second second){
		if (! second.isReadyForSave()){
			return (long) -1;
		}
		
		// Prepare the DB for insert
		KitchenDbHelper mDbHelper = new KitchenDbHelper(context);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		ContentValues values = generateContentValuesForSecond(second);

		// Insert the new row, returning the primary key value of the new row
		long newRowId;
		newRowId = db.insert(
		         SecondEntry.TABLE_NAME,
		         SecondEntry.COLUMN_NAME_NULLABLE,
		         values);
		// note: COLUMN_NAME_NULLABLE=null means a row won't be inserted when there are no data values
		
		return newRowId;
	}
	
	private static ContentValues generateContentValuesForSecond(Second second){
		
		ContentValues values = new ContentValues();
		values.put(SecondEntry.COLUMN_NAME_SECOND_ID, second.getId());
		values.put(SecondEntry.COLUMN_NAME_DATE, Utilities.dateToString(second.getDate()));
		values.put(SecondEntry.COLUMN_NAME_VIDEO_PATH, second.getVideoUri().getPath());
		values.put(SecondEntry.COLUMN_NAME_THUMBNAIL_PATH, second.getThumbnailUri().getPath());
		return values;
	}
	
	public static Second getSecondById(Context context, Long rowId){
		KitchenDbHelper mDbHelper = new KitchenDbHelper(context);
		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = {
		    SecondEntry.COLUMN_NAME_SECOND_ID,
		    SecondEntry.COLUMN_NAME_DATE,
		    SecondEntry.COLUMN_NAME_VIDEO_PATH,
		    SecondEntry.COLUMN_NAME_THUMBNAIL_PATH
		    };

		// How you want the results sorted in the resulting Cursor
		String sortOrder = null;
//		    SecondEntry.COLUMN_NAME_UPDATED + " DESC";

		Cursor c = db.query(
		    SecondEntry.TABLE_NAME,  // The table to query
		    projection,                               // The columns to return
		    SecondEntry._ID+"=?",		  // The columns for the WHERE clause
		    new String[]{ Long.toString(rowId) },                         		   // The values for the WHERE clause
		    null,                                     // don't group the rows
		    null,                                     // don't filter by row groups
		    sortOrder                                 // The sort order
		    );
		
		if (c.moveToFirst()) {
			return new Second(c);
		}
		return null;
	}
	
	public static Cursor getSecondsCursor(Context context) {
		KitchenDbHelper mDbHelper = new KitchenDbHelper(context);
		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = {
			SecondEntry._ID,
		    SecondEntry.COLUMN_NAME_SECOND_ID,
		    SecondEntry.COLUMN_NAME_DATE,
		    SecondEntry.COLUMN_NAME_VIDEO_PATH,
		    SecondEntry.COLUMN_NAME_THUMBNAIL_PATH
		    };

		// How you want the results sorted in the resulting Cursor
		String sortOrder = SecondEntry._ID + " DESC";
		
		Cursor c = db.query(
			    SecondEntry.TABLE_NAME,  // The table to query
			    projection,                               // The columns to return
			    null,		  // The columns for the WHERE clause
			    null,                         		   // The values for the WHERE clause
			    null,                                     // don't group the rows
			    null,                                     // don't filter by row groups
			    sortOrder                                 // The sort order
			    );
		
		return c;
	}
}
