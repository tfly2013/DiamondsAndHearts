package com.diamondshearts.models;

/**
 * Various event types
 * */
public enum EventType {
	/**EventType properties*/
	ExtraTurn("Extra turn", "", 5),
	ExtraCard("Extra card", "", 1),
	SkipTurn("Skip Turn", "", -3),
	Shield("Shield", "", 3),
	Barter("Barter", "", 2),
	Strength("Strength", "", 5),
	Fortitude("Fortitude", "", 5),
	Drain("Drain", "", 6), 
	Load("Load", "", -4),
	Stealth("Stealth", "", 5),
	Curse("Curse", "", -5),
	Weaken("Weaken", "", -3),
	Guilty("Guilty", "", -5),
	Reaction("Reaction", "", -4);
	
	/**Name of event*/
	private String name;
	/**Event description*/
	private String description;
	/**Utility value*/
	private Integer value;

	/**
	 * EventType Constructor to Initialize a EventType
	 * */
	EventType(String name, String desc, Integer value) {
		this.setName(name);
		this.setDescription(desc);
		this.setValue(value);
	}
	
	/**
	 * Access the name
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Modify the name of event type
	 * */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Access the description of event type
	 * @return description
	 * */
	public String getDescription() {
		return description;
	}

	/**
	 * Modify the description of event type
	 * @param description
	 * 				     Event type Description
	 * */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Access the value of an event
	 * @return value
	 * 				Event value
	 * */
	public Integer getValue() {
		return value;
	}

	/**
	 * Modify the value of an event
	 * @param value
	 * 			   The event value
	 * */
	public void setValue(Integer value) {
		this.value = value;
	}
	
}