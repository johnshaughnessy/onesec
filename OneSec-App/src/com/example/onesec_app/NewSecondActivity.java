package com.example.onesec_app;

import java.io.IOException;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onesec.Kitchen;
import com.example.onesec.impl.second.Second;
import com.example.onesec.impl.util.Utilities;

public class NewSecondActivity extends Activity {

	private String id;
	public TextView dateView;
	public ImageView thumbnailView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_second);
		
		dateView = (TextView)findViewById(R.id.date);
		thumbnailView = (ImageView)findViewById(R.id.thumbnail);
		
		id = getIntent().getStringExtra("id");	// get ID from intent
		System.out.println("Id is " + id);
		
		//previewSecond();
	}
	
	public void previewSecond() {
		Second second = Kitchen.getSecond(id);
		Date date = second.getDate();
		Bitmap thumbnail = second.getThumbnail(this);
		
		System.out.println("date is " + Utilities.dateToString(date));
		
		dateView.setText(Utilities.dateToString(date));
		thumbnailView.setImageBitmap(thumbnail);
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
