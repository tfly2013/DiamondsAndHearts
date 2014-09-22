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
			
	String name, description;
	int frequency;	

	EventType(String name, String desc, Integer freq) {
		this.name = name;
		this.frequency = freq;
		this.description = desc;
	}
	
	public void ApplyEvent() {
		//TODO
	}
	
}
