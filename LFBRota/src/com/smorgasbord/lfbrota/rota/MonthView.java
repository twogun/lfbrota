package com.smorgasbord.lfbrota.rota;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.joda.time.DateTime;

import android.util.Log;

public class MonthView {

	private static final String tag = "MonthView";
	
	private final Calendar calendar;
	private final DateTime selectedTime;
	
	public MonthView(Calendar calendar) {
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		selectedTime = new DateTime(calendar);
		this.calendar = calendar;
	}
	
	public List<DateTime> getDates() {
		
		List<DateTime> dates = new ArrayList<DateTime>();
		
		// The number of days to leave blank at the start of this month.
		int trailingSpaces = 0;
		DateTime nextMonth = null;

		int currentMonth = calendar.get(Calendar.MONTH);
		int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		Log.d(tag, "Current Month: " + " " + currentMonth + " having " + daysInMonth + " days.");

		nextMonth = selectedTime.plusMonths(1);
		
		// Compute how much to leave before before the first day of the month.
		// getDayOfWeek() returns 7 for Sunday.
		int firstofMonthWeekDay = selectedTime.getDayOfWeek();
		trailingSpaces = firstofMonthWeekDay;

		Log.d(tag, "Week Day:" + firstofMonthWeekDay + " is " + selectedTime);
		Log.d(tag, "No. preceding days to fill in: " + trailingSpaces);

		// Preceding Month days
		DateTime currentDay = selectedTime;
		if(trailingSpaces != 7) {
		    for (int i = trailingSpaces; i > 0; i--) {
		        currentDay = currentDay.minusDays(1);
		        dates.add(0, currentDay);
	        }
		}
		
		// Current Month Days
		for (int i = 1; i <= daysInMonth; i++) {
			dates.add(selectedTime.dayOfMonth().setCopy(i));				
		}

		// Following Month days
		for (int i = 0; i < dates.size() % 7; i++) {
			dates.add(nextMonth.dayOfMonth().setCopy(i + 1));
		}
			
		return dates;
	}
}
