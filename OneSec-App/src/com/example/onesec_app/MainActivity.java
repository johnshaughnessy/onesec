package com.example.onesec_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;

import com.example.onesec.Kitchen;
import com.example.onesec.impl.second.Second;

public class MainActivity extends Activity {
	
	private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private Long newRowId;
	private Second second;
	@SuppressWarnings("unused")
	private Kitchen kitchen;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        

    }
    
    
    @Override
    protected void onSaveInstanceState (Bundle outState){
    	if (newRowId != null){
    		outState.putLong("newRowId", newRowId);
//    		outState.putString("sec_id", second.getId());
//        	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
//        	outState.putString("sec_date_str", formatter.format(second.getDate()));
//        	outState.putString("sec_vUri", second.getVideoUri().getPath());
//        	outState.putString("sec_tUri", second.getThumbnailUri().getPath());
    	}
    	
    	super.onSaveInstanceState(outState);
    }
    
    @Override
    protected void onRestoreInstanceState (Bundle savedInstanceState){
//    	if (savedInstanceState.containsKey("sec_id") &&
//    			savedInstanceState.containsKey("sec_date_str") &&
//    			savedInstanceState.containsKey("sec_vUri") &&
//    			savedInstanceState.containsKey("sec_tUri")){
//    		
//    		Log.v("restore", "restoring the second");
//    		
//    		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
//        	Date date = new Date();
//        	try {
//        		date = formatter.parse(savedInstanceState.getString("sec_date_str"));
//        	} catch (java.text.ParseException e) {
//				
//				e.printStackTrace();
//			}
//        	second = new Second(savedInstanceState.getString("sec_id"),
//        						date,
//        						Uri.fromFile(new File(savedInstanceState.getString("sec_vUri"))),
//        						Uri.fromFile(new File(savedInstanceState.getString("sec_tUri"))));
//        	
//    	}
    	if (savedInstanceState.containsKey("newRowId")){
    		newRowId = savedInstanceState.getLong("newRowId");
    		second = Kitchen.getSecondById(this, newRowId);
    	}
    	super.onRestoreInstanceState(savedInstanceState);
    	
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
    			
    			// TODO put this in addToKitchen later
    			// OneSecRestClientUsage client = new OneSecRestClientUsage();
    			// client.saveSecondToServer(second);
    			
    			// Send ID to NewSecondActivity and start activity
    			Intent intent = new Intent(this, NewSecondActivity.class);
    			intent.putExtra("newRowId", Kitchen.saveSecondToLocalDb(this, second));
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
    	second = new Second();
    	newRowId = Kitchen.saveSecondToLocalDb(this, second);
    	
    	// Create Intent to capture video
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        
        intent.putExtra(MediaStore.EXTRA_OUTPUT, second.getVideoUri());
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 1);	// 1 second video
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);	// highest quality
        
    	startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
    }
    
    public void viewSeconds(View v) {
    	// Create Intent to go to ViewSecondsActivity
    	Intent intent = new Intent(this, ViewSecondsActivity.class);
    	startActivity(intent);
    }

}
