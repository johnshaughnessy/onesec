package com.example.onesec_app;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.onesec.Kitchen;
import com.example.onesec.impl.http.OneSecRestClient;
import com.example.onesec.impl.second.Second;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MainActivity extends Activity {
	
	private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private Long newRowId;
	private Second second;
	@SuppressWarnings("unused")
	private Kitchen kitchen;
	String token;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //makeRequest();
    }
    
//    @Override
//    protected void onResume() {
//    	checkLogin();
//    }
    
//    private void checkLogin() {
//    	AccountManager am = new AccountManager(); 
//    	if (am.getUserData("user") == null) {
//    	    Intent i = new Intent(this, LoginActivity.class);
//    	    startActivityForResult(i, GlobalContext.REQUEST_LOGIN);
//    	}
//    }
    
    private void makeRequest()
    {
    	File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_MOVIES), "OneSec/Seconds");
		
        String vidPath = mediaStorageDir.getPath() + File.separator + "VID_20130703_143114.mp4";
    	Log.v("uri", vidPath);
    	
    	File file = new File(vidPath);
    	
    	RequestParams params = new RequestParams();
    	params.put("token", "c3LRZKS8wAuD7q3Bjj9a");
    	params.put("second[uid]", "superduperuid");
    	try {
			params.put("second[video]", file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//        String token = "";
//        params.put("token", token);
    	
    	OneSecRestClient.post("mobile_seconds", params, new AsyncHttpResponseHandler() {
    		@Override
    		public void onStart() {
    			Log.v("js client", "onStart()");
    			super.onStart();
    		}
    		@Override
    		public void onSuccess(String response) {
    			Log.v("js client", "onSuccess");
    			
//    			try {
//    				JSONObject jObject = new JSONObject(response);
//					token = jObject.getString("token");
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//    			Log.v("js client", "token is " + token + "!!!!!!!");
    			Log.v("response", response);
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
    public boolean onOptionsItemSelected(MenuItem item) {
    	// Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_new_second:
                takeSecond(findViewById(R.layout.activity_main));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
    
    public void viewCakes(View v) {
    	// Create intent to go to ViewCakesActivity
    	Intent intent = new Intent(this, ViewCakesActivity.class);
    	startActivity(intent);
    }

}
