package com.smorgasbord.lfbrota.activities;

import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.smorgasbord.lfbrota.R;
import com.smorgasbord.lfbrota.adapters.MonthGridAdapter;

public class CalendarViewActivity extends Activity {

	private Calendar calendar;
	private MonthGridAdapter adapter;
	private GridView calendarView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_view_activity);
		calendar = Calendar.getInstance(Locale.getDefault());
		
		calendarView = (GridView) this.findViewById(R.id.calendar);
		
		
		adapter = new MonthGridAdapter(getApplicationContext(), R.id.calendar_day_gridcell, 
				calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
		adapter.notifyDataSetChanged();
		calendarView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()) {
			case R.id.action_settings: {
				startActivity(new Intent(this, SettingsActivity.class));
				return true;
				}
			case R.id.action_help: {
				startActivity(new Intent(this, HelpActivity.class));
				return true;	
			}
			case R.id.action_exit: {
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				return true;	
			}
			case R.id.action_altlayout: {
				startActivity(new Intent(this, CViewListGridActivity.class));
				return true;	
			}
		}
		
		return false;
	}
	
	
}
