package com.smorgasbord.lfbrota.activities;

import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
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

}
