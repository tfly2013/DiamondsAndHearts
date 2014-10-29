package com.diamondshearts.models;

import java.util.Random;

/**
 * Events vary greatly, and add a significant amount of strategy to the game,
 * such as turn modifiers and action modifiers
 * */
public class Event implements Comparable<Event> {
	/** Random number */
	private static Random gen = new Random();
	/** Event type */
	private EventType type;

	/** Define statistical distribution of types */
	private static final EventType[] allTypes = EventType.class
			.getEnumConstants();

	/**
	 * Initialize an event
	 * */
	public Event() {
		int choice = gen.nextInt(allTypes.length);
		type = allTypes[choice];
	}

	@Override
	/** Compare two cards */
	public int compareTo(Event another) {
		return -((Double)getValue()).compareTo(another.getValue());
	}

	@Override
	/**
	 * Check if card has same event
	 * */
	public boolean equals(Object o) {
		if (o.getClass() == Event.class) {
			Event tar = (Event) o;
			return (tar.getType()).equals(getType());
		}
		return false;
	}

	/**
	 * @return the icon
	 */
	public int getIcon() {
		return type.getIcon();
	}

	/**
	 * Access the name of event type
	 * 
	 * @return name
	 * */
	public String getName() {
		return type.getName();
	}

	/**
	 * Access the type of a event
	 * 
	 * @return type
	 * */
	public EventType getType() {
		return type;
	}

	/** Evaluate the value of a type */
	public double getValue() {
		return type.getValue();
	}

	/**
	 * Play against target opponent according to event
	 * 
	 * @param target
	 *            The targeted opponent
	 * */
	public void play(Player owner, Player target, Card card) {
		// actions does not immediately happen will be recorded
		owner.getEffects().put(type, true);

	}
}
