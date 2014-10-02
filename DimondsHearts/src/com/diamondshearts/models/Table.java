package com.diamondshearts.models;

import java.util.ArrayList;

/**
 * Table where a game occurs
 * */
public class Table {
	/**The current player*/
	private Player currentPlayer;
	/**The list of players*/
	private ArrayList<Player> players;
	//plays this turn
	private Player playerThisTurn;
	
	//the number of diamonds
	private Integer diamond;

	/**Counting number of turns have occurred*/
	private Integer turnCounter;
	
	private boolean preGame;
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
			currentPlayer = setPlayerThisTurn(players.get(3));		 	
		}
	}

	/**
	 * Get the next participant.
	 * 
	 * @return participantId of next player, or null if auto-matching
	 */
	public String getNextParticipantId() {
		int desiredIndex = -1;
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getId().equals(playerThisTurn.getId())) {
				desiredIndex = i + 1;
			}
		}

		if (desiredIndex < players.size()) {
			return players.get(desiredIndex).getId();
		}
		return players.get(0).getId();		
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

	/**
	 * @return the playerThisTurn
	 */
	public Player getPlayerThisTurn() {
		return playerThisTurn;
	}

	/**
	 * @param playerThisTurn the playerThisTurn to set
	 */
	public Player setPlayerThisTurn(Player playerThisTurn) {
		this.playerThisTurn = playerThisTurn;
		return playerThisTurn;
	}
	
	public Player getPlayerById(String id){
		for (Player player : players) {
			if (player.getId().equals(id))
				return player;
		}
		return null;
	}
	
	public boolean isMyTurn(){
		return currentPlayer.equals(playerThisTurn);
	}

	/**
	 * @return the pregame
	 */
	public boolean isPreGame() {
		return preGame;
	}

	/**
	 * @param pregame the pregame to set
	 */
	public void setPreGame(boolean pregame) {
		this.preGame = pregame;
	}
	
	@Override
	public String toString() {
		String str = "";
		for (Player player : players){
			str += player.getName();
			str += ": Heart: ";
			str += player.getHeart();
			str += "; ";	
		}
		return str;
	}
}
