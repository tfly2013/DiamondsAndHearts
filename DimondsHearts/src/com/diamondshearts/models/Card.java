package com.diamondshearts.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

/**
 * Cards have 1-3 actions and 0-2 effects
 * */
public class Card {
	/** Random num for events and actions */
	private static Random gen = new Random();
	/** Owner of cards */
	private Player owner;

	/** Lists of actions and events initialized */
	private HashSet<Action> actions = new HashSet<Action>();
	private HashSet<Event> events = new HashSet<Event>();

	public Card() {
	};

	/**
	 * Initialize a card
	 * 
	 * @param owner
	 *            The owner who owns the cards
	 * */
	public Card(Player owner) {
		this.owner = owner;

		ArrayList<Suit> suits = new ArrayList<Suit>(Arrays.asList(Suit.class
				.getEnumConstants()));
		
		// add new actions
		Integer actionsCount = gen.nextInt(3) + 1;
		while (actions.size() < actionsCount) {
			Action newAction = new Action(suits);			
			actions.add(newAction);
			suits.remove(newAction.getSuit());
		}
		// add new events
		Integer eventsCount = gen.nextInt(3);
		while (events.size() < eventsCount)
			events.add(new Event());

	}

	/**
	 * Access the cost
	 * 
	 * @return cost
	 * */
	public Integer getCost() {
		for (Action action : getActions()) {
			if (action.getSuit() == Suit.Diamond)
				return action.getRank();
		}
		return 0;
	}

	/**
	 * Access a list of actions
	 * 
	 * @return actions
	 * */
	public HashSet<Action> getActions() {
		return actions;
	}

	/**
	 * Modify the actions list
	 * 
	 * @param actions
	 *            A list of actions.
	 * */
	public void setActions(HashSet<Action> actions) {
		this.actions = actions;
	}

	/**
	 * Access a list of events
	 * 
	 * @return events
	 * */
	public HashSet<Event> getEvents() {
		return events;
	}

	/**
	 * Modify the list of events
	 * 
	 * @param events
	 *            A list of events
	 * */
	public void setEvents(HashSet<Event> events) {
		this.events = events;
	}

	/**
	 * Given a target, act according to actions and events
	 * 
	 * @param target
	 *            The target opponent
	 * */
	public boolean play(Player target) {
		Integer cost = getCost();
		if (owner.canAfford(cost)) {
			if (needTarget() == target.equals(owner))
				return false;
			owner.setDiamond(owner.getDiamond() - cost);
			for (Action action : getActions())
				action.play(owner, target);
			for (Event event : getEvents())
				event.play(owner, target);
			return true;
		}
		return false;
	}
	
	public boolean needTarget(){
		for (Action action : getActions()) {
			if (action.getSuit() == Suit.Spade)
				return true;
			if (action.getSuit() == Suit.Club)
				return true;
		}
		return false;
	}

	/**
	 * Access the owner of cards
	 * 
	 * @return owner
	 * */
	public Player getOwner() {
		return owner;
	}

	/**
	 * Change the owner of cards
	 * 
	 * @param owner
	 *            The owner who owns the cards
	 * */
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public static Card draw() {
		return new Card();		
	}
}
