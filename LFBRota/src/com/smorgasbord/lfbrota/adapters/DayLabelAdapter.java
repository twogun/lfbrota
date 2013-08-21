package com.smorgasbord.lfbrota.adapters;

import java.text.DateFormatSymbols;
import java.util.Locale;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DayLabelAdapter extends BaseAdapter {

	
	private Context context;
	private final String[] days = new String[7];
	
	
	public DayLabelAdapter(Context context) {
		this.context = context;
		DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(Locale.getDefault());
		String[] daysOfWeek = dateFormatSymbols.getShortWeekdays();
		System.arraycopy(daysOfWeek, 1, days, 0, 7);
	}
	
	@Override
	public int getCount() {
		return days.length;
	}

	@Override
	public Object getItem(int arg0) {
		return days[arg0];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View row = convertView;
		if (row == null)
			{
				//LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				//row = inflater.inflate(R.layout.calendar_day_gridcell, parent, false);
				TextView dayText = new TextView(context);
				dayText.setText(days[position]);
				dayText.setGravity(Gravity.RIGHT);
				row = dayText;
			}
		return row;
	}

}
