package com.smorgasbord.lfbrota.adapters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.smorgasbord.lfbrota.R;
import com.smorgasbord.lfbrota.rota.DayRenderer;
import com.smorgasbord.lfbrota.rota.LFBRota;

/**
 * Grid adapter designed to work with Joda DateTime objects.
 * @author steph
 *
 */
public class RotaGridDateTimeAdapter extends BaseAdapter implements OnClickListener
	{
		private static final String tag = "GridCellAdapter";
		private final Context _context;

		private DayRenderer dayRenderer;
		
		private final List<DateTime> dates;
		
		private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

		private int currentDayOfMonth;
		private int currentWeekDay;
		private Button gridcell;
		
		private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");

		// Days in Current Month
		public RotaGridDateTimeAdapter(Context context, int textViewResourceId, List<DateTime> dates)
			{
				super();
				this._context = context;
				this.dates = dates;
				this.dayRenderer = new DayRenderer(new LFBRota());

						Calendar calendar = Calendar.getInstance();
				setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
				setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
				Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
				Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
				Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

			}

		public DateTime getItem(int position)
			{
				return dates.get(position);
			}

		@Override
		public int getCount()
			{
				return dates.size();
			}


		@Override
		public long getItemId(int position)
			{
				return position;
			}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
			{
				View row = convertView;
				if (row == null)
				{
					LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					row = inflater.inflate(R.layout.calendar_day_gridcell, parent, false);
				}

				// Get a reference to the Day gridcell
				gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
				gridcell.setOnClickListener(this);
				
				// Set the Day GridCell
				Calendar cal = Calendar.getInstance();
				Date date = dates.get(position).toDate();
			    cal.setTime(date);

				gridcell.setText(cal.get(Calendar.DAY_OF_MONTH) + "");
				gridcell.setTextColor(Color.BLACK);
				gridcell.setBackgroundColor(dayRenderer.getDayColour(date));
						
				return row;
			}
		@Override
		public void onClick(View view)
			{
				String date_month_year = (String) view.getTag();
				//selectedDayMonthYearButton.setText("Selected: " + date_month_year);

				try
					{
						Date parsedDate = dateFormatter.parse(date_month_year);
						Log.d(tag, "Parsed Date: " + parsedDate.toString());

					}
				catch (ParseException e)
					{
						e.printStackTrace();
					}
			}

		public int getCurrentDayOfMonth()
			{
				return currentDayOfMonth;
			}

		private void setCurrentDayOfMonth(int currentDayOfMonth)
			{
				this.currentDayOfMonth = currentDayOfMonth;
			}
		public void setCurrentWeekDay(int currentWeekDay)
			{
				this.currentWeekDay = currentWeekDay;
			}
		public int getCurrentWeekDay()
			{
				return currentWeekDay;
			}
	}
