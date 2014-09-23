package com.diamondshearts.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Card {
	private static Random gen = new Random();
	private Integer cost;

	private HashSet<Action> actions = new HashSet<Action>();
	private ArrayList<Event> events = new ArrayList<Event>();
	
	public Card() {
		// add new actions
		Integer actionsCount = gen.nextInt(3) + 1;
		while (actions.size() < actionsCount)
			actions.add(new Action());
		// add new events
		Integer eventsCount = gen.nextInt(3) + 1;
		while (events.size() < eventsCount)
			events.add(new Event());
		
		cost = 0;
		for (Action action : getActions()) {
			if (action.getSuit() == Suit.Diamond)
				cost = action.getRank();
		}
	}

	public Integer getCost() {
		return cost;
	}

	public HashSet<Action> getActions() {
		return actions;
	}

	public void setActions(HashSet<Action> actions) {
		this.actions = actions;
	}

	public ArrayList<Event> getEvents() {
		return events;
	}

	public void setEvents(ArrayList<Event> events) {
		this.events = events;
	}
}
