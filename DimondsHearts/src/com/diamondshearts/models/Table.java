package com.diamondshearts.models;

import java.util.ArrayList;

public class Table {
	private Player currentPlayer;
	private ArrayList<Player> players;
	private Integer playsThisTurn;	
	private Integer diamond;
	
	public Table(){
		players = new ArrayList<Player>();
		for (int i = 0; i < 5; i++)
			players.add(new Player());
		setCurrentPlayer(players.get(0));
		playsThisTurn = 0;
	}

	@SuppressWarnings("unused")
	private void executeCard(Player player, Card card) {
		if (!player.canAfford(card.getCost())) {
			//ERROR
		}

		player.pay(this, card.getCost());

		for (Action action : card.getActions()) {
			switch (action.getSuit()) {
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

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
	
	public Player getPlayerThisTurn(){
		return players.get(playsThisTurn);
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
}
