package com.example.onesec_app;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
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
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;

import com.example.onesec.Kitchen;
import com.example.onesec.impl.cake.Batter;
import com.example.onesec.impl.cake.Cake;
import com.example.onesec.impl.database.KitchenContract;
import com.example.onesec.impl.http.TokenManager;
import com.example.onesec.impl.second.Second;
import com.example.onesec_app.adapters.SecondsArrayAdapter;
import com.example.onesec_app.adapters.SecondsCursorAdapter;

public class ViewSecondsActivity extends Activity {

//	private ListView secondsListView;
	private Batter batter;
	private boolean selectorOn;
	private Context mContext;
//	private int LIST = 0;
//	private int GRID = 1;
//	private int viewType;
	private ListView listView;
	public View row;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_seconds);
		// Show the Up button in the action bar.
		setupActionBar();
		
		batter = new Batter();
		selectorOn = false;
		changeSelectButtonColor(R.color.white);
		mContext = this;
//		viewType = LIST;
		Button selectButton = (Button)findViewById(R.id.select_seconds);
		
		selectButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (selectorOn){
					changeSelectButtonColor(R.color.white);
				}else{
					changeSelectButtonColor(R.color.hot_pink);
				}
				selectorOn = !selectorOn;
			}
		});
		
		showSeconds();
	}
	
	protected void changeSelectButtonColor(int color) {
		Button selectButton = (Button) findViewById(R.id.select_seconds);
		selectButton.setTextColor(getResources().getColor(color));
		
	}

	@SuppressLint("NewApi")
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_seconds, menu);
        
        // Associate searchable configuration with the SearchView
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        	Log.v("onCreateOptionsMenu", "searchable");
        	SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        	SearchView searchView = (SearchView)menu.findItem(R.id.search).getActionView();
        	searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        	
        	searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
	            @Override
	            public boolean onQueryTextSubmit(String sprinkle) {
	                if (sprinkle.length() != 0) {
	                    Log.v("onQueryTextSubmit", "sprinkle is " + sprinkle);
	                    showSeconds(sprinkle);
	                    return true;
	                }
	                else {
	                	showSeconds();
	                }
	                return false;
	            }
	            @Override
	            public boolean onQueryTextChange(String newText) {
	            	if(newText.length() == 0) {
	            		Log.v("onQueryTextChange", "showing all seconds");
	            		showSeconds();
	            	}
	            	return false;
	            }
	        });
        	searchView.setOnCloseListener(new OnCloseListener() {
                @Override
                public boolean onClose() {
                	Log.v("onClose", "showing seconds again");
                	showSeconds();
                    return false;
                }
            });
        }
        
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_settings:
			// show settings
			break;
		case R.id.action_signout:
			signout();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
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

		listView = (ListView)findViewById(R.id.secondsListView);

		listView.setAdapter(adapter);
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
	
	private void showSeconds(String sprinkle) {
		// Get ArrayList of uids and convert it to an array of strings
		ArrayList<String> uidList = Kitchen.getUidsBySprinkle(this, sprinkle);
		String[] uidArray = new String[uidList.size()];
		uidArray = uidList.toArray(uidArray);
		final String[] finalUidArray = uidArray;		// don't look at this too closely...
		
		SecondsArrayAdapter adapter = new SecondsArrayAdapter(this,
				R.layout.listview_seconds_row, uidArray);
		ListView listView = (ListView)findViewById(R.id.secondsListView);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
				Second second = Kitchen.getSecondByUid(mContext, finalUidArray[pos]);
				if(selectorOn)
				{
					//SecondsCursorAdapter adapter = (SecondsCursorAdapter) adapterView.getAdapter();
					batter.addSecond(second);
				}
				else{
					// Play video
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
		Kitchen.saveCakeToLocalDb(this, cake);
		
		Intent intent = new Intent(this, NewCakeActivity.class);
		intent.putExtra("cake_uid", cake.getId());
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
	
    private void signout() {
    	TokenManager.forgetToken(this);
    	Log.v("signout", "forgetting token");
    }

}
