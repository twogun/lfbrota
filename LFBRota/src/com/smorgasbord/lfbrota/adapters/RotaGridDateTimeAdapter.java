package com.smorgasbord.lfbrota.adapters;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.smorgasbord.lfbrota.R;
import com.smorgasbord.lfbrota.rota.DayRenderer;
import com.smorgasbord.lfbrota.rota.LFBRota;

/**
 * Grid adapter designed to work with Joda DateTime objects.
 * 
 * @author steph
 * 
 */
public class RotaGridDateTimeAdapter extends BaseAdapter implements
		OnClickListener {
	private static final String tag = "GridCellAdapter";
	private final Context _context;

	private final DayRenderer dayRenderer;

	private final List<DateTime> dates;
	private final DateTime today;
	private final DateTimeComparator dateOnlyInstance;

	private int currentDayOfMonth;
	private int currentWeekDay;
	private Button gridcell;

	// private final SimpleDateFormat dateFormatter = new
	// SimpleDateFormat("dd-MMM-yyyy");

	// Days in Current Month
	public RotaGridDateTimeAdapter(Context context, List<DateTime> dates) {
		super();
		this._context = context;
		this.dates = dates;
		this.dayRenderer = new DayRenderer(new LFBRota());

		today = new DateTime();
		dateOnlyInstance = DateTimeComparator.getDateOnlyInstance();
		
		Calendar calendar = Calendar.getInstance();
		setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
		setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
		Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
		Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
		Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

	}

	@Override
	public DateTime getItem(int position) {
		return dates.get(position);
	}

	@Override
	public int getCount() {
		return dates.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		
		DateTime dateTime = dates.get(position);
        boolean isToday = false;
        if(dateOnlyInstance.compare(today, dateTime) == 0) {
            isToday = true;
        };
		
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if(isToday) {
			    row = inflater.inflate(R.layout.calander_today_gridcell, parent, false);
			} else {
			    row = inflater.inflate(R.layout.calendar_day_gridcell, parent, false);
			}
			
		}
		
		// Get a reference to the Day gridcell
		gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
		gridcell.setOnClickListener(this);

		
		Date date = dates.get(position).toDate();
		
		gridcell.setText(dateTime.getDayOfMonth() + "");
		gridcell.setTextColor(Color.BLACK);
		gridcell.setBackgroundColor(dayRenderer.getDayColour(date));
		gridcell.setTag(date);

		return row;
	}

	@Override
	public void onClick(View view) {

		TextView dayDesc = (TextView) ((Activity) _context)
				.findViewById(R.id.dayDesc);

		Date date_month_year = (Date) view.getTag();
		String dayDescription = dayRenderer.getDayDescription(date_month_year);
		dayDesc.setText(dayDescription);
		dayDesc.invalidate();

	}

	public int getCurrentDayOfMonth() {
		return currentDayOfMonth;
	}

	private void setCurrentDayOfMonth(int currentDayOfMonth) {
		this.currentDayOfMonth = currentDayOfMonth;
	}

	public void setCurrentWeekDay(int currentWeekDay) {
		this.currentWeekDay = currentWeekDay;
	}

	public int getCurrentWeekDay() {
		return currentWeekDay;
	}
}
