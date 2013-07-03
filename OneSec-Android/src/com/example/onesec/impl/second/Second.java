package com.example.onesec.impl.second;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;

import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;

public class Second {
	
	private int id;
	private Uri thumbnailUri;
	private Uri videoUri;
	private Date date;
	private boolean isChecked;
	
	public Second(Uri vidUri){
		videoUri = vidUri;
		thumbnailUri = createBitmapUri(vidUri);
		date = getDateFromUri();		
		isChecked = false;
	}
	
	// TODO: This nonsense
	@SuppressWarnings("deprecation")
	// This is actually the worst.
	private Date getDateFromUri(){
		String dateString = videoUri.getLastPathSegment();
		if (!dateString.substring(4,5).equals("c")){
			int year = Integer.parseInt(dateString.substring(4, 8));
			int month = Integer.parseInt(dateString.substring(8, 10));
			int day = Integer.parseInt(dateString.substring(10, 12));
			int hour = Integer.parseInt(dateString.substring(13, 15));
			int minute = Integer.parseInt(dateString.substring(15, 17));
			int second = Integer.parseInt(dateString.substring(17, 19));

			return new Date(year, month, day, hour, minute, second);
		}
		return new Date();
		
	}
	
	// Returns a Uri to a thumbnail for the video at vidUri
	public Uri createBitmapUri(Uri vidUri)
	{
		String path = vidUri.getPath().replace("VID", "PNG").replace("mp4", "png");
		File thumbnailFile = new File(path);
		
		if(!thumbnailFile.exists())
		{
			Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(vidUri.getPath(), 3);
			FileOutputStream out;
			try {
				out = new FileOutputStream(thumbnailFile);
				thumbnail.compress(Bitmap.CompressFormat.PNG, 90, out);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return Uri.fromFile(thumbnailFile);		
	}
	
	public Bitmap getThumbnail(ContentResolver cr)
	{
		try {
			return MediaStore.Images.Media.getBitmap(cr, thumbnailUri);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null; // TODO me
	}

	public void toggleChecked() {
		isChecked = !isChecked;
	}

	@SuppressWarnings("resource")
	public Movie getMovie() throws FileNotFoundException, IOException {
		// TODO Figure out why everything throws exceptions and requires suppression.
		return MovieCreator.build(new FileInputStream(videoUri.getPath()).getChannel());
	}
	
	// Getters and setters
	public Uri getThumbnailUri()
	{
		return thumbnailUri;
	}

	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public int getId() {
		return id;
	}

	public Uri getVideoUri() {
		return videoUri;
	}

	public Date getDate() {
		return date;
	}

}
