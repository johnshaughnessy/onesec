package com.example.onesec_app;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onesec.impl.database.KitchenContract.SecondEntry;
import com.example.onesec.impl.database.KitchenDbHelper;

public class NewSecondActivity extends Activity {

	private long id;
	public TextView dateView;
	public ImageView thumbnailView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_second);
		
		dateView = (TextView)findViewById(R.id.date);
		thumbnailView = (ImageView)findViewById(R.id.thumbnail);
		
		id = getIntent().getLongExtra("id", -2);	// get ID from intent
		System.out.println("Id is " + id);
		
		previewSecond();
	}
	
	public void previewSecond() {
		KitchenDbHelper mDbHelper = new KitchenDbHelper(this);
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

//		Cursor cursor = db.query(TABLE_NAME, new String[] {"_id", "title", "title_raw"}, 
//                "title_raw like " + "'%Smith%'", null, null, null, null);
//		
		
		Cursor c = db.query(
		    SecondEntry.TABLE_NAME,  // The table to query
		    projection,                               // The columns to return
		    SecondEntry._ID+"=?",		  // The columns for the WHERE clause
		    new String[]{ Long.toString(id) },                         		   // The values for the WHERE clause
		    null,                                     // don't group the rows
		    null,                                     // don't filter by row groups
		    sortOrder                                 // The sort order
		    );
		
		String[] colNames = c.getColumnNames();
		for(String s : colNames)
		{
			System.out.println("col is " + s);
		}
		
		if(c.moveToFirst()) {
			Log.v("id", c.getString(0));
			Log.v("date", c.getString(1));
			Log.v("vidUri", c.getString(2));
			Log.v("thumbnailUri", c.getString(3));
		}
		
		
//		String id = c.getString(0);
//		String date = c.getString(1);
//		String vidPath = c.getString(2);
//		
//		System.out.println("date is " + date);
//		
//		dateView.setText(date);
		//thumbnailView.setImageBitmap(thumbnail);
	}
	
    public void viewSeconds(View v) {
    	// Create Intent to go to ViewSecondsActivity
    	Intent viewSecondsIntent = new Intent(this, ViewSecondsActivity.class);
    	startActivity(viewSecondsIntent);
    }

	
//	public void playSecond(View view) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException{
//		String url = "https://onesecvids.s3.amazonaws.com/uploads/second/video/18/VID_20130705_151401.mp4?AWSAccessKeyId=AKIAIR367AZSNWO4RXXQ&Signature=JWx2g2Y5rW9Cqw74r6zDvJwLfok%3D&Expires=1373066101"; // your URL here
//		MediaPlayer mediaPlayer = new MediaPlayer();
//		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//		mediaPlayer.setDataSource(url);
//		
//		OnPreparedListener opl = new OnPreparedListener() {
//			public void onPrepared(MediaPlayer mp) {
//				mp.start();
//			}
//		};
//		mediaPlayer.setOnPreparedListener(opl);
//		mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
//	}
}
