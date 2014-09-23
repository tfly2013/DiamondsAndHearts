package com.diamondshearts.models;

import java.util.ArrayList;

public class Table {
	private Player currentPlayer;
	private ArrayList<Player> players;
	private Integer playsThisTurn;
	private Integer diamond;	
	//private TurnBasedMatch match;
	
	public Table(){
		//this.setMatch(match);
	}

	/**
	 * Get the next participant. In this function, we assume that we are
	 * round-robin, with all known players going before all automatch players.
	 * This is not a requirement; players can go in any order. However, you can
	 * take turns in any order.
	 * 
	 * @return participantId of next player, or null if automatching
	 */
	public String getNextParticipantId(Integer autoMatchSlots) {
		int desiredIndex = -1;
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getParticipantId().equals(currentPlayer.getParticipantId())) {
				desiredIndex = i + 1;
			}
		}

		if (desiredIndex < players.size()) {
			return players.get(desiredIndex).getParticipantId();
		}

		if (autoMatchSlots <= 0) {
			// You've run out of automatch slots, so we start over.
			return players.get(0).getParticipantId();
		} else {
			// You have not yet fully automatched, so null will find a new
			// person to play against.
			return null;
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

//	public TurnBasedMatch getMatch() {
//		return match;
//	}
//
//	public void setMatch(TurnBasedMatch match) {
//		this.match = match;
//	}
	
	
//	private void executeCard(Player player, Card card) {
//		if (!player.canAfford(card.getCost())) {
//			//ERROR
//		}
//
//		player.pay(this, card.getCost());
//
//		for (Action action : card.getActions()) {
//			switch (action.getSuit()) {
//			case Spade:
//
//				break;
//			case Heart:
//				
//				break;
//			case Diamond:
//				
//				break;
//			case Club:
//				
//				break;
//			}
//			/*
//			current = iterator.next();
//			if (current.suit == 0) {
//				current.target.hp -= current.rank;
//			}
//			if (current.suit == 1) {
//				player.hp += current.rank;
//			}				
//			if (current.suit == 2) {
//				if (current.target.dp < current.rank) {
//					player.dp += current.target.dp
//					current.target.dp = 0;
//				} else {
//					player.dp += current.rank;
//					current.target.dp -= current.rank;
//				}
//			}
//			*/
//		}
//	}

}
