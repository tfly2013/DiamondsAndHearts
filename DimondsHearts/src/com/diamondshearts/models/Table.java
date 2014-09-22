package com.diamondshearts.models;

import java.util.Iterator;

public class Table {
	private Player[] players;
	private Integer playsThisTurn;	
	private Integer diamond;

	@SuppressWarnings("unused")
	private void executeCard(Player player, Card card) {
		if (!player.canAfford(card.getCost())) {
			//ERROR
		}

		player.pay(this, card.getCost());

		Iterator<Action> iterator = card.actions.iterator();
		while (iterator.hasNext()) {
			Action current = iterator.next();
			switch (current.getSuit()) {
			case Spade:
				//TODO
				break;
			case Heart:
				
				break;
			case Diamond:
				
				break;
			case Club:
				
				break;
			}
			/*
			current = iterator.next();
			if (current.suit == 0) {
				current.target.hp -= current.rank;
			}
			if (current.suit == 1) {
				player.hp += current.rank;
			}				
			if (current.suit == 2) {
				if (current.target.dp < current.rank) {
					player.dp += current.target.dp
					current.target.dp = 0;
				} else {
					player.dp += current.rank;
					current.target.dp -= current.rank;
				}
			}
			*/
		}
	}

	public Integer getDiamond() {
		return diamond;
	}

	public void setDiamond(Integer diamond) {
		this.diamond = diamond;
	}

	public Player[] getPlayers() {
		return players;
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}
	
	public Player getPlayerThisTurn(){
		return players[playsThisTurn];
	}
}
