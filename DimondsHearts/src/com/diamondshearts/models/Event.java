package com.diamondshearts.models;

import java.util.ArrayList;
import java.util.Random;

public class Event {
	private static Random gen = new Random();
	private EventType type;

	// define statistical distribution of types
	public static final EventType[] allTypes = EventType.class
			.getEnumConstants();

	public Event() {
		// select an event based on the given distribution
		ArrayList<EventType> selection = new ArrayList<EventType>();
		for (int i = 0; i < allTypes.length; i++) {
			for (int j = 0; j < allTypes[i].getFrequency(); j++)
				selection.add(allTypes[i]);
		}
		int choice = gen.nextInt(allTypes.length);
		type = selection.get(choice);
	}

	@Override
	public boolean equals(Object o) {
		if (o.getClass() == Event.class) {
			Event tar = (Event) o;
			return (tar.getName().equals(getName()));
		}
		return false;
	}

	public String getName() {
		return type.getName();
	}

	public String getDescription() {
		return type.getDescription();
	}

	public EventType getType() {
		return type;
	}

	public boolean needTarget() {
		// TODO Auto-generated method stub
		return false;
	}

	public void play(Player target) {
		// TODO Auto-generated method stub
		
	}
}