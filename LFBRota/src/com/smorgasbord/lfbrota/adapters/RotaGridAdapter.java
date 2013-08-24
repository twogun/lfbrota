package com.smorgasbord.lfbrota.adapters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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

public class RotaGridAdapter extends BaseAdapter implements OnClickListener
	{
		private static final String tag = "GridCellAdapter";
		private final Context _context;

		private DayRenderer dayRenderer;
		
		private final List<Date> dates;
		private static final int DAY_OFFSET = 1;
		private final String[] weekdays = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
		private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		private final int month, year;
		
		private int daysInMonth, prevMonthDays;
		private int currentDayOfMonth;
		private int currentWeekDay;
		private Button gridcell;
		
		private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");

		// Days in Current Month
		public RotaGridAdapter(Context context, int textViewResourceId, int month, int year)
			{
				super();
				this._context = context;
				this.dates = new ArrayList<Date>();
				this.month = month;
				this.year = year;
				this.dayRenderer = new DayRenderer(new LFBRota());

				Log.d(tag, "==> Passed in Date FOR Month: " + month + " " + "Year: " + year);
				Calendar calendar = Calendar.getInstance();
				setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
				setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
				Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
				Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
				Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

				// Print Month
				printMonth(month, year);

			}
		private String getMonthAsString(int i)
			{
				return months[i];
			}

		private int getNumberOfDaysOfMonth(int i)
			{
				return daysOfMonth[i];
			}

		public Date getItem(int position)
			{
				return dates.get(position);
			}

		@Override
		public int getCount()
			{
				return dates.size();
			}

		/**
		 * Prints Month
		 * 
		 * @param mm
		 * @param yy
		 */
		private void printMonth(int mm, int yy)
			{
				Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
				// The number of days to leave blank at
				// the start of this month.
				int trailingSpaces = 0;
				int daysInPrevMonth = 0;
				int prevMonth = 0;
				int prevYear = 0;
				int nextMonth = 0;
				int nextYear = 0;

				int currentMonth = mm - 1;
				String currentMonthName = getMonthAsString(currentMonth);
				daysInMonth = getNumberOfDaysOfMonth(currentMonth);

				Log.d(tag, "Current Month: " + " " + currentMonthName + " having " + daysInMonth + " days.");

				// Gregorian Calendar : MINUS 1, set to FIRST OF MONTH
				GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
				Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

				if (currentMonth == 11)
					{
						prevMonth = currentMonth - 1;
						daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
						nextMonth = 0;
						prevYear = yy;
						nextYear = yy + 1;
						Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
					}
				else if (currentMonth == 0)
					{
						prevMonth = 11;
						prevYear = yy - 1;
						nextYear = yy;
						daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
						nextMonth = 1;
						Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
					}
				else
					{
						prevMonth = currentMonth - 1;
						nextMonth = currentMonth + 1;
						nextYear = yy;
						prevYear = yy;
						daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
						Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
					}

				// Compute how much to leave before before the first day of the
				// month.
				// getDay() returns 0 for Sunday.
				int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
				trailingSpaces = currentWeekDay;

				Log.d(tag, "Week Day:" + currentWeekDay + " is " + currentWeekDay);
				Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
				Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

				if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 1)
					{
						++daysInMonth;
					}

				// Trailing Month days
				for (int i = 0; i < trailingSpaces; i++)
					{
						Log.d(tag, "PREV MONTH:= " + prevMonth + " => " + getMonthAsString(prevMonth) + " " + String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i));
						dates.add(new Date(this.year, prevMonth, (daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i));
					}

				// Current Month Days
				for (int i = 1; i <= daysInMonth; i++)
					{
						Log.d(currentMonthName, String.valueOf(i) + " " + getMonthAsString(currentMonth) + " " + yy);
						
						dates.add(new Date(this.year, currentMonth, i));
						
					}

				// Leading Month days
				for (int i = 0; i < dates.size() % 7; i++)
					{
						Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
						dates.add(new Date(this.year, nextMonth,  i + 1));
					}
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
				Date date = dates.get(position);
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
