package com.diamondshearts.models;

import java.util.ArrayList;
import java.util.EnumMap;

import android.net.Uri;

import com.google.android.gms.games.multiplayer.Participant;

/**
 * Player who participate in the turn-based card game
 * */
public class Player {
	/**Initial hands*/
	private static final int INITIALHAND = 5;
	/**The number of diamonds a player has*/
	private Integer diamond;
	/**The number of hearts a player has*/
	private Integer heart;
	/**The hand of cards a player holds*/
	private ArrayList<Card> hand;
	/**The table where player plays the game*/
	private Table table;
	/**Participant in the game*/	
	private Participant participant;
	/**EventActivated map*/
	private EnumMap<EventType, Boolean> effects;
	
	private boolean alive;

	/**
	 * Initialize a player
	 * @param table
	 * 			   The table player plays the game
	 * @param participant
	 * 			   The player as participant
	 * */	
	public Player(Table table, Participant participant) {
		this.table = table;
		this.participant = participant;
		effects = new EnumMap<EventType, Boolean>(EventType.class);
		for (EventType key : EventType.class.getEnumConstants())
			effects.put(key, false);
		diamond = 30;
		heart = 1;
		alive = true;
		initialHand();
	}

	@Override
	/**
	 * Check if two players are the same person
	 * @param o
	 * 			Player object
	 * @return true/false
	 * */
	public boolean equals(Object o) {
		if (o != null && o.getClass() == Player.class) {
			Player tar = (Player) o;
			return (tar.getId().equals(getId()));
		}
		return false;
	}

	/**
	 * Access the name of the player
	 * @return name
	 * */
	public String getName() {
		if (participant == null)
			return "TestPlayer";
		return participant.getDisplayName();
	}

	/**
	 * Access the diamond
	 * @return diamond
	 * */
	public Integer getDiamond() {
		return diamond;
	}

	/**
	 * Modify the number of diamonds
	 * @param diamond
	 * 				 The number of diamonds
	 * */
	public void setDiamond(Integer diamond) {
		this.diamond = diamond;
	}

	/**
	 * Access the number of hearts
	 * @return heart
	 * */
	public Integer getHeart() {
		return heart;
	}

	/**
	 * Modify the number of hearts
	 * @param heart
	 * 			   The number of hearts
	 * */
	public void setHeart(Integer heart) {
		this.heart = heart;
	}

	/**
	 * Access the hand
	 * @return hand
	 * */
	public ArrayList<Card> getHand() {
		return hand;
	}

	/**
	 * Modify the hand
	 * @param hand
	 * 			  The hand
	 * */
	public void setHand(ArrayList<Card> hand) {
		this.hand = hand;
	}

		/**
	 * Check if diamond is sufficient
	 * @return true/false
	 * */
	public Boolean canAfford(int amount) {
		return (this.diamond >= amount);
	}

	/**
	 * Add new cards into the hand to initialize the hand
	 * */
	public void initialHand() {
		hand = new ArrayList<Card>();
		for (int i = 0; i < INITIALHAND; i++) {
			hand.add(Card.draw(this));
		}
	}

	/**
	 * Add diamond, hearts and hand information onto the label
	 * @return labels
	 * 				 The label of a player
	 * */
	public ArrayList<String> getLabels() {
		ArrayList<String> labels = new ArrayList<String>();
		labels.add("♦: " + diamond);
		labels.add("♥: " + heart);
		labels.add("H: " + hand.size());
		return labels;
	}

	/**
	 * Access the player participant id
	 * @return participantId
	 * 						The participant id
	 * */
	public String getId() {
		if (participant == null)
			return "test";
		return participant.getParticipantId();
	}

	/**
	 * Access the table
	 * @return the table
	 */
	public Table getTable() {
		return table;
	}
	
	/**
	 * Access the player image from server
	 * @return Uri
	 * 			  The player profile image uri
	 * */
	public Uri getImageUri() {
		if (participant == null)
			return null;
		return participant.getIconImageUri();
	}

	/**
	 * A data structure where key is various event types and values are booleans
	 * @return eventActivated
	 * 						A map of events might have been activated
	 * */
	public EnumMap<EventType, Boolean> getEffects() {
		return effects;
	}

	/**
	 * @return the alive
	 */
	public boolean isAlive() {
		return alive;
	}

	/**
	 * @param alive the alive to set
	 */
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
}
