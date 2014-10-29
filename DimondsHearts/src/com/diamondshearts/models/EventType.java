package com.diamondshearts.models;

import com.diamondshearts.R;

/**
 * Various event types
 * */
public enum EventType {
	/** EventType properties */
	ExtraTurn("Extra turn", R.drawable.event_extra_turn, 5), 
	ExtraCard("Extra card", R.drawable.event_extra_card, 1), 
	SkipTurn("Skip Turn", R.drawable.event_skip_turn, -3), 
	Shield("Shield", R.drawable.event_shield, 3), 
	Barter("Barter", R.drawable.event_barter, 2), 
	Strength("Strength", R.drawable.event_strength, 5), 
	Fortitude("Fortitude", R.drawable.event_fortitude, 5), 
	Drain("Drain", R.drawable.event_drain, 6), 
	Load("Load", R.drawable.event_load, -4), 
	Stealth("Stealth", R.drawable.event_stealth, 5), 
	Curse("Curse", R.drawable.event_curse, -5), 
	Weaken("Weaken", R.drawable.event_weaken, -3), 
	Guilty("Guilty", R.drawable.event_guilty, -5), 
	Reaction("Reaction", R.drawable.event_reaction, -4);

	/** Name of event */
	private String name;
	/** Event icon */
	private int icon;
	/** Utility value */
	private Integer value;

	/**
	 * EventType Constructor to Initialize a EventType
	 * */
	EventType(String name, int icon, Integer value) {
		this.name = name;
		this.icon = icon;
		this.value = value;
	}

	/**
	 * @return the icon
	 */
	public int getIcon() {
		return icon;
	}

	/**
	 * Access the name
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Access the value of an event
	 * 
	 * @return value Event value
	 * */
	public Integer getValue() {
		return value;
	}
}