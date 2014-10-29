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
	/** List of gameplay results */
	private ArrayList<ParticipantResult> results;
	/** Number of cards drawn */
	private int cardDrawed;

	/**
	 * Initialize a table
	 * 
	 * @param debug
	 *            Debug value set to be false atm
	 * */
	public Table() {
		turnCounter = 0;
		cardPlayed = new ArrayList<Card>();
		cardDrawed = 0;
		results = new ArrayList<ParticipantResult>();
	}

	/** Count the number of players still alive
	 * @return number of alive players*/
	public int countAlivedPlayers() {
		int count = 0;
		for (Player player : players)
			if (player.isAlive())
				count++;
		return count;
	}

	/**
	 * Get the card drawn
	 * @return the cardDrawed
	 */
	public int getCardDrawed() {
		return cardDrawed;
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
	 * Access the current player
	 * 
	 * @return current player
	 * */
	public Player getCurrentPlayer() {
		return currentPlayer;
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
	 * Access the players
	 * 
	 * @return players The players
	 * */
	public ArrayList<Player> getPlayers() {
		return players;
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
	 * @return the results
	 */
	public ArrayList<ParticipantResult> getResults() {
		return results;
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
	 * Access the turn counter
	 * 
	 * @return turnCounter
	 * */
	public Integer getTurnCounter() {
		return turnCounter;
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
	 * Get the player who was hit from last turn
	 * 
	 * @param player
	 *            The player
	 * */
	public boolean isPlayerLastHit(Player player) {
		return player.equals(playerTLastHit);
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
	 * Get preGame boolean
	 * 
	 * @return pregame the pregame
	 */
	public boolean isPreGame() {
		return preGame;
	}

	/**
	 * Set the card drawn
	 * @param cardDrawed the cardDrawed to set
	 */
	public void setCardDrawed(int cardDrawed) {
		this.cardDrawed = cardDrawed;
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
	 * Modify the number of diamonds on the table
	 * 
	 * @param diamond
	 *            The number of diamonds
	 * */
	public void setDiamond(Integer diamond) {
		this.diamond = diamond;
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
	 * Modify the players
	 * 
	 * @param players
	 *            The players
	 * */
	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
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
	 * Set the preGame
	 * 
	 * @param pregame
	 *            The pregame to set
	 */
	public void setPreGame(boolean pregame) {
		this.preGame = pregame;
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
}
