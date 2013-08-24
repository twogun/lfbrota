package com.smorgasbord.lfbrota.rota;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeField;

import android.util.Log;

public class MonthView {

	private static final String tag = "MonthView";
	
	private Calendar calendar;
	private DateTime selectedTime;
	
	public MonthView(Calendar calendar) {
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		selectedTime = new DateTime(calendar);
		this.calendar = calendar;
	}
	
	public List<DateTime> getDates() {
		
		List<DateTime> dates = new ArrayList<DateTime>();
		
		// The number of days to leave blank at
		// the start of this month.
		int trailingSpaces = 0;
		int daysInPrevMonth = 0;
		DateTime prevMonth = null;
		DateTime nextMonth = null;

		int currentMonth = calendar.get(Calendar.MONTH);
		int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		Log.d(tag, "Current Month: " + " " + currentMonth + " having " + daysInMonth + " days.");

		prevMonth = selectedTime.minusMonths(1);
		daysInPrevMonth = prevMonth.dayOfMonth().getMaximumValue();
		nextMonth = selectedTime.plusMonths(1);
		
		
		// Compute how much to leave before before the first day of the
		// month.
		// getDay() returns 0 for Sunday.
		int firstofMonthWeekDay = selectedTime.getDayOfWeek();
		trailingSpaces = firstofMonthWeekDay;

		Log.d(tag, "Week Day:" + firstofMonthWeekDay + " is " + selectedTime);
		Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
		Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

		// Trailing Month days
		for (int i = 0; i < trailingSpaces; i++) {
			dates.add(prevMonth.dayOfMonth().setCopy(daysInPrevMonth - trailingSpaces + i ));
		}

		// Current Month Days
		for (int i = 1; i <= daysInMonth; i++) {
			dates.add(selectedTime.dayOfMonth().setCopy(i));				
		}

		// Leading Month days
		for (int i = 0; i < dates.size() % 7; i++) {
			dates.add(nextMonth.dayOfMonth().setCopy(i + 1));
			}
		
		
		return dates;
	}
}
