package com.smorgasbord.lfbrota.adapters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.smorgasbord.lfbrota.R;

public class MonthGridAdapter extends BaseAdapter implements OnClickListener
	{
		private static final String tag = "GridCellAdapter";
		private final Context _context;

		private final List<String> list;
		private static final int DAY_OFFSET = 1;
		private final String[] weekdays = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
		private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		private final int month, year;
		
		private int daysInMonth, prevMonthDays;
		private int currentDayOfMonth;
		private int currentWeekDay;
		private Button gridcell;
		private TextView num_events_per_day;
		
		private final HashMap eventsPerMonthMap;
		private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");

		// Days in Current Month
		public MonthGridAdapter(Context context, int textViewResourceId, int month, int year)
			{
				super();
				this._context = context;
				this.list = new ArrayList<String>();
				this.month = month;
				this.year = year;

				Log.d(tag, "==> Passed in Date FOR Month: " + month + " " + "Year: " + year);
				Calendar calendar = Calendar.getInstance();
				setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
				setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
				Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
				Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
				Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

				// Print Month
				printMonth(month, year);

				// Find Number of Events
				eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
			}
		private String getMonthAsString(int i)
			{
				return months[i];
			}

		private String getWeekDayAsString(int i)
			{
				return weekdays[i];
			}

		private int getNumberOfDaysOfMonth(int i)
			{
				return daysOfMonth[i];
			}

		public String getItem(int position)
			{
				return list.get(position);
			}

		@Override
		public int getCount()
			{
				return list.size();
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
				int leadSpaces = 0;
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

				Log.d(tag, "Week Day:" + currentWeekDay + " is " + getWeekDayAsString(currentWeekDay));
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
						list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-GREY" + "-" + getMonthAsString(prevMonth) + "-" + prevYear);
					}

				// Current Month Days
				for (int i = 1; i <= daysInMonth; i++)
					{
						Log.d(currentMonthName, String.valueOf(i) + " " + getMonthAsString(currentMonth) + " " + yy);
						if (i == getCurrentDayOfMonth())
							{
								list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
							}
						else
							{
								list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
							}
					}

				// Leading Month days
				for (int i = 0; i < list.size() % 7; i++)
					{
						Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
						list.add(String.valueOf(i + 1) + "-GREY" + "-" + getMonthAsString(nextMonth) + "-" + nextYear);
					}
			}

		/**
		 * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
		 * ALL entries from a SQLite database for that month. Iterate over the
		 * List of All entries, and get the dateCreated, which is converted into
		 * day.
		 * 
		 * @param year
		 * @param month
		 * @return
		 */
		private HashMap findNumberOfEventsPerMonth(int year, int month)
			{
				HashMap map = new HashMap<String, Integer>();
				// DateFormat dateFormatter2 = new DateFormat();
				//						
				// String day = dateFormatter2.format("dd", dateCreated).toString();
				//
				// if (map.containsKey(day))
				// {
				// Integer val = (Integer) map.get(day) + 1;
				// map.put(day, val);
				// }
				// else
				// {
				// map.put(day, 1);
				// }
				return map;
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

				// ACCOUNT FOR SPACING

				Log.d(tag, "Current Day: " + getCurrentDayOfMonth());
				String[] day_color = list.get(position).split("-");
				String theday = day_color[0];
				String themonth = day_color[2];
				String theyear = day_color[3];
				if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null))
					{
						if (eventsPerMonthMap.containsKey(theday))
							{
								num_events_per_day = (TextView) row.findViewById(R.id.num_events_per_day);
								Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
								num_events_per_day.setText(numEvents.toString());
							}
					}

				// Set the Day GridCell
				gridcell.setText(theday);
				gridcell.setTag(theday + "-" + themonth + "-" + theyear);
				Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-" + theyear);

				
				if (day_color[1].equals("GREY"))
					{
						gridcell.setTextColor(Color.LTGRAY);
						
					}
				if (day_color[1].equals("WHITE"))
					{
						gridcell.setTextColor(Color.WHITE);
					}
				if (day_color[1].equals("BLUE"))
					{
						//gridcell.setTextColor(_context.getResources().getColor(R.color.static_text_color));
						gridcell.setTextColor(Color.RED);
						gridcell.setBackgroundColor(Color.GREEN);
						gridcell.setBackgroundResource(0);
						//gridcell.getBackground().setColorFilter(Color.GREEN, Mode.LIGHTEN);
					}
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
