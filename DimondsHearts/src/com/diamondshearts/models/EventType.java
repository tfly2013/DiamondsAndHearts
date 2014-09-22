package com.diamondshearts.models;

public enum EventType {
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
			
	private String name;
	private String description;
	private Integer frequency;	

	EventType(String name, String desc, Integer freq) {
		this.setName(name);
		this.setFrequency(freq);
		this.setDescription(desc);
	}
	
	public void ApplyEvent() {
		//TODO
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

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}
	
}
