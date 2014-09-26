package com.diamondshearts.models;

import java.util.ArrayList;

// the user can sort their cards into piles
public class Deck {
	private ArrayList<Pile> piles = new ArrayList<Pile>();
	
	public Deck(Player owner) {
		this.addPile();
	}
	
	public void addCard(Card card) {
		Pile firstPile = piles.get(0);
		this.AddCard(card, firstPile);
	}

	public void addCard(Card card, Pile pile) {
		this.AddCard(card, pile);
	}

	public void addPile() {
		this.piles.add(new Pile());
	}

	private ArrayList<Pile> getPiles() {
		return piles;
	}

	private class Pile {
		private ArrayList<Card> cards = new ArrayList<Card>();
		
		public Pile() {
		}
		
		public void addCard(Card card) {
			this.cards.add(card);
		}
		
		public ArrayList<Card> getCards() {
			return this.cards;
		}
	}
}