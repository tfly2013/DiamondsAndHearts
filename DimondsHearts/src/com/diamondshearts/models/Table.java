package com.diamondshearts.models;

import java.util.ArrayList;

import com.google.android.gms.games.multiplayer.ParticipantResult;

/**
 * Table where a game occurs
 * */
public class Table {
	/** The current player */
	private Player currentPlayer;
	/** The list of players */
	private ArrayList<Player> players;
	/** Player plays this turn */
	private Player playerThisTurn;
	/** Player who was hit */
	private Player playerTLastHit;
	/** the number of diamonds on the table */
	private Integer diamond;
	/** Counting number of turns have occurred */
	private Integer turnCounter;
	/** Pregame boolean */
	private boolean preGame;
	/** All cards have been played since the game starts */
	private ArrayList<Card> cardPlayed;

	private ArrayList<ParticipantResult> results;
	public boolean debug;

	/**
	 * Initialize a table
	 * 
	 * @param debug
	 *            Debug value set to be false atm
	 * */
	public Table(boolean debug) {
		this.debug = debug;
		turnCounter = 0;
		cardPlayed = new ArrayList<Card>();
		results = new ArrayList<ParticipantResult>();
		if (debug) {
			players = new ArrayList<Player>();
			for (int i = 0; i < 5; i++)
				players.add(new Player(this, null));
			setPlayerThisTurn(players.get(3));
			currentPlayer = getPlayerThisTurn();
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
	 * 
	 * @return diamond
	 * */
	public Integer getDiamond() {
		return diamond;
	}

	/**
	 * Modify the number of diamonds on the table
	 * 
	 * @param diamond
	 *            The number of diamonds
	 * */
	public void setDiamond(Integer diamond) {
		this.diamond = diamond;
	}

	/**
	 * Access the players
	 * 
	 * @return players The players
	 * */
	public ArrayList<Player> getPlayers() {
		return players;
	}

	/**
	 * Modify the players
	 * 
	 * @param players
	 *            The players
	 * */
	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	/**
	 * Access the current player
	 * 
	 * @return current player
	 * */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	/**
	 * Modify the current player
	 * 
	 * @param currentPlayer
	 *            The current player
	 * */
	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	/**
	 * Access the turn counter
	 * 
	 * @return turnCounter
	 * */
	public Integer getTurnCounter() {
		return turnCounter;
	}

	/**
	 * Modify the turn counter
	 * 
	 * @param turn
	 *            counter
	 * */
	public void setTurnCounter(Integer turnCounter) {
		this.turnCounter = turnCounter;
	}

	/**
	 * Calculate the number of rounds
	 * 
	 * @return rounds
	 * */
	public Integer getRound() {
		return turnCounter / players.size() + 1;
	}

	/**
	 * Access the player of this turn
	 * 
	 * @return the playerThisTurn
	 */
	public Player getPlayerThisTurn() {
		return playerThisTurn;
	}

	/**
	 * Modify the player in this turn
	 * 
	 * @param playerThisTurn
	 *            the playerThisTurn to set
	 */
	public void setPlayerThisTurn(Player playerThisTurn) {
		this.playerThisTurn = playerThisTurn;
	}

	/**
	 * Map player id to the player
	 * 
	 * @param id
	 *            The player's id
	 * @return player The corresponding player
	 * */
	public Player getPlayerById(String id) {
		for (Player player : players) {
			if (player.getId().equals(id))
				return player;
		}
		return null;
	}

	/**
	 * Check if it is the current player's turn
	 * 
	 * @return true/false This might be the current player's turn
	 * */
	public boolean isMyTurn() {
		return isPlayerTurn(currentPlayer);
	}

	/**
	 * Check if it is the player's turn
	 * 
	 * @param player
	 *            The player
	 * */
	public boolean isPlayerTurn(Player player) {
		return !preGame && player.equals(playerThisTurn);
	}

	/**
	 * Modify the last player who was hit
	 * 
	 * @param playerTLastHit
	 *            the playerTLastHit to set
	 */
	public void setPlayerLastHit(Player playerTLastHit) {
		this.playerTLastHit = playerTLastHit;
	}

	/**
	 * Get the player who was hit from last turn
	 * 
	 * @param player
	 *            The player
	 * */
	public boolean isPlayerLastHit(Player player) {
		return player.equals(playerTLastHit);
	}

	/**
	 * Get preGame boolean
	 * 
	 * @return pregame the pregame
	 */
	public boolean isPreGame() {
		return preGame;
	}

	/**
	 * Set the preGame
	 * 
	 * @param pregame
	 *            The pregame to set
	 */
	public void setPreGame(boolean pregame) {
		this.preGame = pregame;
	}

	/**
	 * Access the all cards played
	 * 
	 * @return the cardPlayed All cards played
	 */
	public ArrayList<Card> getCardPlayed() {
		return cardPlayed;
	}

	/**
	 * @return the results
	 */
	public ArrayList<ParticipantResult> getResults() {
		return results;
	}

	public int countAlivedPlayers() {
		int count = 0;
		for (Player player : players)
			if (player.isAlive())
				count++;
		return count;
	}
}
