package com.diamondshearts.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.TreeSet;

/**
 * Cards have 1-3 actions and 0-2 effects
 * */
public class Card {
	/** All play results */
	public enum PlayResult{
		OK,
		CantAfford,
		NeedTarget,
		SelfTarget
	}
	/** Random num for events and actions */
	private static Random gen = new Random();

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
	/** Systematic evaluation approach
	 *  that balances the power of a new card
	 *  @param card
	 *  @return true/false if the card should be generated*/
	public static boolean evaluate(Card card){
		double value = 0;
		for (Action action : card.getActions()) {
			value += action.getValue();
		}
		for (Event event : card.getEvents()) {
			value += event.getValue();
		}
		if (value >= 2 && value <= 4)
			return true;
		return false;
	}

	/** Owner of cards */
	private Player owner;

	/** Lists of actions and events initialized */
	private TreeSet<Action> actions = new TreeSet<Action>();
	
	private TreeSet<Event> events = new TreeSet<Event>();

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
			Action action = new Action(suits);			
			if (actions.add(action))
				suits.remove(action.getSuit());
		}
		// add new events
		Integer eventsCount = gen.nextInt(3);
		while (events.size() < eventsCount){
			Event event = new Event();
			events.add(event);
		}

	}

	/**
	 * Access a list of actions
	 * 
	 * @return actions
	 * */
	public TreeSet<Action> getActions() {
		return actions;
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
	 * Access a list of events
	 * 
	 * @return events
	 * */
	public TreeSet<Event> getEvents() {
		return events;
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
	 * Given a target, act according to actions and events
	 * 
	 * @param target
	 *            The target opponent
	 * */
	public PlayResult play(Player target) {
		Integer cost = getCost();
		//Barter: the players next diamond cost is halved
		if(owner.getEffects().get(EventType.Barter)){
			cost /= 2;
			owner.getEffects().put(EventType.Barter, false);
		}
		//Load: the players next diamond cost is doubled
		if(owner.getEffects().get(EventType.Load)){
			cost *= 2;
			owner.getEffects().put(EventType.Load, false);
		}
		if (owner.canAfford(cost)) {
			if (needTarget() && target.equals(owner))
				return PlayResult.NeedTarget;
			if (!needTarget() && !target.equals(owner))
				return PlayResult.SelfTarget;
			owner.setDiamond(owner.getDiamond() - cost);
			for (Action action : getActions())
				action.play(owner, target);
			for (Event event : getEvents())
				event.play(owner, target, this);
			return PlayResult.OK;
		}
		return PlayResult.CantAfford;
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
}
