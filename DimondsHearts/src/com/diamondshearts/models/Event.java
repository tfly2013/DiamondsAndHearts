package com.diamondshearts.models;

import java.util.ArrayList;
import java.util.Random;

public class Event {
	private static Random gen = new Random();
	private String name;
	private String description;

	// define statistical distribution of types
	public static final EventType[] allTypes = EventType.class.getEnumConstants();

	public Event() {
		// select an event based on the given distribution
		ArrayList<EventType> selection = new ArrayList<EventType>();
		for (int i = 0; i < allTypes.length; i++) {
			for (int j = 0; j < allTypes[i].getFrequency(); j++)
				selection.add(allTypes[i]);
		}
		int choice = gen.nextInt(allTypes.length);
		setName(selection.get(choice).getName());
		setDescription(selection.get(choice).getDescription());
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