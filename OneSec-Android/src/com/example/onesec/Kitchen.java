package com.example.onesec;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.onesec.impl.cake.Batter;
import com.example.onesec.impl.cake.Cake;
import com.example.onesec.impl.database.KitchenCakeDbHelper;
import com.example.onesec.impl.database.KitchenContract;
import com.example.onesec.impl.database.KitchenContract.CakeEntry;
import com.example.onesec.impl.database.KitchenContract.SecondEntry;
import com.example.onesec.impl.database.KitchenContract.SprinkleEntry;
import com.example.onesec.impl.database.KitchenSecondDbHelper;
import com.example.onesec.impl.database.KitchenSprinkleDbHelper;
import com.example.onesec.impl.second.Second;


public class Kitchen {
	public static Long saveSecondToLocalDb(Context context, Second second){
		if (!second.isReadyForSave()){
			return (long) -1;
		}
		
		// Prepare the Second DB for insert
		KitchenSecondDbHelper mDbHelper = new KitchenSecondDbHelper(context);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		ContentValues values = second.generateContentValues();

		// Insert the new row, returning the primary key value of the new row
		long newRowId;
		newRowId = db.insert(
		         SecondEntry.TABLE_NAME,
		         SecondEntry.COLUMN_NAME_NULLABLE,
		         values);
		// note: COLUMN_NAME_NULLABLE=null means a row won't be inserted when there are no data values
		db.close();
		
		return newRowId;
	}
	
	public static Long saveCakeToLocalDb(Context context, Cake cake){
		if (!cake.isReadyForSave()){
			return (long) -1;
		}
		
		// Prepare the Cake DB for insert
		KitchenCakeDbHelper mDbHelper = new KitchenCakeDbHelper(context);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		ContentValues values = cake.generateContentValues();

		// Insert the new row, returning the primary key value of the new row
		long newRowId;
		newRowId = db.insert(
		         CakeEntry.TABLE_NAME,
		         CakeEntry.COLUMN_NAME_NULLABLE,
		         values);
		// note: COLUMN_NAME_NULLABLE=null means a row won't be inserted when there are no data values
		db.close();
		
		return newRowId;
	}
	
	public static Long saveSprinkleToLocalDb(Context context, String videoId, String tag) {
		// Prepare the Sprinkle DB for insert
		KitchenSprinkleDbHelper dbHelper = new KitchenSprinkleDbHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(SprinkleEntry.COLUMN_NAME_VIDEO_ID, videoId);
		values.put(SprinkleEntry.COLUMN_NAME_TAG, tag);
		
		// Insert the new row
		long newRowId;
		newRowId = db.insert(
				SprinkleEntry.TABLE_NAME,
				SprinkleEntry.COLUMN_NAME_NULLABLE,
				values);
		db.close();
		
		return newRowId;
	}
	
	public static Second getSecondByUid(Context context, String uid){
		KitchenSecondDbHelper mDbHelper = new KitchenSecondDbHelper(context);
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		String[] projection = KitchenContract.FULL_SECOND_PROJECTION;

		// How you want the results sorted in the resulting Cursor
		String sortOrder = null;

		Cursor c = db.query(
		    SecondEntry.TABLE_NAME,  					// The table to query
		    projection,                               	// The columns to return
		    SecondEntry.COLUMN_NAME_SECOND_ID+"=?",		// The columns for the WHERE clause
		    new String[]{ uid },                      	// The values for the WHERE clause
		    null,                                     	// don't group the rows
		    null,                                     	// don't filter by row groups
		    sortOrder                                 	// The sort order
		    );
		
		if (c.moveToFirst()) {
			db.close();
			Log.v("getsecbyuid", "new second(c)");
			return new Second(c);
		}
		Log.v("getsecbyuid", "null");
		return null;
	}

	public static Cake getCakeByUid(Context context, String uid){
		KitchenCakeDbHelper mDbHelper = new KitchenCakeDbHelper(context);
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		String[] projection = KitchenContract.FULL_CAKE_PROJECTION;
		String sortOrder = null;

		Cursor c = db.query(
		    CakeEntry.TABLE_NAME,  					  // The table to query
		    projection,                               // The columns to return
		    CakeEntry.COLUMN_NAME_CAKE_ID+"=?",		  // The columns for the WHERE clause
		    new String[]{ uid },                      // The values for the WHERE clause
		    null,                                     // don't group the rows
		    null,                                     // don't filter by row groups
		    sortOrder                                 // The sort order
		    );
		
		if (c.moveToFirst()) {
			db.close();
			return new Cake(c);
		}
		Log.v("getsecbyuid", "null");
		return null;
	}
	
	public static List<String> getSprinklesByUid(Context context, String uid){
		KitchenSprinkleDbHelper dbHelper = new KitchenSprinkleDbHelper(context);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String[] projection = KitchenContract.FULL_SPRINKLE_PROJECTION;
		String sortOrder = null;
	
		Cursor c = db.query(
		    SprinkleEntry.TABLE_NAME,  					// The table to query
		    projection,                               	// The columns to return
		    SprinkleEntry.COLUMN_NAME_VIDEO_ID+"=?",	// The columns for the WHERE clause
		    new String[]{ uid },                        // The values for the WHERE clause
		    null,                                     	// don't group the rows
		    null,                                     	// don't filter by row groups
		    sortOrder                                 	// The sort order
		    );
		
		List<String> sprinklesList = new ArrayList<String>();
		Log.v("getSprinklesByUid", "START");
		if (c.moveToFirst()){
			do{		// do/while? ew
				String spr = c.getString(KitchenContract.SPRINKLE_TAG_COL_NUM);
				sprinklesList.add(spr);
				Log.v("getSprinklesByUid", "adding " + spr);
			} while(c.moveToNext());
		}
		Log.v("getSprinklesByUid", "END");
		c.close();
		db.close();
		
		return sprinklesList;
	}

	// Makes cursor to view all seconds	
	public static Cursor getSecondsCursor(Context context) {
		KitchenSecondDbHelper mDbHelper = new KitchenSecondDbHelper(context);
		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = KitchenContract.FULL_SECOND_PROJECTION;

		// How you want the results sorted in the resulting Cursor
		String sortOrder = SecondEntry._ID + " DESC";
		
		Cursor c = db.query(
			    SecondEntry.TABLE_NAME,  	// The table to query
			    projection,				 	// The columns to return
			    null,		  			 	// The columns for the WHERE clause
			    null,					 	// The values for the WHERE clause
			    null,					 	// don't group the rows
			    null,					 	// don't filter by row groups
			    sortOrder				 	// The sort order
			    );

		return c;
	}
	
	// Returns list of the IDs of all videos (seconds and cakes) with given sprinkle
	public static ArrayList<String> getUidsBySprinkle(Context context, String sprinkle) {
		KitchenSprinkleDbHelper mDbHelper = new KitchenSprinkleDbHelper(context);
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		String[] projection = KitchenContract.FULL_SPRINKLE_PROJECTION;
		String sortOrder = SprinkleEntry._ID + " DESC";
		
		Cursor c = db.query(
			    SprinkleEntry.TABLE_NAME,  					// The table to query
			    projection,				 					// The columns to return
			    SprinkleEntry.COLUMN_NAME_TAG+"=?",			// The columns for the WHERE clause
			    new String[]{ sprinkle },                   // The values for the WHERE clause
			    null,					 					// don't group the rows
			    null,					 					// don't filter by row groups
			    sortOrder				 					// The sort order
			    );

		ArrayList<String> uidList = new ArrayList<String>();
		if (c.moveToFirst()){
			do{
				String uid = c.getString(KitchenContract.SPRINKLE_VIDEO_ID_COL_NUM);
				if(uid.charAt(0) == 'S')
					Log.v("getUidsBySprinkle", "adding " + uid);
					uidList.add(uid);		// add uid to list if it's a second
			} while(c.moveToNext());
			db.close();
			c.close();
		}
		
		return uidList;
	}
	
	
	// Makes cursor to view all cakes
	public static Cursor getCakesCursor(Context context) {
		KitchenCakeDbHelper mDbHelper = new KitchenCakeDbHelper(context);
		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = KitchenContract.FULL_CAKE_PROJECTION;
		
		Log.v("getcakescursor", "making cake cursor");

		// How you want the results sorted in the resulting Cursor
		String sortOrder = CakeEntry._ID + " DESC";
		
		Cursor c = db.query(
			    CakeEntry.TABLE_NAME,  		// The table to query
			    projection,                 // The columns to return
			    null,		  				// The columns for the WHERE clause
			    null,                       // The values for the WHERE clause
			    null,						// don't group the rows
			    null,						// don't filter by row groups
			    sortOrder					// The sort order
			    );
		
		return c;
	}
	
	public static void writeBatterToFile(Context context, Batter batter) {
		File batterFile = new File(batter.getUri().getPath());
		List<String> idList = batter.getIdList();
		
		try {
			int listLength = idList.size();
			FileOutputStream out = new FileOutputStream(batterFile);
		    for (int i = 0; i < listLength; i++) {
		    	Log.v("writeBatterToFile", "writing " + idList.get(i));
		        out.write(idList.get(i).getBytes());
		    }
		    out.close();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}

	public static void updateCakeTitle(Context context, Cake cake) {
		// Prepare the Cake DB for insert
		KitchenCakeDbHelper mDbHelper = new KitchenCakeDbHelper(context);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(CakeEntry.COLUMN_NAME_TITLE, cake.getTitle());
		Log.v("updateCakeTitle", "title is " + cake.getTitle());
	
		// Insert the new row
		db.update(
		         CakeEntry.TABLE_NAME,					// table name
		         values,								// values to update to
		         CakeEntry.COLUMN_NAME_CAKE_ID+"=?",	// WHERE clause
		         new String[]{ cake.getId() });			// WHERE arguments
		db.close();
	}
}
