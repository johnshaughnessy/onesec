package com.example.onesec_app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;

import com.example.onesec.impl.second.Cabinet;
import com.example.onesec.impl.second.Second;

public class MainActivity extends Activity {
	
	private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private Second second;
	private Cabinet cabinet;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    			second.addToCabinet();
    			
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
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        
        // Create file to save the video and name it
        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
        takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 1);	// 1 second video
        takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);	// highest quality
        
    	// Start Intent to capture video
    	startActivityForResult(takeVideoIntent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
    }
}
