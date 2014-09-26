package com.diamondshearts.models;

import java.util.ArrayList;

public class Table {
	private Player currentPlayer;
	private ArrayList<Player> players;
	private Integer playsThisTurn;
	private Integer diamond;
	private Integer turnCounter;

	public boolean debug;

	public Table() {
		this(false);
	}


	public Table(boolean debug) {
		this.debug = debug;
		turnCounter = 0;
		if (debug) {
			players = new ArrayList<Player>();
			for (int i = 0; i < 5; i++)
				players.add(new Player(this, i + "", "TestPlayer" + i));
			currentPlayer = players.get(3);
			playsThisTurn = 3;
		}
	}

	/**
	 * Get the next participant.
	 * 
	 * @return participantId of next player, or null if automatching
	 */
	public String getNextParticipantId(Integer autoMatchSlots) {
		int desiredIndex = -1;
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getId().equals(currentPlayer.getId())) {
				desiredIndex = i + 1;
			}
		}

		if (desiredIndex < players.size()) {
			return players.get(desiredIndex).getId();
		}

		if (autoMatchSlots <= 0) {
			// You've run out of automatch slots, so we start over.
			return players.get(0).getId();
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

	public Player getPlayerThisTurn() {
		return players.get(playsThisTurn);
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public Integer getTurnCounter() {
		return turnCounter;
	}

	public void setTurnCounter(Integer turnCounter) {
		this.turnCounter = turnCounter;
	}

	public Integer getRound() {
		return turnCounter / players.size() + 1;
	}
}
