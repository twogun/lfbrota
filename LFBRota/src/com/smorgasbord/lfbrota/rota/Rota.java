package com.smorgasbord.lfbrota.rota;

import java.util.Date;
import java.util.List;

public interface Rota {

	public abstract String getRotaName();

	public abstract Date getStartDate();

	public abstract List<RotaEntry> getEntries();

	public abstract int getRotaDurationDays();

}