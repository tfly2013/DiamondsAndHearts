package com.diamondshearts.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * An action is a represented by a suit (Hearts, Clubs, Diamonds, Spades) 
 * and a number from 1 to 9  
 * */
public class Action {
	/**random number for*/
	private static Random gen = new Random();
	/**action rank*/
	private Integer rank;
	/**a suit*/
	private Suit suit;
	/**suit description*/
	private String description;

	/** define statistical distribution of suits*/
	public static final Suit[] allSuits = Suit.class.getEnumConstants();

	/**
	 * Initialize a action
	 * */
	public Action() {
		// select a suit based on the given distribution
		List<Suit> selection = new ArrayList<Suit>();
		for (int i = 0; i < allSuits.length; i++) {
			for (int j = 0; j < allSuits[i].getFrequency(); j++)
				selection.add(allSuits[i]);
		}
		int choice = gen.nextInt(allSuits.length);
		setSuit(selection.get(choice));
		setDescription(selection.get(choice).getDescription());

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