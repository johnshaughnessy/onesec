package com.example.onesec.impl.http;

import java.io.File;
import java.io.FileNotFoundException;

import android.net.Uri;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class OneSecRestClient {
	
    private static final String BASE_URL = "http://54.218.123.27:3000/";
	public static final String SECONDS_VIDEO_TYPE = "second";
	public static final String CAKES_VIDEO_TYPE = "cake";
	
	public static AsyncHttpResponseHandler getResponseHandler(String... responseHandler){
		final String tag = (responseHandler.length > 0) ? responseHandler[0] : "generic";
		return new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				Log.v(tag + " response handler", "onStart()");
				super.onStart();
			}
			@Override
			public void onSuccess(String response) {
				Log.v(tag + " response handler", "onSuccess() has the response: \n" + response);
			}
			
			@Override
			public void onFailure(Throwable e, String response) {
				Log.v(tag + " response handler", "onFailure() has the response: \n" + response + "\n\n");
				e.printStackTrace();
				super.onFailure(e, response);
			}
			
			@Override
			public void onFinish() {
				// Completed the request (either success or failure)
				Log.v(tag + " response handler", "onFinish()");
				super.onFinish();
			}
		};
		
	}

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
    	client.setTimeout(30000);
      	client.post(getAbsoluteUrl(url), params, responseHandler);
    }


    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
    
    public static RequestParams buildParams(String[] keys, String[] values){
	    RequestParams params = new RequestParams();
	    int keysLength = keys.length;
	    for (int i = 0; i < keysLength; ++i){
	      params.put(keys[i], values[i]);
	    }
	    return params;
    }
    
    public static RequestParams addVideoToParams(RequestParams params, String type, Uri videoUri){
        File file = new File(videoUri.getPath());
        try {
      	  params.put(type + "[video]", file);
        } catch (FileNotFoundException e1) {
      	  e1.printStackTrace();
        }
        return params;
    }
    
}
