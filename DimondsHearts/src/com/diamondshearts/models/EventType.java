package com.diamondshearts.models;

/**
 * Various event types
 * */
public enum EventType {
	/** EventType properties */
	ExtraTurn("Extra turn", "", 5), ExtraCard("Extra card", "", 1), SkipTurn(
			"Skip Turn", "", -3), Shield("Shield", "", 3), Barter("Barter", "",
			2), Strength("Strength", "", 5), Fortitude("Fortitude", "", 5), Drain(
			"Drain", "", 6), Load("Load", "", -4), Stealth("Stealth", "", 5), Curse(
			"Curse", "", -5), Weaken("Weaken", "", -3), Guilty("Guilty", "", -5), Reaction(
			"Reaction", "", -4);

	/** Name of event */
	private String name;
	/** Event description */
	private String description;
	/** Utility value */
	private Integer value;

	/**
	 * EventType Constructor to Initialize a EventType
	 * */
	EventType(String name, String desc, Integer value) {
		this.name = name;
		this.description = desc;
		this.value = value;
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
	 * Access the description of event type
	 * 
	 * @return description
	 * */
	public String getDescription() {
		return description;
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