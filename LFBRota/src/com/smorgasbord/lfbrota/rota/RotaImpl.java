package com.smorgasbord.lfbrota.rota;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RotaImpl implements Rota {
	
	private String rotaName;
	private Date startDate;
	private List<RotaEntry> entries = new ArrayList<RotaEntry>();
	
	/**
	 * The total number of days for the rota to repeat itself. For example in the
	 * LFB the rota is 4 days on / 4 days off so total rota duration is 8 days.
	 */
	int rotaDurationDays;
	
	/* (non-Javadoc)
	 * @see com.smorgasbord.lfbrota.rota.Rota#getRotaName()
	 */
	@Override
	public String getRotaName() {
		return rotaName;
	}
	public void setRotaName(String rotaName) {
		this.rotaName = rotaName;
	}
	/* (non-Javadoc)
	 * @see com.smorgasbord.lfbrota.rota.Rota#getStartDate()
	 */
	@Override
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/* (non-Javadoc)
	 * @see com.smorgasbord.lfbrota.rota.Rota#getEntries()
	 */
	@Override
	public List<RotaEntry> getEntries() {
		return entries;
	}
	public void setEntries(List<RotaEntry> entries) {
		this.entries = entries;
	}
	/* (non-Javadoc)
	 * @see com.smorgasbord.lfbrota.rota.Rota#getRotaDurationDays()
	 */
	@Override
	public int getRotaDurationDays() {
		return rotaDurationDays;
	}
	public void setRotaDurationDays(int rotaDurationDays) {
		this.rotaDurationDays = rotaDurationDays;
	}
}
