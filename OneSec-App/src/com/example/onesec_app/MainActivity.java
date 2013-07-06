package com.example.onesec_app;

import java.io.File;
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
import com.example.onesec.impl.http.OneSecRestClientUsage;
import com.example.onesec.impl.second.Second;

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
    
//    public static void makeRequest() {
//        AsyncHttpClient client = new AsyncHttpClient();
//        
//        RequestParams params = new RequestParams();
//        params.put("second[date]", "nested date");
//        params.put("second[uid]", "nested uid");
//
//        client.post("http://54.218.123.27:3000/seconds", params, new AsyncHttpResponseHandler() {
//        	@Override
//            public void onStart() {
//                Log.v("js client", "onStart()");
//                super.onStart();
//            }
//        	@Override
//            public void onSuccess(String response) {
//        		Log.v("js client", "onSuccess");
//                System.out.println(response);
//            }
//        	
//            @Override
//            public void onFailure(Throwable e, String response) {
//                // Response failed :(
//            	Log.v("js client", "onFailure() has the response: " + response);
//            	e.printStackTrace();
//            	super.onFailure(e, response);
//            }
//            
//            @Override
//            public void onFinish() {
//                // Completed the request (either success or failure)
//            	Log.v("js client", "onFinish()");
//            	super.onFinish();
//            }
//
//        });
//        
//        
//    }
    
    
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
    			long newRowId = second.addToKitchen(this);
    			Log.v("onActivityResult", "newRowId = " + newRowId);
    			OneSecRestClientUsage client = new OneSecRestClientUsage();
    			
    			// TODO put this in addToKitchen later
    			client.saveSecondToServer(second);
    			
    			// Send ID to NewSecondActivity and start activity
    			Intent intent = new Intent(this, NewSecondActivity.class);
    			intent.putExtra("id", newRowId);
    			intent.putExtra("sec_vUri", second.getVideoUri().getPath());
    			intent.putExtra("sec_uid", second.getId());
    			startActivity(intent);
    		}
    		else if(resultCode == RESULT_CANCELED) {
    			// User cancelled video capture
    		}
    		else {
    			// Video capture failed
    		}
    	}
    }
    
    public void takeSecond(View v) {
    	// Create new [empty] Second and get its location
    	second = new Second();
    	Uri videoUri = second.getVideoUri();
    	
    	// Create Intent to capture video
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        
        // Create file to save the video and name it
        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 1);	// 1 second video
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);	// highest quality
        
        System.out.println("beforehand, second is null:" + (second == null));
    	// Start Intent to capture video
    	startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
    }
    
    public void viewSeconds(View v) {
    	// Create Intent to go to ViewSecondsActivity
    	Intent intent = new Intent(this, ViewSecondsActivity.class);
    	startActivity(intent);
    }

}
