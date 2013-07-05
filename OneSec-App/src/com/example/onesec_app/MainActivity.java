package com.example.onesec_app;

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
        
        kitchen = new Kitchen();
        
        for (int i = 0; i < 1; ++i){
        	makeRequest();
        	Log.v("js client", "request made");
        }
        
    }
    
    public static void makeRequest() {
        AsyncHttpClient client = new AsyncHttpClient();
        
        RequestParams params = new RequestParams();
        params.put("second", "\"date\"=>\"fsdjk\"");
        params.put("second_uid", "jsdkfa");

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
    		if(resultCode == RESULT_OK) {
    			// Video successfully captured and saved to videoUri
    			second.addToKitchen();
    			
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
        
    	// Start Intent to capture video
    	startActivityForResult(takeSecondIntent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
    }
}
