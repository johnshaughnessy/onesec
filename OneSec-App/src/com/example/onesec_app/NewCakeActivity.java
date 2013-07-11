package com.example.onesec_app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onesec.Kitchen;
import com.example.onesec.impl.cake.Cake;
import com.example.onesec.impl.http.OneSecRestClient;
import com.example.onesec.impl.http.TokenManager;
import com.example.onesec.impl.util.Utilities;
import com.loopj.android.http.RequestParams;

public class NewCakeActivity extends Activity {

	public ImageView thumbnailView;
	public EditText titleEdit;
	public TextView dateView;
	public Button done;
//	private long rowId;
	private String uid;
	public EditText newCakeSprinkle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_cake);
		this.setTitle("New Cake");
		// Show the Up button in the action bar.
		setupActionBar();
		
		thumbnailView = (ImageView)findViewById(R.id.thumbnail);
		titleEdit = (EditText)findViewById(R.id.title);
		dateView = (TextView)findViewById(R.id.date);
		done = (Button)findViewById(R.id.done);
		newCakeSprinkle = (EditText) findViewById(R.id.newCakeSprinkle);
		
//		rowId = getIntent().getLongExtra("newRowId", -2);	// get ID from intent
//		System.out.println("Id is " + rowId);
		
		uid = getIntent().getStringExtra("cake_uid");
		System.out.println("Uid is " + uid);
		
		previewCake();
	}
	
	public void previewCake() {
		Cake cake = Kitchen.getCakeByUid(this, uid);
		
		thumbnailView.setImageBitmap(cake.getThumbnail(this));
		dateView.setText(Utilities.dateToNiceString(cake.getDate()));
		
		thumbnailView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				playCake();
			}
		});
		
		titleEdit.setOnFocusChangeListener(new OnFocusChangeListener() {
	        public void onFocusChange(View v, boolean hasFocus) {
	            if(!hasFocus)
	            	setCakeTitle();
	        }
	    });
		
		done.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setCakeTitle();
				viewCakes();
			}
		});
	}
	
	private void setCakeTitle() {		
		String title = titleEdit.getText().toString();
		Cake cake = Kitchen.getCakeByUid(this, uid);
		cake.setTitle(title);
		Kitchen.updateCakeTitle(this, cake, uid);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_cake, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void viewCakes() {
    	// Create Intent to go to ViewCakesActivity
    	Intent viewSecondsIntent = new Intent(this, ViewCakesActivity.class);
    	startActivity(viewSecondsIntent);
    }
	
	private void playCake() {
		Cake cake = Kitchen.getCakeByUid(this, uid);
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(cake.getVideoUri(), "video/mp4");
		startActivity(intent);
	}
	
	public void uploadCake(View view){
		Cake cake = Kitchen.getCakeByUid(this, uid);
		
		// Upload Cake to Server
				RequestParams params = OneSecRestClient.buildParams(new String[] {"token", "cake[uid]"}, 
									   								new String[] {TokenManager.getToken(this), cake.getId()});
				params = OneSecRestClient.addVideoToParams(params, OneSecRestClient.CAKES_VIDEO_TYPE, cake.getVideoUri());
				OneSecRestClient.post("mobile_cakes", params, OneSecRestClient.getResponseHandler("uploadCake"));
	}
	
	public void addTag(View view){
		Cake cake = Kitchen.getCakeByUid(this, uid);
		String sprinkleTag = newCakeSprinkle.getText().toString();
		// Upload Second to Server
		RequestParams params = OneSecRestClient.buildParams(new String[] {"token", "cake_uid", "sprinkle_tag"}, 
							   								new String[] {TokenManager.getToken(this), cake.getId(), sprinkleTag});
		OneSecRestClient.post("mobile_cake_sprinkles", params, OneSecRestClient.getResponseHandler("addTag"));
		//newCakeSprinkle.setText("");
	}

}
