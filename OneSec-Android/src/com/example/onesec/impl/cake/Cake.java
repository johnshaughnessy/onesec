package com.example.onesec.impl.cake;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

public class Cake {
	
	private static final int MEDIA_TYPE_VIDEO = 0;
	private static final int MEDIA_TYPE_THUMBNAIL = 1;
	
	private String id;
	public String title;
	private Date date;
	private Batter batter;
	private Uri videoUri;
	private Uri thumbnailUri;
	private Uri batterUri;
	
	public Cake() {
		id = generateId();
		date = new Date();
		videoUri = makeVideoUri(date);
		thumbnailUri = makeThumbnailUri(date);
	}
	
	public Cake(Batter b, Uri vUri, Uri tUri) {
		batter = b;
		videoUri = vUri;
		thumbnailUri = tUri;
	}
	
	private String generateId() {
		return "cake_" +(new Random()).nextInt();
	}

	/** Create a file URI for saving an image or video */
    private static Uri makeVideoUri(Date date){
    	return Uri.fromFile(makeCakeFile(date, MEDIA_TYPE_VIDEO));
    }
    
    /** Create a file URI for saving an image or video */
    private static Uri makeThumbnailUri(Date date){
          return Uri.fromFile(makeCakeFile(date, MEDIA_TYPE_THUMBNAIL));
    }

	/** Create a File for saving an image or video */
    private static File makeCakeFile(Date date, int type){    	
    	if(!isExternalStorageWritable()) {
    		// TODO error - can't write to external storage
    		Log.v("makeCakeFile", "external storage isn't writable");
    		return null;
    	}
    	
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_MOVIES), "OneSec/Cakes");
		
		// Create the storage directory if it does not exist
		if (! mediaStorageDir.exists()){
			if (!mediaStorageDir.mkdirs()){
				Log.d("OneSec", "failed to create OneSec/Cakes directory");
				return null;
			}
		}

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(date);
        Log.v("makeCakeFile", "timestamp is " + timeStamp);
        Log.v("makeCakeFile", "type is " + type);
        
        File mediaFile;
       
        if (type == MEDIA_TYPE_VIDEO){
        	mediaFile = new File(mediaStorageDir.getPath() + File.separator +
        	"VID_"+ timeStamp + ".mp4");
        } else if (type == MEDIA_TYPE_THUMBNAIL){
        	mediaFile = new File(mediaStorageDir.getPath() + File.separator +
            "PIC_"+ timeStamp + ".png");
        } else {
        	Log.d("OneSec", "failed to create cake mediaFile");
        	return null;
        }
        
        return mediaFile;
    }

	private boolean createThumbnail() {
		File thumbnailFile = new File(thumbnailUri.getPath());
		
		if(!thumbnailFile.exists()){
			Bitmap thumbBmp = ThumbnailUtils.createVideoThumbnail(videoUri.getPath(), 1);
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
		return (new File(videoUri.getPath())).exists();
	}
	
	public boolean isReadyForSave(){
    	return (videoUriIsValid() && createThumbnail());
    }

	public Batter getBatter() {
		return batter;
	}
	public void setBatter(Batter batter) {
		this.batter = batter;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	public Uri getVideoUri() {
		return videoUri;
	}

	public Uri getThumbnailUri() {
		return thumbnailUri;
	}
	
	public Bitmap getThumbnail(Context context)
	{
		try {
			return MediaStore.Images.Media.getBitmap(context.getContentResolver(), thumbnailUri);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Uri getBatterUri() {
		return batterUri;
	}
}
