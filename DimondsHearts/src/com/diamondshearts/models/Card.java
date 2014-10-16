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

	/**
	 * Initialize a card
	 * 
	 * @param owner
	 *            The owner who owns the cards
	 * */
	private Card(Player owner) {
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
	 * Given a target, act according to actions and events
	 * 
	 * @param target
	 *            The target opponent
	 * */
	public boolean play(Player target) {
		Integer cost = getCost();
		//Barter: the players next diamond cost is halved
		if(owner.getEventsActivated().get(EventType.Barter)){
			cost /= 2;
			owner.getEventsActivated().put(EventType.Barter, false);
		}
		//Load: the players next diamond cost is doubled
		if(owner.getEventsActivated().get(EventType.Load)){
			cost *= 2;
			owner.getEventsActivated().put(EventType.Load, false);
		}
		if (owner.canAfford(cost)) {
			if (needTarget() == target.equals(owner))
				return false;
			owner.setDiamond(owner.getDiamond() - cost);
			for (Action action : getActions())
				action.play(owner, target);
			for (Event event : getEvents())
				event.play(owner, target, this);
			return true;
		}
		return false;
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
	 * Check if the play need to target
	 * @return true/false
	 * */
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

	/**
	 * Draw a new card
	 * @return card
	 * 				A new card
	 * */
	public static Card draw(Player player) {
		Card card = new Card(player);
		while (!evaluate(card)) {
			card = new Card(player);
		}		
		return card;
	}
	
	public static boolean evaluate(Card card){
		double value = 0;
		for (Action action : card.getActions()) {
			value += action.getValue();
		}
		for (Event event : card.getEvents()) {
			value += event.getValue();
		}
		if (value >= -1 && value < 2)
			return true;
		return false;
	}
}
