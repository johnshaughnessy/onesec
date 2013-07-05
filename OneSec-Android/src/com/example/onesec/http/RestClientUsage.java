package com.example.onesec.http;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;

public class RestClientUsage {
	public static void getPublicTimeLine() {
		RestClient.get("statuses/public_timeline.json", null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray timeline) {
				try {
					JSONObject firstEvent = (JSONObject)timeline.get(0);
					String tweetText = firstEvent.getString("text");
					System.out.println(tweetText);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
		});
	}
}
