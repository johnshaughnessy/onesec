package com.example.onesec_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.onesec.Kitchen;
import com.example.onesec.impl.http.OneSecRestClient;
import com.example.onesec.impl.http.TokenManager;
import com.example.onesec.impl.second.Second;
import com.example.onesec.impl.util.Utilities;
import com.loopj.android.http.RequestParams;

public class MainActivity extends Activity {
	
	private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private Long newRowId;
	private Second second;
	@SuppressWarnings("unused")
	private Kitchen kitchen;
	String token;
	Context context = this;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Restore preferences
        //SharedPreferences tokenPrefs = getSharedPreferences("token preferences", 0);
        
        if (TokenManager.getToken(this).equals(TokenManager.NOTOKEN)){
        	Log.v("onCreate", "no token was found");
        	TokenManager.generateAndSaveToken(this, "a@ex.com", "password");
        } 
        
        Button forgetToken = (Button) findViewById(R.id.forget_token);
        forgetToken.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		TokenManager.forgetToken(context);
        	}
        });
        
        //makeRequest();
    }
    

    @Override
    protected void onSaveInstanceState (Bundle outState){
    	if (newRowId != null){
    		outState.putLong("newRowId", newRowId);
    	}
    	
    	super.onSaveInstanceState(outState);
    }
    
    @Override
    protected void onRestoreInstanceState (Bundle savedInstanceState){
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
    			
    			// Upload Second to Server
    			RequestParams params = OneSecRestClient.buildParams(new String[] {"token", "second[uid]", "second[date]"}, 
    								   								new String[] {TokenManager.getToken(this), second.getId(), Utilities.dateToString(second.getDate())});
    			params = OneSecRestClient.addVideoToParams(params, OneSecRestClient.SECONDS_VIDEO_TYPE, second.getVideoUri());
    			OneSecRestClient.post("mobile_seconds", params, OneSecRestClient.GENERIC_RESPONSE_HANDLER);
    			
    			 
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
