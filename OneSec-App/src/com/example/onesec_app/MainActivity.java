package com.example.onesec_app;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.example.onesec.Kitchen;
import com.example.onesec.impl.second.Second;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MainActivity extends Activity {
	
	private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private Second second;
	@SuppressWarnings("unused")
	private Kitchen kitchen;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v("main", "onCreate");
        
        kitchen = new Kitchen();

        
        //makeRequest();
        
    }
    
    public static void makeRequest() {
        AsyncHttpClient client = new AsyncHttpClient();
        
        RequestParams params = new RequestParams();
        params.put("second[date]", "nested date");
        params.put("second[uid]", "nested uid");

        client.post("http://54.218.123.27:3000/seconds", params, new AsyncHttpResponseHandler() {
        	@Override
            public void onStart() {
                Log.v("js client", "onStart()");
                super.onStart();
            }
        	@Override
            public void onSuccess(String response) {
        		Log.v("js client", "onSuccess");
                System.out.println(response);
            }
        	
            @Override
            public void onFailure(Throwable e, String response) {
                // Response failed :(
            	Log.v("js client", "onFailure() has the response: " + response);
            	e.printStackTrace();
            	super.onFailure(e, response);
            }
            
            @Override
            public void onFinish() {
                // Completed the request (either success or failure)
            	Log.v("js client", "onFinish()");
            	super.onFinish();
            }

        });
        
        
    }
    
    public static void storeSecond(Second second) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(30000);
        
        RequestParams params = new RequestParams();
        params.put("second[date]", second.getDate().toString());
        params.put("second[uid]", second.getId());
        
        File videoFile = new File(second.getVideoUri().getPath());
        
        try {
			params.put("second[video]", videoFile);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
       

        client.post("http://54.218.123.27:3000/seconds", params, new AsyncHttpResponseHandler() {
        	@Override
            public void onStart() {
                Log.v("js client", "onStart()");
                super.onStart();
            }
        	@Override
            public void onSuccess(String response) {
        		Log.v("js client", "onSuccess");
                System.out.println(response);
            }
        	
            @Override
            public void onFailure(Throwable e, String response) {
                // Response failed :(
            	Log.v("js client", "onFailure() has the response: " + response);
            	e.printStackTrace();
            	super.onFailure(e, response);
            }
            
            @Override
            public void onFinish() {
                // Completed the request (either success or failure)
            	Log.v("js client", "onFinish()");
            	super.onFinish();
            }

        });
        
       
        
        
    }
    
    @Override
    protected void onSaveInstanceState (Bundle outState){
    	if (second != null){
    		outState.putString("sec_id", second.getId());
        	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
        	outState.putString("sec_date_str", formatter.format(second.getDate()));
        	outState.putString("sec_vUri", second.getVideoUri().getPath());
        	outState.putString("sec_tUri", second.getThumbnailUri().getPath());
    	}
    	
    	super.onSaveInstanceState(outState);
    }
    
    @Override
    protected void onRestoreInstanceState (Bundle savedInstanceState){
    	if (savedInstanceState.containsKey("sec_id") &&
    			savedInstanceState.containsKey("sec_date_str") &&
    			savedInstanceState.containsKey("sec_vUri") &&
    			savedInstanceState.containsKey("sec_tUri")){
    		
    		Log.v("restore", "restoring the second");
    		
    		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
        	Date date = new Date();
        	try {
        		date = formatter.parse(savedInstanceState.getString("sec_date_str"));
        	} catch (java.text.ParseException e) {
				
				e.printStackTrace();
			}
        	second = new Second(savedInstanceState.getString("sec_id"),
        						date,
        						Uri.fromFile(new File(savedInstanceState.getString("sec_vUri"))),
        						Uri.fromFile(new File(savedInstanceState.getString("sec_tUri"))));
        	super.onRestoreInstanceState(savedInstanceState);
    	}
    	
    }
    
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
    		if(resultCode == RESULT_OK) {
    			System.out.println("afterwards, second is null: " + (second == null));
    			// Video successfully captured and saved to videoUri
    			second.addToKitchen();
    			storeSecond(second);
    			
    			// Send ID to NewSecondActivity and start activity
    			Intent newSecondIntent = new Intent(this, NewSecondActivity.class);
    			newSecondIntent.putExtra("id", second.getId());
    			startActivity(newSecondIntent);
    		}
    		else if(resultCode == RESULT_CANCELED) {
    			// User cancelled video capture
    		}
    		else {
    			// Video capture failed
    		}
    	}
    }
    
    public void takeSecond(View v){
    	// Create new [empty] Second and get its location
    	second = new Second();
    	Uri videoUri = second.getVideoUri();
    	
    	// Create Intent to capture video
        Intent takeSecondIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        
        // Create file to save the video and name it
        takeSecondIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
        takeSecondIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 1);	// 1 second video
        takeSecondIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);	// highest quality
        
        System.out.println("beforehand, second is null:" + (second == null));
    	// Start Intent to capture video
    	startActivityForResult(takeSecondIntent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
    }
}
