package com.smorgasbord.lfbrota.rota;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Days;

public class DayRenderer {
	
	private Rota rota;
	
	public DayRenderer(Rota rota) {
		this.rota = rota;
	}
	
	public int getDayColour(Date date) {
		Days d = Days.daysBetween(new DateTime(rota.getStartDate()), new DateTime(date));
		int days = d.getDays();
		int rotaDay = days % rota.getRotaDurationDays();
		RotaEntry rotaEntry = rota.getEntries().get(rotaDay);
		return rotaEntry.getColour();
	}
	
	public boolean isStartDay(Date date) {
		Days d = Days.daysBetween(new DateTime(rota.getStartDate()), new DateTime(date));
		int days = d.getDays();
		int rotaDay = days % rota.getRotaDurationDays();
		if(rotaDay == 0 ) {
			return true;
		}
		return false;
	}

}
