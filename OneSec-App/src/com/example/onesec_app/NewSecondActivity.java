package com.example.onesec_app;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
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
		
		previewSecond();
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

}
