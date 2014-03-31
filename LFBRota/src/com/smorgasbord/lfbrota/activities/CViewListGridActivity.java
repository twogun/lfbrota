package com.smorgasbord.lfbrota.activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
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
		
		new PopulateMonthGrid(this).execute(selectedCalendar);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cview_list_grid, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

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
	    
	    new PopulateMonthGrid(this).execute(selectedCalendar);
	}
	
	   public void gotoTodayClicked(View v){
	        
	        ImageButton todayButton = (ImageButton) this.findViewById(R.id.activityToday);
	        todayButton.setVisibility(View.INVISIBLE);
	        
	        Calendar calendar = Calendar.getInstance(Locale.getDefault());
	        selectedCalendar = calendar;
	        
	        new PopulateMonthGrid(this).execute(selectedCalendar);
	        
	        Log.d(LFBTAG, "goto Today");
	    }
		
	@Override
    protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
	    
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        final ComponentName cn = new ComponentName(this, LFBWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(cn);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.dayList);

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
	      
	      new PopulateMonthGrid(this).execute(selectedCalendar);
	    }
	    
	 }
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent e)
	{
	    return super.dispatchTouchEvent(e);
	}
	

	
	private class PopulateMonthGrid extends AsyncTask<Calendar, Integer, BaseAdapter> {
	    
	    private final Activity activityContext;
	    GridView calendarEntries;
	    private boolean showGotoToday = false;
	    
	    public PopulateMonthGrid(Activity activity) {
	        this.activityContext = activity;
	        calendarEntries = (GridView) activity.findViewById(R.id.calendarEntries);;
	    }
	    
	    @Override
        protected BaseAdapter doInBackground(Calendar... calendar) {
	        
	        Calendar now = Calendar.getInstance(Locale.getDefault());
	        if(calendar[0].get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
	                calendar[0].get(Calendar.MONTH) == now.get(Calendar.MONDAY)) {
	            showGotoToday = false;
	        } else {
	            showGotoToday = true;
	        }
	        
	        MonthView monthView = new MonthView(calendar[0]);
	        
	        BaseAdapter gridAdapter = new RotaGridDateTimeAdapter(activityContext, monthView.getDates());
	        gridAdapter.notifyDataSetChanged();
	       
	        return gridAdapter;
	     }

	     @Override
        protected void onProgressUpdate(Integer... progress) {
	         //setProgressPercent(progress[0]);
	     }

	     @Override
        protected void onPostExecute(BaseAdapter result) {
	         calendarEntries.setAdapter(result);
	         
	         TextView currentMonth = (TextView) activityContext.findViewById(R.id.currentMonth);
	         currentMonth.setText(DateFormat.format(dateTemplate, selectedCalendar.getTime()));
	         
	         ImageButton todayButton = (ImageButton) activityContext.findViewById(R.id.activityToday); 
	         if(showGotoToday) {
	             todayButton.setVisibility(View.VISIBLE);
	         }
	         else {
	             todayButton.setVisibility(View.INVISIBLE);
	         }
	         
	         Log.d(LFBTAG, "gridPopulated");
	     }
	 }

}
