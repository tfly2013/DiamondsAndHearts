package com.diamondshearts.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Card {
	private static Random gen = new Random();
	private Integer cost;

	private List<Action> actions = new ArrayList<Action>();
	private List<Event> events = new ArrayList<Event>();
	
	public Card() {
		// add new actions/events
		for (int i=0; i<gen.nextInt(3) + 1; i++)
			getActions().add(new Action());
		for (int i=0; i<gen.nextInt(3) + 1; i++)
			getEvents().add(new Event());
		
		cost = 0;
		for (Action action : getActions()) {
			if (action.getSuit() == Suit.Diamond)
				cost = action.getRank();
		}
	}

	public Integer getCost() {
		return cost;
	}

	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}
}
