package com.diamondshearts.models;

import java.util.HashSet;
import java.util.Random;

/**
 * Cards have 1-3 actions and 0-2 effects
 * */
public class Card {
	//random num for events and actions
	private static Random gen = new Random();
	//owner of cards
	private transient Player owner;
	//cost according to action rank
	private Integer cost;

	//lists of actions and events initialized
	private HashSet<Action> actions = new HashSet<Action>();
	private HashSet<Event> events = new HashSet<Event>();
	
	/**
	 * Initialize a card
	 * @param owner
	 *            The owner who owns the cards 
	 * */
	public Card(Player owner) {
		this.owner = owner;
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

	/**
	 * Access the cost
	 * @return cost
	 * */
	public Integer getCost() {
		return cost;
	}

	/**
	 * Access a list of actions
	 * @return actions
	 * */
	public HashSet<Action> getActions() {
		return actions;
	}

	/**
	 * Modify the actions list
	 * @param actions
	 * 				 A list of actions.
	 * */
	public void setActions(HashSet<Action> actions) {
		this.actions = actions;
	}

	/**
	 * Access a list of events
	 * @return events
	 * */
	public HashSet<Event> getEvents() {
		return events;
	}

	/**
	 * Modify the list of events
	 * @param events
	 * 				A list of events
	 * */
	public void setEvents(HashSet<Event> events) {
		this.events = events;
	}

	/**
	 * Given a target, act according to actions and events
	 * @param target
	 * 				The target opponent
	 * */
	public void play(Player target) {
		target.setHeart(target.getHeart() - 1);
//		for (Action action : getActions())
//			action.play(target);		
//		for (Event event : getEvents())
//			event.play(target);		
	}

	/**
	 * Access the owner of cards
	 * @return owner
	 * */
	public Player getOwner() {
		return owner;
	}

	/**
	 * Change the owner of cards
	 * @param owner
	 * 			   The owner who owns the cards
	 * */
	public void setOwner(Player owner) {
		this.owner = owner;
	}
}