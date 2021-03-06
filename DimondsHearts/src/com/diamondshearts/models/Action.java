package com.diamondshearts.models;

import java.util.ArrayList;
import java.util.Random;

/**
 * An action is a represented by a suit (Hearts, Clubs, Diamonds, Spades) and a
 * number from 1 to 9
 * */
public class Action implements Comparable<Action> {
	/** Random number for */
	private static Random gen = new Random();
	/** Action rank */
	private Integer rank;
	/** A suit */
	private Suit suit;
	/** Suit description */
	private String description;

	/**
	 * Initialize a action
	 * 
	 * @param suits
	 *            Suits enum
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
	/** Compare suits of two cards
	 * @return comparison result */
	public int compareTo(Action another) {
		return suit.compareTo(another.getSuit());
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
			return (tar.getRank().equals(rank)) && (tar.getSuit().equals(suit));
		}
		return false;
	}

	/**
	 * Access the description
	 * 
	 * @return description
	 * */
	public String getDescription() {
		return description;
	}

	/**
	 * Access the rank
	 * 
	 * @return rank
	 * */
	public Integer getRank() {
		return rank;
	}

	/**
	 * Access the suit
	 * 
	 * @return suit
	 * */
	public Suit getSuit() {
		return suit;
	}

	/** Get the value coefficient of selected suit
	 * @return value */
	public double getValue() {
		return getSuit().getValueCoefficient() * getRank();
	}

	/**
	 * Play against target opponent according to action
	 * 
	 * @param target
	 *            The targeted opponent
	 * */
	public void play(Player owner, Player target) {
		Integer power = rank;
		switch (suit) {
		case Club:
			// Strength: the player's next Club card does double damage
			if (owner.getEffects().get(EventType.Strength)) {
				power *= 2;
				owner.getEffects().put(EventType.Strength, false);
			}
			// Weaken: the player’s next Club card does half damage
			if (owner.getEffects().get(EventType.Weaken)) {
				power /= 2;
				owner.getEffects().put(EventType.Weaken, false);
			}
			if (!target.getEffects().get(EventType.Shield)) {
				target.setHeart(target.getHeart() - power);
			} else {
				// Shield: protect player's from next attack, no damage done
				target.getEffects().put(EventType.Shield, false);
			}
			// Drain: the player's next Club card steals the target’s Hearts
			if (owner.getEffects().get(EventType.Drain)) {
				owner.setHeart(owner.getHeart() + power);
				owner.getEffects().put(EventType.Drain, false);
			}
			// Reaction: the player’s next Club does damage to target but reacts
			// back to himself
			if (owner.getEffects().get(EventType.Reaction)) {
				owner.setHeart(owner.getHeart() - power);
				owner.getEffects().put(EventType.Reaction, false);
			}
			break;
		case Heart:
			if (owner.getEffects().get(EventType.Fortitude)) {
				// Fortitude: the player's next Heart card heals twice as much
				power *= 2;
				owner.getEffects().put(EventType.Fortitude, false);
			}
			if (owner.getEffects().get(EventType.Curse)) {
				// Curse: the player’s next Heart card will do damage instead
				owner.setHeart(owner.getHeart() - power);
				owner.getEffects().put(EventType.Curse, false);
			} else {
				owner.setHeart(owner.getHeart() + power);
			}
			break;
		case Spade:
			// Guilty: the player’s next Spade card steals nothing instead
			if (!owner.getEffects().get(EventType.Guilty)) {
				// not guilty
				Integer targetDiamond = target.getDiamond();
				if (owner.getEffects().get(EventType.Stealth)) {
					// Stealth: the player's next Spade card steals twice as
					// much
					power *= 2;
					owner.getEffects().put(EventType.Stealth, false);
				}
				if (targetDiamond < power)
					power = targetDiamond;
				target.setDiamond(targetDiamond - power);
				owner.setDiamond(owner.getDiamond() + power);

			} else {
				owner.getEffects().put(EventType.Guilty, false);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Modify the description of a card
	 * 
	 * @param description
	 *            The description for a card
	 * */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Modify the rank
	 * 
	 * @param rank
	 *            Rank of the action
	 * */
	public void setRank(Integer rank) {
		this.rank = rank;
	}

	/**
	 * Modify the suit
	 * 
	 * @param suit
	 *            The suit
	 * */
	public void setSuit(Suit suit) {
		this.suit = suit;
	}
}
