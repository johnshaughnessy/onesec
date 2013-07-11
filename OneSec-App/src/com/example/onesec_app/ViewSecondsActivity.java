package com.example.onesec_app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.example.onesec.Kitchen;
import com.example.onesec.impl.cake.Batter;
import com.example.onesec.impl.cake.Cake;
import com.example.onesec.impl.database.KitchenContract;
import com.example.onesec.impl.second.Second;
import com.example.onesec_app.adapters.SecondsCursorAdapter;

public class ViewSecondsActivity extends Activity {

//	private ListView secondsListView;
	private Batter batter;
	private boolean selectorOn;
//	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setTitle("My Seconds");
		setContentView(R.layout.activity_view_seconds);
		// Show the Up button in the action bar.
		setupActionBar();
		
		batter = new Batter();
		selectorOn = false;
		Button selectButton = (Button)findViewById(R.id.select_seconds);
		selectButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				selectorOn = !selectorOn;
			}
		});
		
		showSeconds();
	}
	
	/*
	 * Loads all Seconds
	 */
	private void showSeconds() {
		final Cursor c = Kitchen.getSecondsCursor(this);
		
		String[] fromColumns = {
				KitchenContract.SecondEntry.COLUMN_NAME_DATE,
				KitchenContract.SecondEntry.COLUMN_NAME_THUMBNAIL_PATH };
		int[] toViews = {
				R.id.secondDate,
				R.id.secondThumbnail };
		c.moveToFirst();
		SecondsCursorAdapter adapter = new SecondsCursorAdapter(this, 
		        R.layout.listview_seconds_row, c, fromColumns, toViews, 0);
		Log.v("second adapter string", adapter.toString());
		ListView listView = (ListView)findViewById(R.id.secondsListView);
		Log.v("seconds listview count", ""+listView.getCount());
		listView.setAdapter(adapter);
		//listView.setLongClickable(true);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
				c.moveToPosition(pos);
				Second second = new Second(c);
				if(selectorOn)
				{
					//SecondsCursorAdapter adapter = (SecondsCursorAdapter) adapterView.getAdapter();
					batter.addSecond(second);
				}
				else{
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.setDataAndType(second.getVideoUri(), "video/mp4");
					startActivity(intent);
				}
			}
		});
	}
	
	public void bakeCake(View v) {
		Cake cake = batter.bake(this);
		
		// Save cake and its batter locally
		//Kitchen.saveCakeToLocalDb(this, cake);
		Kitchen.writeBatterToFile(this, batter);
		
		
		
		Intent intent = new Intent(this, NewCakeActivity.class);
		intent.putExtra("newRowId", Kitchen.saveCakeToLocalDb(this, cake));
    	startActivity(intent);
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
