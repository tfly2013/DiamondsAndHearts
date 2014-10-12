package com.diamondshearts;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diamondshearts.models.Card;
import com.diamondshearts.models.EventType;
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

	private LinearLayout cardPlayedLayout;

	private Button drawButton;

	private Button skipButton;

	/** The table state of game */
	private Table table;

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

		// Set up layouts
		playersUpLayout = (LinearLayout) findViewById(R.id.players_up_layout);
		playersDownLayout = (LinearLayout) findViewById(R.id.players_down_layout);
		currentPlayerLayout = (LinearLayout) findViewById(R.id.current_player_layout);
		handLayout = (LinearLayout) findViewById(R.id.hand_layout);
		midMessageView = (TextView) findViewById(R.id.mid_message_view);
		cardPlayedLayout = (LinearLayout) findViewById(R.id.card_played_layout);
		drawButton = (Button) findViewById(R.id.draw_button);
		skipButton = (Button) findViewById(R.id.skip_button);
		
	}

	public void finishTurn() {
		if (table.debug) {
			finish();
			return;
		}
		//Extra Turn: set the next participant the current one
		String nextParticipantId;
		Player playerThisTurn = table.getPlayerThisTurn(); 
		if(!playerThisTurn.getEventsActivated().get(EventType.ExtraTurn)){
			nextParticipantId = table.getNextParticipantId();
		}else{
			nextParticipantId = playerThisTurn.getId();
			playerThisTurn.getEventsActivated().put(EventType.ExtraTurn, false);
		}
		//Extra Card: obtain an extra card at end of the turn
		if(playerThisTurn.getEventsActivated().get(EventType.ExtraCard)){
			playerThisTurn.getHand().add(Card.draw());
			playerThisTurn.getEventsActivated().put(EventType.ExtraCard, false);
		}
		table.setPlayerThisTurn(table.getPlayerById(nextParticipantId));
		loadPlayers();
		loadCardPlayed();
		// Update a match with new turn data
		Games.TurnBasedMultiplayer.takeTurn(getApiClient(), match.getMatchId(),
				xStream.toXML(table).getBytes(), nextParticipantId);
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
		alertDialogBuilder.setPositiveButton("Hide",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						GameActivity.this.finish();
					}
				}).setNegativeButton("Leave",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						if (table.isMyTurn()) {
							String nextParticipantId = table
									.getNextParticipantId();
							Games.TurnBasedMultiplayer.leaveMatchDuringTurn(
									getApiClient(), match.getMatchId(),
									nextParticipantId);
						} else
							Games.TurnBasedMultiplayer.leaveMatch(
									getApiClient(), match.getMatchId());
						finish();
					}
				});
		alertDialogBuilder.show();
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
		if (!table.debug)
			checkPreGame(match);
		updateUI();
	}

	@Override
	/**
	 * Callback invoked when a new update to a match arrives.
	 * @param match
	 * 			  The match that was received
	 */
	public void onTurnBasedMatchReceived(TurnBasedMatch match) {
		this.match = match;
		String tableData = new String(match.getData());
		table = (Table) xStream.fromXML(tableData);
		checkPreGame(match);
		updateUI();
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

	public void showMessage(String string, long duration) {
		midMessageView.setVisibility(View.VISIBLE);
		midMessageView.setAlpha(0);
		midMessageView.setText(string);
		midMessageView.animate().alpha(1).setDuration(1000)
				.setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						midMessageView.animate().alpha(0).setDuration(1000)
								.setStartDelay(1000)
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

	/**
	 * @param match
	 */
	private void checkPreGame(TurnBasedMatch match) {
		if (match.getAvailableAutoMatchSlots() > 0) {
			Games.TurnBasedMultiplayer.takeTurn(getApiClient(),
					match.getMatchId(), match.getData(), null);
		} else if (table.isPreGame()) {
			if (isAllPlayersJoined(match))
				table.setPreGame(false);
			String nextParticipantId = table.getNextParticipantId();
			table.setPlayerThisTurn(table.getPlayerById(nextParticipantId));
			Games.TurnBasedMultiplayer.takeTurn(getApiClient(),
					match.getMatchId(), xStream.toXML(table).getBytes(),
					nextParticipantId);
		}
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

	private void loadCardPlayed() {
		cardPlayedLayout.removeAllViews();
		for (int i = 0; i < table.getCardPlayed().size(); i++) {
			CardView cardView = new CardView(this);
			cardView.setCard(table.getCardPlayed().get(i));
			cardPlayedLayout.addView(cardView);
		}
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
	 * Load player views for players, card views for current player's hand and
	 * show round view too.
	 */
	private void updateUI() {
		if (!table.debug) {
			String currnetParticipantId = match.getParticipantId(Games.Players
					.getCurrentPlayerId(getApiClient()));
			currentPlayer = table.getPlayerById(currnetParticipantId);
			table.setCurrentPlayer(currentPlayer);
			table.setTurnCounter(table.getTurnCounter() + 1);
		} else {
			currentPlayer = table.getCurrentPlayer();
		}
		if (table.isMyTurn()) {
			drawButton.setVisibility(View.VISIBLE);
			skipButton.setVisibility(View.VISIBLE);
			//Skip Turn: current player's turn is forced to be skipped
			if(currentPlayer.getEventsActivated().get(EventType.SkipTurn)){
				currentPlayer.getEventsActivated().put(EventType.SkipTurn, false);
				finishTurn();
			}
		}
		else {
			drawButton.setVisibility(View.GONE);
			skipButton.setVisibility(View.GONE);
		}

		// Load UI
		loadPlayers();
		loadCardPlayed();
		loadHands();
		if (table.isPreGame()) {
			midMessageView.setVisibility(View.VISIBLE);
			midMessageView.setText("Waiting for players...");
		} else
			showMessage("Round " + table.getRound(), 2000);
	}

	public void onDrawButtonClicked(View view) {
		if (currentPlayer.canAfford(3)) {
			currentPlayer.setDiamond(currentPlayer.getDiamond() - 3);
			currentPlayer.getHand().add(Card.draw());
			loadHands();
			showMessage("I spend 3 diamond to draw a card.", 1000);
		} else {
			showMessage("I cant afford this.", 1000);
		}
	}

	public void onSkipButtonClicked(View view) {
		currentPlayer.setDiamond(currentPlayer.getDiamond() + 4);
		finishTurn();
		showMessage("I gain 4 diamond for skipping my turn.", 1000);
	}
}
