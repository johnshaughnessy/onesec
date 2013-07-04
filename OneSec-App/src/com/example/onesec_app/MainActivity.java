package com.example.onesec_app;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
        
        Log.v("******", "CALLING SENDREQUEST");
        String output = sendRequest();
        Log.v("******", output);
        
        kitchen = new Kitchen();
    }
    
    private String sendRequest()
    {
    	String uri = "https://www.google.com";
    	
    	URL url = null;
    	try {
    		url = new URL(uri);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	
    	HttpURLConnection urlConnection = null;
    	try {
    		urlConnection = (HttpURLConnection)url.openConnection();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	Log.v("conn", "just made urlconnection");
    	
    	try {
    		urlConnection.setDoInput(true);
    		Log.v("conn", "about to make inputstream");
    	    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
    	    Log.v("conn", "just got inputstream");
    	    String response = readStream(in);
    	    Log.v("conn", "just read response");
    	    return response;
    	} catch (IOException e) {
    		e.printStackTrace();
    	} finally {
    	    urlConnection.disconnect();
    	}
    	
    	return null;
//    	AsyncHttpClient client = new AsyncHttpClient();
//    	client.get("http://www.google.com", new AsyncHttpResponseHandler() {
//    	    @Override
//    	    public void onSuccess(String response) {
//    	        System.out.println(response);
//    	    }
//    	});
//    	
//    	RequestParams params = new RequestParams();
//    	params.put("name", "value");
//    	params.put("more", "data");
//
//    	// Testing for OneSecRestClient
//    	try {
//    		(new OneSecRestClientUsage()) .getPublicTimeline();
//    	} catch (JSONException e) {
//    		// TODO Auto-generated catch block
//    		e.printStackTrace();
//    	}
    }
    
    private String readStream(InputStream in)
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String result = null;
		String line = null;
		Log.v("readStream", "about to read stream");
		// Read first line
		try {
			line = reader.readLine();
			Log.v("readStream", "first line is " + line);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		result = line;
		
		// Read rest of lines
		try {
			while((line=reader.readLine()) != null){
			    result += line;
			    Log.v("readStream", "next line is " + line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
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
