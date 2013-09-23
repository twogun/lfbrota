package com.smorgasbord.lfbrota.activities;

import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.smorgasbord.lfbrota.R;
import com.smorgasbord.lfbrota.adapters.DayLabelAdapter;
import com.smorgasbord.lfbrota.adapters.RotaGridAdapter;
import com.smorgasbord.lfbrota.adapters.RotaGridDateTimeAdapter;
import com.smorgasbord.lfbrota.rota.MonthView;

public class CViewListGridActivity extends Activity {

	
	private Calendar selectedCalendar;
	private DayLabelAdapter dayLabelAdapter;
	
	private static final String dateTemplate = "MMMM yyyy";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monthview_grid);
		
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		selectedCalendar = calendar;
		
		GridView dayName = (GridView) this.findViewById(R.id.dayNames);
		
		dayLabelAdapter = new DayLabelAdapter(this);
		dayLabelAdapter.notifyDataSetChanged();
		dayName.setAdapter(dayLabelAdapter);
		
		GridView calendarEntries = (GridView) this.findViewById(R.id.calendarEntries);
		MonthView monthView = new MonthView(selectedCalendar);
		
		BaseAdapter gridAdapter = new RotaGridDateTimeAdapter(this, R.id.calendar_day_gridcell, monthView.getDates());
		gridAdapter.notifyDataSetChanged();
		calendarEntries.setAdapter(gridAdapter);		
		
		Button currentMonth = (Button) this.findViewById(R.id.currentMonth);
		currentMonth.setText(DateFormat.format(dateTemplate, selectedCalendar.getTime()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cview_list_grid, menu);
		return true;
	}
	
	public void changeMonth(View v) {
	    Log.d("CViewListGridActivity", "ChangeMonth clicked " + v.getId());
	    switch(v.getId()) {
	    	case R.id.prevMonth: {
	    		selectedCalendar.add(Calendar.MONTH, -1);
	    		break;
	    	}
	    	case R.id.nextMonth: {
	    		selectedCalendar.add(Calendar.MONTH, 1);
	    		break;
	    	}
	    }
	    
	    refreshRotaGrid();
	}
	
	private void refreshRotaGrid()
	{
		GridView calendarEntries = (GridView) this.findViewById(R.id.calendarEntries);
		BaseAdapter gridAdapter = new RotaGridAdapter(this, R.id.calendar_day_gridcell, 
				selectedCalendar.get(Calendar.MONTH) + 1, selectedCalendar.get(Calendar.YEAR));
		gridAdapter.notifyDataSetChanged();
		calendarEntries.setAdapter(gridAdapter);
		
		Button currentMonth = (Button) this.findViewById(R.id.currentMonth);
		currentMonth.setText(DateFormat.format(dateTemplate, selectedCalendar.getTime()));
		
	}

}
