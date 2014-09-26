package com.diamondshearts.models;

import java.util.ArrayList;

/**
 * Table where a game occurs
 * */
public class Table {
	/**the current player*/
	private Player currentPlayer;
	/**the list of players*/
	private ArrayList<Player> players;
	/**plays this turn*/
	private Integer playsThisTurn;
	/**the number of diamonds*/
	private Integer diamond;
	/**counting number of turns have occurred*/
	private Integer turnCounter;
	
	public boolean debug;

	/**
	 * Table Constructor for debug purpose
	 * */
	public Table() {
		this(false);
	}

	/**
	 * Initialize a table
	 * @param debug
	 * 			   Debug value set to be false atm
	 * */
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
	 * @return participantId of next player, or null if auto-matching
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
			// You've run out of auto-match slots, so we start over.
			return players.get(0).getId();
		} else {
			// You have not yet fully auto-matched, so null will find a new
			// person to play against.
			return null;
		}
	}

	/**
	 * Access the number of diamonds on the table
	 * @return diamond
	 * */
	public Integer getDiamond() {
		return diamond;
	}

	/**
	 * Modify the number of diamonds on the table
	 * @param diamond
	 * 				 The number of diamonds
	 * */
	public void setDiamond(Integer diamond) {
		this.diamond = diamond;
	}

	/**
	 * Access the players
	 * @return players
	 * 				  The players
	 * */
	public ArrayList<Player> getPlayers() {
		return players;
	}

	/**
	 * Modify the players
	 * @param players
	 * 		 		The players
	 * */
	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}
	
	/**
	 * Access the player who plays this turn
	 * @return player plays the turn
	 * */
	public Player getPlayerThisTurn() {
		return players.get(playsThisTurn);
	}

	/**
	 * Access the current player
	 * @return current player
	 * */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	/**
	 * Modify the current player
	 * @param currentPlayer
	 * 					   The current player
	 * */
	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	/**
	 * Access the turn counter
	 * @return turnCounter
	 * */
	public Integer getTurnCounter() {
		return turnCounter;
	}

	/**
	 * Modify the turn counter
	 * @param turn counter
	 * */
	public void setTurnCounter(Integer turnCounter) {
		this.turnCounter = turnCounter;
	}

	/**
	 * Calculate the number of rounds
	 * @return rounds
	 * */
	public Integer getRound() {
		return turnCounter / players.size() + 1;
	}
}