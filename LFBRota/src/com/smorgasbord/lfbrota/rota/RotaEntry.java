package com.smorgasbord.lfbrota.rota;

import android.graphics.Color;

public class RotaEntry {

	private int colour;
	private String name;
	private int duration;
	private String description;

	public RotaEntry(String name, int colour) {
		this.name = name;
		this.colour = colour;
	}

	public RotaEntry(String name, int colour, String desc) {
		this.name = name;
		this.colour = colour;
		this.description = desc;
	}

	public void setColour(int colour) {
		this.colour = colour;
	}

	public int getColour() {
		return colour;
	}

	public void setColour(String colourHexCode) {
		colour = Color.parseColor(colourHexCode);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
