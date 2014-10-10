package com.diamondshearts.models;

import java.util.ArrayList;
import java.util.Random;

/**
 * An action is a represented by a suit (Hearts, Clubs, Diamonds, Spades) 
 * and a number from 1 to 9  
 * */
public class Action {
	/**Random number for*/
	private static Random gen = new Random();
	/**Action rank*/
	private Integer rank;
	/**A suit*/
	private Suit suit;
	/**Suit description*/
	private String description;

	/**
	 * Initialize a action
	 * */
	public Action(ArrayList<Suit> suits) {
		// select a suit based on the given distribution
		int choice = gen.nextInt(suits.size());
		setSuit(suits.get(choice));
		setDescription(suits.get(choice).getDescription());

		// define statistical distribution of ranks
		int[] rankDistribution = { 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 5, 5, 6, 6,
				7, 8, 9 };
		choice = gen.nextInt(rankDistribution.length);
		setRank(rankDistribution[choice]);
	}

	@Override
	/**
	 * Check if card has equal action
	 * @param o
	 * 		   Card object
	 * @return true/false
	 * */
	public boolean equals(Object o) {
		if (o.getClass() == Action.class) {
			Action tar = (Action) o;
			return (tar.getRank() == rank) && (tar.getSuit() == suit);
		}
		return false;
	}

	/**
	 * Access the rank
	 * @return rank
	 * */
	public Integer getRank() {
		return rank;
	}

	/**
	 * Modify the rank
	 * @param rank
	 * 			  Rank of the action
	 * */
	public void setRank(Integer rank) {
		this.rank = rank;
	}

	/**
	 * Access the suit
	 * @return suit
	 * */
	public Suit getSuit() {
		return suit;
	}

	/**
	 * Modify the suit
	 * @param suit
	 * 			  The suit
	 * */
	public void setSuit(Suit suit) {
		this.suit = suit;
	}

	/**
	 * Access the description
	 * @return description
	 * */
	public String getDescription() {
		return description;
	}

	/**
	 * Modify the description of a card
	 * @param description
	 * 					 The description for a card
	 * */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Play against target opponent according to action 
	 * @param target
	 * 				The targeted opponent
	 * */
	public void play(Player target) {
		// TODO Auto-generated method stub
		
	}
}