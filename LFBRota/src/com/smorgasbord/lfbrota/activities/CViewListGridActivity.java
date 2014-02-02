package com.smorgasbord.lfbrota.activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.smorgasbord.lfbrota.R;
import com.smorgasbord.lfbrota.adapters.DayLabelAdapter;
import com.smorgasbord.lfbrota.adapters.RotaGridDateTimeAdapter;
import com.smorgasbord.lfbrota.rota.MonthView;
import com.smorgasbord.lfbrota.widget.LFBWidgetProvider;

public class CViewListGridActivity extends Activity implements OnGesturePerformedListener{
    
    public static final String LFBTAG = "LFBApp";

    public static final int SETTING_REQUEST = 1;
	
	private Calendar selectedCalendar;
	private DayLabelAdapter dayLabelAdapter;
	private GestureLibrary mLibrary;
	
	private static final String dateTemplate = "MMMM yyyy";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monthview_grid);
		
		mLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
		   if (!mLibrary.load()) {
		     finish();
		   }
		 
		   GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gestures);
		   gestures.addOnGesturePerformedListener(this);
		
		
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		selectedCalendar = calendar;
		
		GridView dayName = (GridView) this.findViewById(R.id.dayNames);
		
		dayLabelAdapter = new DayLabelAdapter(this);
		dayLabelAdapter.notifyDataSetChanged();
		dayName.setAdapter(dayLabelAdapter);
		
		GridView calendarEntries = (GridView) this.findViewById(R.id.calendarEntries);
		MonthView monthView = new MonthView(selectedCalendar);
		
		BaseAdapter gridAdapter = new RotaGridDateTimeAdapter(this, monthView.getDates());
		gridAdapter.notifyDataSetChanged();
		calendarEntries.setAdapter(gridAdapter);		
		
		TextView currentMonth = (TextView) this.findViewById(R.id.currentMonth);
		currentMonth.setText(DateFormat.format(dateTemplate, selectedCalendar.getTime()));
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cview_list_grid, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()) {
			case R.id.action_settings: {
				startActivityForResult(new Intent(this, SettingsActivity.class), SETTING_REQUEST);
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
		}
		
		return false;
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
		List<DateTime> dates = new MonthView(selectedCalendar).getDates();
		BaseAdapter gridAdapter = new RotaGridDateTimeAdapter(this, dates);
		
		gridAdapter.notifyDataSetChanged();
		calendarEntries.setAdapter(gridAdapter);
		
		TextView currentMonth = (TextView) this.findViewById(R.id.currentMonth);
		currentMonth.setText(DateFormat.format(dateTemplate, selectedCalendar.getTime()));
		
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
	    
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        final ComponentName cn = new ComponentName(this, LFBWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(cn);
        //LFBWidgetProvider.
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.dayList);
        Log.d(LFBTAG, "Blagh");
	}

	@Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
	    ArrayList<Prediction> predictions = mLibrary.recognize(gesture);
	  
	    if (predictions.size() > 0 && predictions.get(0).score > 1.0) {
	      String result = predictions.get(0).name;
	  
	      if ("next".equalsIgnoreCase(result)) {
	          selectedCalendar.add(Calendar.MONTH, 1);
	      } else if ("previous".equalsIgnoreCase(result)) {
	          selectedCalendar.add(Calendar.MONTH, -1);
	      }
	      
	      refreshRotaGrid();
	    }
	    
	 }
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent e)
	{
	    return super.dispatchTouchEvent(e);
	}

}
