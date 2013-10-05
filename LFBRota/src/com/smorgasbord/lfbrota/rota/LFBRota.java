package com.smorgasbord.lfbrota.rota;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.graphics.Color;

public class LFBRota extends RotaImpl {

	public LFBRota() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy", Locale.getDefault());
		Date startDate;
		try {
			startDate = sdf.parse("08/07/2013");
			this.setRotaName("London FireBrigade");
			this.setStartDate(startDate);
			this.setRotaDurationDays(8);
			List<RotaEntry> entries = new ArrayList<RotaEntry>();
			entries.add(new RotaEntry("White Day 1", Color.WHITE, "White day, Green night"));
			entries.add(new RotaEntry("White Day 2", Color.WHITE, "White day, Green night"));
			entries.add(new RotaEntry("Red Day 1", Color.parseColor("#FF4444"), "Red day, White night"));
			entries.add(new RotaEntry("Red Day 2", Color.parseColor("#FF4444"), "Red day, White night"));
			entries.add(new RotaEntry("Blue Day 1", Color.parseColor("#33B5E5"), "Blue day, Red night"));  
			entries.add(new RotaEntry("Blue Day 2", Color.parseColor("#33B5E5"), "Blue day, Red night"));  
			entries.add(new RotaEntry("Green Day 1", Color.parseColor("#99CC00"), "Green day, Blue night"));
			entries.add(new RotaEntry("Green Day 2", Color.parseColor("#99CC00"), "Green day, Blue night"));
			this.setEntries(entries);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
