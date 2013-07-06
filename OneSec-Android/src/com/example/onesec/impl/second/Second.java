package com.example.onesec.impl.second;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.example.onesec.impl.database.KitchenContract.SecondEntry;
import com.example.onesec.impl.database.KitchenDbHelper;
import com.example.onesec.impl.util.Utilities;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;

public class Second {
	
	private static final int MEDIA_TYPE_VIDEO = 0;
	private static final int MEDIA_TYPE_THUMBNAIL = 1;
	
	private String id;
	private Uri videoUri;
	private Uri thumbnailUri;
	private Date date;
	
	public Second(){
		id = generateId();
		date = new Date();
		videoUri = makeVideoUri(date);
		thumbnailUri = makeThumbnailUri(date);
	}
	
	public Second(String id, Date date, Uri vUri, Uri tUri){
		this.id = id;
		this.date = date;
		videoUri = vUri;
		thumbnailUri = tUri;
	}
	
	private String generateId() {
		return "sec_" +(new Random()).nextInt();
	}

	/** Create a file Uri for saving an image or video */
    private static Uri makeVideoUri(Date date){
    	return Uri.fromFile(makeSecondFile(date, MEDIA_TYPE_VIDEO));
    }
    
    /** Create a file Uri for saving an image or video */
    private static Uri makeThumbnailUri(Date date){
          return Uri.fromFile(makeSecondFile(date, MEDIA_TYPE_THUMBNAIL));
    }

	/** Create a File for saving an image or video */
    private static File makeSecondFile(Date date, int type){    	
    	if(!isExternalStorageWritable()) {
    		// TODO error - can't write to external storage
    		Log.v("makeSecondFile", "external storage isn't writable");
    		return null;
    	}
    	
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_MOVIES), "OneSec/Seconds");
		
		// Create the storage directory if it does not exist
		if (! mediaStorageDir.exists()){
			if (! mediaStorageDir.mkdirs()){
				Log.d("OneSec", "failed to create directory");
				return null;
			}
		}

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(date);
        Log.v("makeSecondFile", "timestamp is " + timeStamp);
        Log.v("makeSecondFile", "type is " + type);
        
        File mediaFile;
       
        if (type == MEDIA_TYPE_VIDEO){
        	mediaFile = new File(mediaStorageDir.getPath() + File.separator +
        	"VID_"+ timeStamp + ".mp4");
        } else if (type == MEDIA_TYPE_THUMBNAIL){
        	mediaFile = new File(mediaStorageDir.getPath() + File.separator +
            "PIC_"+ timeStamp + ".png");
        } else {
        	Log.d("OneSec", "failed to create mediaFile");
        	return null;
        }
        
        return mediaFile;
    }
	
/*
//	@SuppressWarnings("deprecation")
//	// This is actually the worst.
//	private Date getDateFromUri(){
//		String dateString = videoUri.getLastPathSegment();
//		if (!dateString.substring(4,5).equals("c")){
//			int year = Integer.parseInt(dateString.substring(4, 8));
//			int month = Integer.parseInt(dateString.substring(8, 10));
//			int day = Integer.parseInt(dateString.substring(10, 12));
//			int hour = Integer.parseInt(dateString.substring(13, 15));
//			int minute = Integer.parseInt(dateString.substring(15, 17));
//			int second = Integer.parseInt(dateString.substring(17, 19));
//
//			return new Date(year, month, day, hour, minute, second);
//		}
//		return new Date();
//		
//	}
*/
	
    
    public long addToKitchen(Context context){
    	Log.v("second", "preparing to add to kitchen");
    	if (videoUriIsValid() && createThumbnail()){
    		Log.v("second", "adding to kitchen");
    		// Create new DBHelper
    		KitchenDbHelper mDbHelper = new KitchenDbHelper(context);
    		
    		// Gets the data repository in write mode
    		SQLiteDatabase db = mDbHelper.getWritableDatabase();

    		// Create a new map of values, where column names are the keys
    		ContentValues values = new ContentValues();
    		values.put(SecondEntry.COLUMN_NAME_SECOND_ID, id);
    		values.put(SecondEntry.COLUMN_NAME_DATE, Utilities.dateToString(date));
    		values.put(SecondEntry.COLUMN_NAME_VIDEO_PATH, videoUri.getPath());
    		values.put(SecondEntry.COLUMN_NAME_THUMBNAIL_PATH, thumbnailUri.getPath());

    		// Insert the new row, returning the primary key value of the new row
    		long newRowId;
    		newRowId = db.insert(
    		         SecondEntry.TABLE_NAME,
    		         SecondEntry.COLUMN_NAME_NULLABLE,
    		         values);
    		// note: COLUMN_NAME_NULLABLE=null means a row won't be inserted when there are no data values
    		
    		return newRowId;
    	}
    	Log.v("second", "failed to add to kitchen");
    	return -1;
    }
    
    
    
	private boolean createThumbnail() {
		File thumbnailFile = new File(thumbnailUri.getPath());
		
		if(!thumbnailFile.exists()){
			Bitmap thumbBmp = ThumbnailUtils.createVideoThumbnail(videoUri.getPath(), 3);
			FileOutputStream out;
			try{
				out = new FileOutputStream(thumbnailFile);
				thumbBmp.compress(Bitmap.CompressFormat.PNG, 90, out);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	/* Checks if external storage is available for read and write */
	public static boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}

	/* Checks if external storage is available to at least read */
	public static boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}

	private boolean videoUriIsValid() {
		// TODO Auto gen
		return true;
	}

	// Getters and setters
	public Uri getThumbnailUri()
	{
		return thumbnailUri;
	}

	public String getId() {
		return id;
	}

	public Uri getVideoUri() {
		return videoUri;
	}

	public Date getDate() {
		return date;
	}
	
	@SuppressWarnings("resource")
	public Movie getMovie() throws FileNotFoundException, IOException {
		// TODO Figure out why everything throws exceptions and requires suppression.
		return MovieCreator.build(new FileInputStream(videoUri.getPath()).getChannel());
	}
	
	public Bitmap getThumbnail(Context ctx)
	{
		try {
			return MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), thumbnailUri);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
