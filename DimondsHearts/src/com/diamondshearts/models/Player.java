package com.diamondshearts.models;

import java.util.ArrayList;

public class Player {

	private static final int INITIALHAND = 5;
	private String participantId;
	private String name;
	private Integer diamond;
	private Integer heart;
	private ArrayList<Card> hand;	
	private Table table;
	
	// Test only
	public Player(Table table) {
		this.name = "TestPlayerName";
		diamond = 30;
		heart = 20;
		initialHand();
	}

	public Player(Table table, String participantId, String displayName) {
		this.setParticipantId(participantId);
		this.name = displayName;
		diamond = 30;
		heart = 20;
		initialHand();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getDiamond() {
		return diamond;
	}

	public void setDiamond(Integer diamond) {
		this.diamond = diamond;
	}

	public Integer getHeart() {
		return heart;
	}

	public void setHeart(Integer heart) {
		this.heart = heart;
	}

	public ArrayList<Card> getHand() {
		return hand;
	}

	public void setHand(ArrayList<Card> hand) {
		this.hand = hand;
	}

	public void drawCard(int cost) {
		// check if the card can be afforded
		// deduct funds and append card
	}

	public Boolean canSurvive(int amount) {
		return (heart >= amount);
	}

	public void hit(Player target, int power) {

	}

	public Boolean canAfford(int amount) {
		return (this.diamond >= amount);
	}

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

	public void pay(Table table, int amount) {
		if (!this.canAfford(amount)) {
			// ERROR
		}
		diamond -= amount;
		table.setDiamond(table.getDiamond() + amount);
		return;
	}

	public void initialHand() {
		hand = new ArrayList<Card>();
		for (int i = 0; i < INITIALHAND; i++) {
			hand.add(new Card(this));
		}
	}

	public ArrayList<String> getLabels() {
		ArrayList<String> labels = new ArrayList<String>();
		labels.add("♦: " + diamond);
		labels.add("♥: " + heart);
		labels.add("H: " + hand.size());
		return labels;
	}

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}

	/**
	 * @return the table
	 */
	public Table getTable() {
		return table;
	}

	/**
	 * @param table the table to set
	 */
	public void setTable(Table table) {
		this.table = table;
	}
}
