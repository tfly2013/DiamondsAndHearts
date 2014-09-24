package com.diamondshearts.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Action {
	private static Random gen = new Random();
	private Integer rank;
	private Suit suit;
	private String description;

	// define statistical distribution of suits
	public static final Suit[] allSuits = Suit.class.getEnumConstants();

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
	public boolean equals(Object o) {
		if (o.getClass() == Action.class) {
			Action tar = (Action) o;
			return (tar.getRank() == rank) && (tar.getSuit() == suit);
		}
		return false;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Suit getSuit() {
		return suit;
	}

	public void setSuit(Suit suit) {
		this.suit = suit;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void play(Player target) {
		// TODO Auto-generated method stub
		
	}
}
