package com.example.onesec_app;

import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.onesec.Kitchen;
import com.example.onesec.impl.second.Second;
import com.example.onesec_app.adapters.SecondsAdapter;

public class ViewSecondsActivity extends Activity {

	private ListView secondsListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_seconds);
		// Show the Up button in the action bar.
		setupActionBar();
		
		showSeconds();
	}
	
	/*
	 * Loads all Seconds
	 */
	private void showSeconds()
	{
		List<Second> seconds = Kitchen.allSeconds;
		//Log.v("showSeconds", "first second date is " + Utilities.dateToString(seconds.get(0).getDate()));
		secondsListView = (ListView)findViewById(R.id.secondsListView);
		
		SecondsAdapter adapter = new SecondsAdapter(this, 
		        R.layout.listview_seconds_row, seconds);
		
		secondsListView.setAdapter(adapter);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_seconds, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
