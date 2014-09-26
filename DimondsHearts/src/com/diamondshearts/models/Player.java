package com.diamondshearts.models;

import java.util.ArrayList;

/**
 * Player who participate in the turn-based card game
 * */
public class Player {
	//initial hands
	private static final int INITIALHAND = 5;
	//participant id
	private String participantId;
	//player name
	private String name;
	//the number of diamonds a player has
	private Integer diamond;
	//the number of hearts a player has
	private Integer heart;
	//the hand of cards a player holds
	private ArrayList<Card> hand;
	//the table where player plays the game
	private Table table;

	/**
	 * Initialize a player
	 * @param table
	 * 			   The table player plays the game
	 * @param participantId
	 * 			   The player id
	 * @param displayName
	 * 			   The player's alias name
	 * */
	public Player(Table table, String participantId, String displayName) {
		this.setId(participantId);
		this.name = displayName;
		diamond = 30;
		heart = 20;
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
		return name;
	}

	/**
	 * Modify the name of the player
	 * @param name
	 * 			   Player's name
	 * */
	public void setName(String name) {
		this.name = name;
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
	 * draw a card if can afford one
	 * @param cost
	 * 			  The cost of card
	 * */
	public void drawCard(int cost) {
		// check if the card can be afforded
		// deduct funds and append card
	}

	/**
	 * Check if a player can survive
	 * @return true/false
	 * */
	public Boolean canSurvive(int amount) {
		return (heart >= amount);
	}

	/**
	 * Hit a target
	 * @param target
	 * 				Target player
	 * @param power
	 * 				According to action and event
	 * */
	public void hit(Player target, int power) {

	}

	/**
	 * Check if diamond is sufficient
	 * @return true/false
	 * */
	public Boolean canAfford(int amount) {
		return (this.diamond >= amount);
	}

	/**
	 * Rob diamond from target
	 * @param target
	 * 			    Target player
	 * @param power
	 * 				According to action and event
	 * */
	public void rob(Player target, int power) {
		if (target.diamond < power) {
			power = target.diamond;
			target.diamond = 0;
		} else {
			target.diamond -= power;
		}
		this.diamond += power;
		return;
	}

	/**
	 * Pay to the table by amount
	 * @param table
	 * 			   The table where player is currently playing the game
	 * @param amount
	 * 			   The amount of diamond is paid
	 * */
	public void pay(Table table, int amount) {
		if (!this.canAfford(amount)) {
			// ERROR
		}
		diamond -= amount;
		table.setDiamond(table.getDiamond() + amount);
		return;
	}

	/**
	 * Add new cards into the hand to initialize the hand
	 * */
	public void initialHand() {
		hand = new ArrayList<Card>();
		for (int i = 0; i < INITIALHAND; i++) {
			hand.add(new Card(this));
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
		return participantId;
	}

	/**
	 * Modify the player's participant id
	 * @param participantId
	 * 					   The new participant id
	 * */
	public void setId(String participantId) {
		this.participantId = participantId;
	}

	/**
	 * Access the table
	 * @return the table
	 */
	public Table getTable() {
		return table;
	}

	/**
	 * Modify the table
	 * @param table
	 * 			   The table to set
	 */
	public void setTable(Table table) {
		this.table = table;
	}
}