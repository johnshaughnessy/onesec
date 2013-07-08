package com.example.onesec.impl.http;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class TokenManager {
	public static final String NOTOKEN = "notoken";
	
	public static void generateAndSaveToken(final Context ctx, String email, String password){
		RequestParams params = OneSecRestClient.buildParams(new String[] {"email", "password"}, new String[] {email, password});
		OneSecRestClient.post("tokens.json", params, new AsyncHttpResponseHandler(){
			@Override
    		public void onStart() {
    			Log.v("generate token", "onStart()");
    			super.onStart();
    		}
    		@Override
    		public void onSuccess(String response) {
    			Log.v("generate token", "onSuccess");
    			String token = NOTOKEN;
    			try {
    				JSONObject jObject = new JSONObject(response);
					token = jObject.getString("token");
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
    			Log.v("generate token", response);
    			saveToken(ctx, token);
    		}
    		
    		@Override
    		public void onFailure(Throwable e, String response) {
    			// Response failed :(
    			Log.v("generate token", "onFailure() has the response: " + response);
    			e.printStackTrace();
    			super.onFailure(e, response);
    		}
    		
    		@Override
    		public void onFinish() {
    			// Completed the request (either success or failure)
    			Log.v("generate token", "onFinish()");
    			super.onFinish();
    		}
    	});
	}
	
	public static String getToken(Context ctx){
		SharedPreferences sharedPref = ctx.getSharedPreferences("token preferences", Context.MODE_PRIVATE);
		return sharedPref.getString("token", NOTOKEN);
	}
	
	public static void saveToken(Context ctx, String token){
		SharedPreferences sharedPref = ctx.getSharedPreferences("token preferences", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString("token", token);
		editor.commit();
	}
	
	public static void forgetToken(Context ctx){
		SharedPreferences sharedPref = ctx.getSharedPreferences("token preferences", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString("token", NOTOKEN);
		editor.commit();
	}
}
