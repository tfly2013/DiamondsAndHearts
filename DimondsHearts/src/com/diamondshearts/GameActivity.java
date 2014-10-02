package com.diamondshearts;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diamondshearts.models.Card;
import com.diamondshearts.models.Player;
import com.diamondshearts.models.Table;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.turnbased.OnTurnBasedMatchUpdateReceivedListener;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.example.games.basegameutils.BaseGameActivity;
import com.thoughtworks.xstream.XStream;

/**
 * GameActivity handles the player views, card views, round views, the layouts
 * as well as how to exit a game and how to finish a game
 * 
 * @author Fei Tang & Kimple Ke(co-author)
 * */
public class GameActivity extends BaseGameActivity implements
		OnTurnBasedMatchUpdateReceivedListener {
	/** Player Layout (Up) */
	private LinearLayout playersUpLayout;

	/** Player Layout (Down) */
	private LinearLayout playersDownLayout;

	/** Current Player Layout */
	private LinearLayout currentPlayerLayout;

	/** Layout of current player's hand */
	private LinearLayout handLayout;

	/** The text view to show round */
	private TextView midMessageView;

	private Button doneButton;

	/** The table state of game */
	private Table table;

	/** The player */
	private String playerId;

	/** Current Player */
	private Player currentPlayer;

	/** The match */
	private TurnBasedMatch match;

	/** The XStream object. */
	private XStream xStream;

	@Override
	/**
	 * Initialize the activity and match data.
	 */
	protected void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.activity_game);

		// Retrieve match data from MainActivity
		xStream = new XStream();
		xStream.alias("table", Table.class);
		String matchKey = "com.diamondshearts.match";
		if (getIntent().hasExtra(matchKey)
				&& (match = getIntent().getParcelableExtra(matchKey)) != null) {
			String tableData = new String(match.getData());
			table = (Table) xStream.fromXML(tableData);
		} else {
			// Table for debugging
			table = new Table(true);
		}
		// Retrieve current logged in player Id
		playerId = null;
		String playerIdKey = "com.diamondshearts.playerid";
		if (getIntent().hasExtra(playerIdKey))
			playerId = getIntent().getStringExtra(playerIdKey);

		// Set up layouts
		playersUpLayout = (LinearLayout) findViewById(R.id.players_up_layout);
		playersDownLayout = (LinearLayout) findViewById(R.id.players_down_layout);
		currentPlayerLayout = (LinearLayout) findViewById(R.id.current_player_layout);
		handLayout = (LinearLayout) findViewById(R.id.hand_layout);
		doneButton = (Button) findViewById(R.id.done_button);
		midMessageView = (TextView) findViewById(R.id.mid_message_view);

		loadUI();
	}

	/**
	 * Load player views for players, card views for current player's hand and
	 * show round view too.
	 */
	private void loadUI() {
		if (!table.debug) {
			updateTable(playerId);
		} else {
			currentPlayer = table.getCurrentPlayer();
		}
		doneButton.setEnabled(table.isMyTurn());

		// Load UI
		loadPlayers();
		loadHands();
		if (table.isPreGame())
			showPreGameMessage();
		else 
			showRound();
	}

	private void showPreGameMessage() {
		midMessageView.setVisibility(View.VISIBLE);
		midMessageView.setText("Waiting for players...");
	}

	@Override
	/**
	 * Warn player when back button pressed.
	 * Perform leave game if get positive result.
	 */
	public void onBackPressed() {
		if (table.debug) {
			finish();
			return;
		}

		// Dialog to leave match
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		alertDialogBuilder
				.setMessage("Do you want to hide this game or leave this game?");

		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Hide",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								GameActivity.this.finish();
							}
						})
				.setNegativeButton("Leave",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								if (table.isMyTurn()) {
									String nextParticipantId = table
											.getNextParticipantId();
									Games.TurnBasedMultiplayer
											.leaveMatchDuringTurn(
													getApiClient(),
													match.getMatchId(),
													nextParticipantId);
								} else
									Games.TurnBasedMultiplayer.leaveMatch(
											getApiClient(), match.getMatchId());
								finish();
							}
						});
		alertDialogBuilder.show();
	}

	/**
	 * Save table state and end current turn.
	 * 
	 * @param View
	 *            the done button.
	 */
	public void onDoneClicked(View view) {
		if (table.debug) {
			finish();
			return;
		}
		String nextParticipantId = table.getNextParticipantId();
		table.setPlayerThisTurn(table.getPlayerById(nextParticipantId));
		loadPlayers();

		Log.d("DoneButtonClicked", table.toString());
		// Update a match with new turn data
		Games.TurnBasedMultiplayer.takeTurn(getApiClient(), match.getMatchId(),
				xStream.toXML(table).getBytes(), nextParticipantId);
		view.setEnabled(false);
	}

	@Override
	public void onSignInFailed() {
		showWarning("Sign in failed",
				"Something wrong with your authentication, please sign in again.");
		finish();
	}

	@Override
	public void onSignInSucceeded() {
		Games.TurnBasedMultiplayer.registerMatchUpdateListener(getApiClient(),
				this);
	}

	/**
	 * Load cardViews for cards in current player's hand
	 */
	private void loadHands() {
		// Show Cards in hand
		handLayout.removeAllViews();
		for (Card card : currentPlayer.getHand()) {
			CardView cardView = new CardView(this);
			cardView.setCard(card);
			handLayout.addView(cardView);
		}
	}

	/**
	 * Load playerViews for players
	 */
	private void loadPlayers() {
		// Show players
		playersUpLayout.removeAllViews();
		playersDownLayout.removeAllViews();
		Integer count = 0;
		for (Player player : table.getPlayers()) {
			if (!player.equals(currentPlayer)) {
				PlayerView playerView = new PlayerView(this);
				playerView.setPlayer(player);
				if (count < 2)
					playersUpLayout.addView(playerView);
				else
					playersDownLayout.addView(playerView);
				count++;
			}
		}
		PlayerView currentPlayerView = new PlayerView(this);
		currentPlayerView.setPlayer(currentPlayer);
		currentPlayerLayout.removeAllViews();
		currentPlayerLayout.addView(currentPlayerView);
	}

	/**
	 * Show a round counter at the start of each turn. The round counter will
	 * fade in and fade out.
	 */
	private void showRound() {
		// Show Round
		midMessageView.setVisibility(View.VISIBLE);
		midMessageView.setAlpha(0f);
		midMessageView.setText("Round " + table.getRound());
		// Add a fade in fade out animation
		midMessageView.animate().alpha(1f).setDuration(2000)
				.setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						midMessageView.animate().alpha(0f).setDuration(2000)
								.setStartDelay(2000)
								.setListener(new AnimatorListenerAdapter() {
									@Override
									public void onAnimationEnd(
											Animator animation) {
										midMessageView.setVisibility(View.GONE);
									}
								});
					}
				});
	}

	/**
	 * Table setup. Set the current player on table and update turn counter.
	 * 
	 * @param playerId
	 *            The player ID
	 */
	private void updateTable(String playerId) {
		// Update Table data
		ArrayList<Player> players = new ArrayList<Player>();
		ArrayList<Participant> participants = match.getParticipants();
		for (Participant participant : participants)
			players.add(new Player(table, participant.getParticipantId(),
					participant.getDisplayName()));
		table.setPlayers(players);

		// Set current player
		String currnetParticipantId = match.getParticipantId(playerId);
		String currentPlayerName = match.getParticipant(currnetParticipantId)
				.getDisplayName();
		currentPlayer = new Player(table, currnetParticipantId,
				currentPlayerName);
		table.setCurrentPlayer(currentPlayer);
		table.setTurnCounter(table.getTurnCounter() + 1);
	}

	@Override
	/**
	 * Callback invoked when a new update to a match arrives.
	 * @param arg0
	 * 			  The match that was received
	 */
	public void onTurnBasedMatchReceived(TurnBasedMatch arg0) {
		String tableData = new String(match.getData());
		table = (Table) xStream.fromXML(tableData);
		Log.d("JustReceived", table.toString());
		if (match.getAvailableAutoMatchSlots() > 0) {
			Games.TurnBasedMultiplayer.takeTurn(getApiClient(),
					match.getMatchId(), match.getData(), null);
		}
		else if (table.isPreGame()) {
			if (isAllPlayersJoined(match))
				table.setPreGame(false);
			String nextParticipantId = table.getNextParticipantId();
			table.setPlayerThisTurn(table.getPlayerById(nextParticipantId));
			Games.TurnBasedMultiplayer.takeTurn(getApiClient(), match.getMatchId(),
					xStream.toXML(table).getBytes(), nextParticipantId);
		}		
		loadUI();
		Log.d("AfterLoadUI", table.toString());
	}

	private boolean isAllPlayersJoined(TurnBasedMatch match) {
		for (Participant participant : match.getParticipants()) {
			int status = participant.getStatus();
			if (status == Participant.STATUS_INVITED) {
				return false;
			}
		}
		return true;
	}

	@Override
	/**
	 * Callback invoked when a match has been removed from the local device.
	 * @param arg0
	 * 			  The ID of the match that has been removed
	 */
	public void onTurnBasedMatchRemoved(String arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * Show a Warning dialog.
	 * 
	 * @param title
	 *            The title of the dialog.
	 * @param message
	 *            The message to show in the dialog.
	 */
	public void showWarning(String title, String message) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle(title).setMessage(message);

		// set dialog message
		alertDialogBuilder.setCancelable(false).setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, maybe close current
						// activity
					}
				});

		// create and show alert dialog
		alertDialogBuilder.create().show();
	}
}
