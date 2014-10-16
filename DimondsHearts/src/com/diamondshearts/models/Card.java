package com.diamondshearts.models;

import java.util.HashSet;
import java.util.Random;

public class Card {
	private static Random gen = new Random();
	private Player owner;
	private Integer cost;

	private HashSet<Action> actions = new HashSet<Action>();
	private HashSet<Event> events = new HashSet<Event>();
	
	public Card(Player owner) {
		// add new actions
		Integer actionsCount = gen.nextInt(3) + 1;
		while (actions.size() < actionsCount)
			actions.add(new Action());
		// add new events
		Integer eventsCount = gen.nextInt(3);
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

	public HashSet<Event> getEvents() {
		return events;
	}

	public void setEvents(HashSet<Event> events) {
		this.events = events;
	}

	public void play(Player target) {
		for (Action action : getActions())
			action.play(target);		
		for (Event event : getEvents())
			event.play(target);		
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}
}
