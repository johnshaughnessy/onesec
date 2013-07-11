package com.example.onesec_app;


import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onesec.Kitchen;
import com.example.onesec.impl.http.OneSecRestClient;
import com.example.onesec.impl.http.TokenManager;
import com.example.onesec.impl.second.Second;
import com.example.onesec.impl.util.Utilities;
import com.loopj.android.http.RequestParams;

public class NewSecondActivity extends Activity {

	public TextView dateView;
	public ImageView thumbnailView;
	public EditText newSecSprinkle;
	private String uid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setTitle("New Second");
		setContentView(R.layout.activity_new_second);
		
		dateView = (TextView)findViewById(R.id.date);
		thumbnailView = (ImageView)findViewById(R.id.thumbnail);
		
		uid = getIntent().getStringExtra("second_uid");
		Log.v("NewSecondActivity", "Uid is " + uid);		
		
		newSecSprinkle = (EditText) findViewById(R.id.newSecSprinkle);
		
		previewSecond();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_second, menu);
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_settings:
			// show settings
			break;
		case R.id.action_signout:
			signout();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}
	
	public void previewSecond() {
		Second second = Kitchen.getSecondByUid(this, uid);
		
		dateView.setText(getDateString(second.getDate()));
		thumbnailView.setImageBitmap(second.getThumbnail(this));
		
		thumbnailView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				playSecond();
			}
		});
	}
	
	private String getDateString(Date date) {
		String dateStr = Utilities.dateToString(date);
		String year = dateStr.substring(0, 4);
		String month = getMonth(Integer.parseInt(dateStr.substring(4, 6)));
		String day = dateStr.substring(6, 8);
		int hour = Integer.parseInt(dateStr.substring(9, 11));
		int minute = Integer.parseInt(dateStr.substring(11, 13));
		int second = Integer.parseInt(dateStr.substring(13, 15));

		String secondNum = NumberFormat.getNumberInstance(Locale.US).format((hour*3600)+(minute*60)+second);
		
		return "Second " + secondNum + " of " + month + " " + day + ", " + year;
	}
	
	private String getMonth(int month) {
	    return new DateFormatSymbols().getMonths()[month-1];
	}
	
    public void viewSeconds(View v) {
    	// Create Intent to go to ViewSecondsActivity
    	Intent viewSecondsIntent = new Intent(this, ViewSecondsActivity.class);
    	startActivity(viewSecondsIntent);
    }

	private void playSecond() {
		Second second = Kitchen.getSecondByUid(this, uid);
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(second.getVideoUri(), "video/mp4");
		startActivity(intent);
	}
	
	public void uploadSecond(View view){
		Second second = Kitchen.getSecondByUid(this, uid);
		
		// Upload Second to Server
		RequestParams params = OneSecRestClient.buildParams(new String[] {"token", "second[uid]", "second[date]"}, 
							   								new String[] {TokenManager.getToken(this), second.getId(), Utilities.dateToString(second.getDate())});
		params = OneSecRestClient.addVideoToParams(params, OneSecRestClient.SECONDS_VIDEO_TYPE, second.getVideoUri());
		OneSecRestClient.post("mobile_seconds", params, OneSecRestClient.getResponseHandler("uploadSecond"));
	}
	
	public void addTag(View view){
		Second second = Kitchen.getSecondByUid(this, uid);
		String sprinkleTag = newSecSprinkle.getText().toString();
		
		// Save Sprinkle to local database
		Kitchen.saveSprinkleToLocalDb(this, second.getId(), sprinkleTag);
				
		// Upload Sprinkle to Server
		RequestParams params = OneSecRestClient.buildParams(new String[] {"token", "second_uid", "sprinkle_tag"}, 
							   								new String[] {TokenManager.getToken(this), second.getId(), sprinkleTag});
		OneSecRestClient.post("mobile_sec_sprinkles", params, OneSecRestClient.getResponseHandler("uploadSecond"));
		newSecSprinkle.setText("");
	}
	
    private void signout() {
    	TokenManager.forgetToken(this);
    	Log.v("signout", "forgetting token");
    }
}
