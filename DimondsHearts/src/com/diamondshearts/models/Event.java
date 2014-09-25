package com.diamondshearts.models;

import java.util.ArrayList;
import java.util.Random;

/**
 * Events vary greatly, and add a significant amount of strategy to the game,
 * such as turn modifiers and action modifiers
 * */
public class Event {
	//random number
	private static Random gen = new Random();
	//event type
	private EventType type;

	// define statistical distribution of types
	public static final EventType[] allTypes = EventType.class
			.getEnumConstants();

	/**
	 * Initialize an event
	 * */
	public Event() {
		// select an event based on the given distribution
		ArrayList<EventType> selection = new ArrayList<EventType>();
		for (int i = 0; i < allTypes.length; i++) {
			for (int j = 0; j < allTypes[i].getFrequency(); j++)
				selection.add(allTypes[i]);
		}
		int choice = gen.nextInt(selection.size());
		type = selection.get(choice);
	}

	@Override
	/**
	 * Check if card has same event
	 * */
	public boolean equals(Object o) {
		if (o.getClass() == Event.class) {
			Event tar = (Event) o;
			return (tar.getName().equals(getName()));
		}
		return false;
	}

	/**
	 * Access the name of event type
	 * @return name 
	 * */
	public String getName() {
		return type.getName();
	}

	/**
	 * Access the description of event type
	 * @return description
	 * */
	public String getDescription() {
		return type.getDescription();
	}

	/**
	 * Access the type of a event
	 * @return type
	 * */
	public EventType getType() {
		return type;
	}

	/**
	 * Play against target opponent according to event
	 * @param target
	 * 				The targeted opponent
	 * */
	public void play(Player target) {
		// TODO Auto-generated method stub
		
	}
}