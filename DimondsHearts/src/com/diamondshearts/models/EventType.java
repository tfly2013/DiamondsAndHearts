package com.diamondshearts.models;

/**
 * Various event types
 * */
public enum EventType {
	/**EventType properties*/
	ExtraTurn("Extra turn", "", 4),
	ExtraCard("Extra card", "", 4),
	SkipTurn("Skip Turn", "", 4), 
	Reverse("Reverse", "", 4),
	Shield("Shield", "", 4), 
	Barter("Barter", "", 2),
	Strength("Strength", "", 2),
	Fortitude("Fortitude", "", 2), 
	Absorb("Absorb", "", 1),
	Drain("Drain", "", 1), 
	Revive("Revive", "", 1);
	
	/**Name of event*/
	private String name;
	/**Event description*/
	private String description;
	/**Event frequency*/
	private Integer frequency;	

	/**
	 * EventType Constructor to Initialize a EventType
	 * */
	EventType(String name, String desc, Integer freq) {
		this.setName(name);
		this.setFrequency(freq);
		this.setDescription(desc);
	}
	
	/**
	 * Apply the effect of event
	 * */
	public void ApplyEvent() {
		//TODO
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
	 * Access the frequency of the event type
	 * @return frequency
	 * */
	public Integer getFrequency() {
		return frequency;
	}

	/**
	 * Modify the frequency of event type
	 * @param frequency
	 * 				   The frequency of event type
	 * */
	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}
	
}