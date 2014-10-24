package com.diamondshearts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diamondshearts.models.Card;
import com.diamondshearts.models.EventType;
import com.diamondshearts.models.Player;
import com.diamondshearts.models.Table;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.ParticipantResult;
import com.google.android.gms.games.multiplayer.turnbased.OnTurnBasedMatchUpdateReceivedListener;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.UpdateMatchResult;
import com.google.example.games.basegameutils.BaseGameActivity;
import com.thoughtworks.xstream.XStream;

/**
 * GameActivity handles the player views, card views, round views, the layouts
 * as well as how to exit a game and how to finish a game
 * 
 * @author Fei Tang & Kimple Ke
 * */
public class GameActivity extends BaseGameActivity implements
		OnTurnBasedMatchUpdateReceivedListener {

	private class PlayerViewDragListener implements View.OnDragListener {
		@Override
		public boolean onDrag(View v, DragEvent event) {
			switch (event.getAction()) {
			// Signals the start of a drag and drop operation of a card.
			case DragEvent.ACTION_DRAG_STARTED:
				if (event.getLocalState().getClass() == CardView.class) {
					soundPool.play(tapSoundID, 1, 1, 1, 0, 1);
					return true;
				}
				return false;
				// Signals to a View that the drag point has entered the
				// bounding box of the player View.
			case DragEvent.ACTION_DRAG_ENTERED:
				PlayerView playerView = (PlayerView) v;
				playerView.setBorderColor(Color.GREEN);
				v.invalidate();
				return true;
				// Sent to a View after ACTION_DRAG_ENTERED if the drag
				// shadow is still within the player View's bounding box.
			case DragEvent.ACTION_DRAG_LOCATION:
				return true;
				// Signals that the user has moved the drag shadow
				// outside the bounding box of the player View.
			case DragEvent.ACTION_DRAG_EXITED:
				playerView = (PlayerView) v;
				playerView.resetColor();
				v.invalidate();
				return true;
				// Signals to a View that the user has released the drag
				// shadow, and the drag point is within the bounding box of the
				// player View.
			case DragEvent.ACTION_DROP:
				playerView = (PlayerView) v;
				CardView cardView = (CardView) event.getLocalState();
				onDrop(playerView, cardView);
				return true;
				// Signals to a View that the drag and drop operation
				// has concluded.
			case DragEvent.ACTION_DRAG_ENDED:
				cardView = (CardView) event.getLocalState();
				cardView.setVisibility(View.VISIBLE);
				soundPool.play(untapSoundID, 1, 1, 1, 0, 1);
			}
			return false;
		}

		/**
		 * @param player
		 * @return If the match is finished.
		 */
		private boolean checkFinished(Player player) {
			if (player.getHeart() <= 0) {
				player.setAlive(false);
				int alivedPlayers = table.countAlivedPlayers();
				ParticipantResult result = new ParticipantResult(
						player.getId(), ParticipantResult.MATCH_RESULT_LOSS,
						alivedPlayers + 1);
				ArrayList<ParticipantResult> results = player.getTable()
						.getResults();
				results.add(result);

				if (alivedPlayers <= 1) {
					Player winner = null;
					for (Player p : table.getPlayers()) {
						if (p.isAlive())
							winner = p;
					}
					if (winner != null)
						results.add(new ParticipantResult(winner.getId(),
								ParticipantResult.MATCH_RESULT_WIN, 1));
					showScoreBoard();
					Games.TurnBasedMultiplayer
							.finishMatch(getApiClient(), match.getMatchId(),
									xStream.toXML(table).getBytes(),
									table.getResults()).setResultCallback(
									new ResultCallback<UpdateMatchResult>() {
										@Override
										public void onResult(
												UpdateMatchResult result) {
											if (match != null) {
												match = result.getMatch();
												checkMatchStatus(match);
											}
										}
									});
					return true;
				}
			}
			return false;
		}

		/**
		 * @param playerView
		 * @param cardView
		 */
		private void onDrop(PlayerView playerView, CardView cardView) {
			Card card = cardView.getCard();
			Card.PlayResult result = card.play(playerView.getPlayer());
			switch (result) {
			case OK:
				Player playerThisTurn = card.getOwner();
				Player target = playerView.getPlayer();
				playerThisTurn.getHand().remove(card);
				((ViewGroup) cardView.getParent()).removeView(cardView);
				Table table = target.getTable();
				table.setPlayerLastHit(target);
				ArrayList<Card> cardPlayed = table.getCardPlayed();
				cardPlayed.add(card);
				if (cardPlayed.size() > 10)
					cardPlayed.remove(0);
				soundPool.play(knockSoundID, 1, 1, 1, 0, 1);
				if (checkFinished(target))
					break;
				if (checkFinished(playerThisTurn))
					break;
				card.setOwner(null);
				finishTurn();
				break;
			case CantAfford:
				soundPool.play(errorSoundID, 1, 1, 1, 0, 1);
				showMessage("I cant Afford this.", 1000);
				break;
			case NeedTarget:
				soundPool.play(errorSoundID, 1, 1, 1, 0, 1);
				showMessage("This card need a enmey target.", 1000);
				break;
			case SelfTarget:
				soundPool.play(errorSoundID, 1, 1, 1, 0, 1);
				showMessage("This card can only target yourself.", 1000);
				break;

			}
			playerView.resetColor();
			playerView.invalidate();
		}

	}

	/** Player Layout */
	private LinearLayout playersLayout;

	/** Current Player Layout */
	private LinearLayout currentPlayerLayout;

	private RelativeLayout scoreboardLayout;

	private HorizontalScrollView handScrollView;

	/** Layout of current player's hand */
	private LinearLayout handLayout;

	/** The text view to show round */
	private TextView midMessageView;

	private LinearLayout cardPlayedLayout;

	private HorizontalScrollView cardplayedScrollView;

	private LinearLayout rankingLayout;

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

	private SoundPool soundPool;
	private int errorSoundID;
	private int drawSoundID;
	private int knockSoundID;
	private int nextTurnSoundID;
	private int skipSoundID;
	private int tapSoundID;
	private int untapSoundID;

	@Override
	/**
	 * Initialize the activity and match data.
	 * @param b
	 * 		   The bundle
	 */
	protected void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.activity_game);

		// Set up layouts
		playersLayout = (LinearLayout) findViewById(R.id.players_layout);
		currentPlayerLayout = (LinearLayout) findViewById(R.id.current_player_layout);
		scoreboardLayout = (RelativeLayout) findViewById(R.id.scoreboard_layout);
		handScrollView = (HorizontalScrollView) findViewById(R.id.hand_scroll_view);
		handLayout = (LinearLayout) findViewById(R.id.hand_layout);
		midMessageView = (TextView) findViewById(R.id.mid_message_view);
		cardplayedScrollView = (HorizontalScrollView) findViewById(R.id.card_played_scroll_view);
		cardPlayedLayout = (LinearLayout) findViewById(R.id.card_played_layout);
		rankingLayout = (LinearLayout) findViewById(R.id.ranking_layout);
		drawButton = (Button) findViewById(R.id.draw_button);
		skipButton = (Button) findViewById(R.id.skip_button);
		soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		drawSoundID = soundPool.load(this, R.raw.draw, 1);
		errorSoundID = soundPool.load(this, R.raw.error, 1);
		knockSoundID = soundPool.load(this, R.raw.knock, 1);
		nextTurnSoundID = soundPool.load(this, R.raw.next_turn, 1);
		skipSoundID = soundPool.load(this, R.raw.skip, 1);
		tapSoundID = soundPool.load(this, R.raw.tap, 1);
		untapSoundID = soundPool.load(this, R.raw.untap, 1);
	}

	/**
	 * Called when a turn is finish
	 * */
	public void finishTurn() {
		if (table.debug) {
			finish();
			return;
		}
		// Extra Turn: set the next participant the current one
		String nextParticipantId;
		Player playerThisTurn = table.getPlayerThisTurn();
		if (playerThisTurn.isAlive()
				&& playerThisTurn.getEffects().get(EventType.ExtraTurn)) {
			nextParticipantId = playerThisTurn.getId();
			playerThisTurn.getEffects().put(EventType.ExtraTurn, false);

		} else {
			nextParticipantId = table.getNextParticipantId();
		}
		// Extra Card: obtain an extra card at end of the turn
		if (playerThisTurn.isAlive()
				&& playerThisTurn.getEffects().get(EventType.ExtraCard)) {
			playerThisTurn.getHand().add(Card.draw(playerThisTurn));
			playerThisTurn.getEffects().put(EventType.ExtraCard, false);
		}
		table.setPlayerThisTurn(table.getPlayerById(nextParticipantId));
		updateUI();
		table.setCardDrawed(0);
		// Update a match with new turn data
		Games.TurnBasedMultiplayer.takeTurn(getApiClient(), match.getMatchId(),
				xStream.toXML(table).getBytes(), nextParticipantId)
				.setResultCallback(new ResultCallback<UpdateMatchResult>() {
					@Override
					public void onResult(UpdateMatchResult result) {
						if (match != null) {
							match = result.getMatch();
							checkMatchStatus(match);
						}
					}
				});
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

	/**
	 * Press draw button to buy a card
	 * 
	 * @param view
	 *            The view pressed
	 * */
	public void onDrawButtonClicked(View view) {
		if (table.getCardDrawed() >= 3) {
			soundPool.play(errorSoundID, 1, 1, 1, 0, 1);
			showMessage("Cant draw more card this turn.", 1000);
			return;
		}
		if (currentPlayer.canAfford(3)) {
			currentPlayer.setDiamond(currentPlayer.getDiamond() - 3);
			currentPlayer.getHand().add(Card.draw(currentPlayer));
			table.setCardDrawed(table.getCardDrawed() + 1);
			loadCurrentPlayer();
			loadHands();
			handScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
			soundPool.play(drawSoundID, 1, 1, 1, 0, 1);
			showMessage("Spend 3 diamond to draw a card.", 1000);
		} else {
			soundPool.play(errorSoundID, 1, 1, 1, 0, 1);
			showMessage("I cant afford this.", 1000);
		}
	}

	public void onScoreboardDoneButtonClicked(View view) {
		scoreboardLayout.setVisibility(View.GONE);
		this.finish();
	}

	@Override
	/**
	 * Show warning for failing sign-in
	 * */
	public void onSignInFailed() {
		showWarning(
				"Sign in failed",
				"Something wrong with your authentication, please sign in again.",
				true);
		finish();
	}

	@Override
	/**
	 * Sign-in succeeded, and update match
	 * */
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
		if (!table.debug) {
			checkPreGame(match);
			checkMatchStatus(match);
		}
		updateUI();
	}

	/**
	 * Press skip button to skip a turn
	 * 
	 * @param view
	 *            The view pressed
	 * */
	public void onSkipButtonClicked(View view) {
		currentPlayer.setDiamond(currentPlayer.getDiamond() + 4);
		loadCurrentPlayer();
		finishTurn();
		soundPool.play(skipSoundID, 1, 1, 1, 0, 1);
		showMessage("Gain 4 diamond for skipping turn.", 1000);
	}

	@Override
	/**
	 * Callback invoked when a new update to a match arrives.
	 * @param match
	 * 			  The match that was received
	 */
	public void onTurnBasedMatchReceived(TurnBasedMatch match) {
		if (match.getMatchId().equals(this.match.getMatchId())) {
			this.match = match;
			String tableData = new String(match.getData());
			table = (Table) xStream.fromXML(tableData);
			checkPreGame(match);
			checkMatchStatus(match);
			soundPool.play(nextTurnSoundID, 1, 1, 1, 0, 1);
			updateUI();
		}
	}

	@Override
	/**
	 * Callback invoked when a match has been removed from the local device.
	 * @param arg0
	 * 			  The ID of the match that has been removed
	 */
	public void onTurnBasedMatchRemoved(String arg0) {

	}

	/**
	 * Animating a message for a while in game activity
	 * 
	 * @param string
	 *            The message to display
	 * @param duration
	 *            Time period
	 * */
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
	public void showWarning(String title, String message, final boolean finish) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle(title).setMessage(message);

		// set dialog message
		alertDialogBuilder.setCancelable(false).setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						if (finish)
							GameActivity.this.finish();
					}
				});

		// create and show alert dialog
		alertDialogBuilder.create().show();
	}

	private void checkMatchStatus(TurnBasedMatch match2) {
		int status = match.getStatus();
		switch (status) {
		case TurnBasedMatch.MATCH_STATUS_CANCELED:
			showWarning("Canceled!", "This game is canceled!", true);
			return;
		case TurnBasedMatch.MATCH_STATUS_EXPIRED:
			showWarning("Expired!", "This game is expired!", true);
			return;
		case TurnBasedMatch.MATCH_STATUS_COMPLETE:
			showScoreBoard();
			return;
		}
	}

	private void showScoreBoard() {
		if (scoreboardLayout.getVisibility() == View.GONE) {
			scoreboardLayout.setVisibility(View.VISIBLE);
			rankingLayout.removeAllViews();
			ArrayList<ParticipantResult> results = table.getResults();
			Collections.sort(results, new Comparator<ParticipantResult>() {
				@Override
				public int compare(ParticipantResult lhs, ParticipantResult rhs) {
					return ((Integer) lhs.getPlacing()).compareTo(rhs
							.getPlacing());
				}
			});
			for (ParticipantResult result : results) {
				TextView resultTextView = new TextView(this);
				Player player = table.getPlayerById(result.getParticipantId());
				resultTextView.setTextAppearance(this, R.style.MainText);
				resultTextView.setText(result.getPlacing() + "  "
						+ player.getName());
				if (player.equals(currentPlayer))
					resultTextView.setTextColor(Color.RED);
				rankingLayout.addView(resultTextView);
			}
		}
	}

	/**
	 * Check if all players joined, if so, set the current player to to take
	 * turn and start the game.
	 * 
	 * @param match
	 *            The turn based match
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

	/**
	 * Check if all players have joined the game
	 * 
	 * @param match
	 *            Turn base match
	 * @return false/true True if all players joined
	 * */
	private boolean isAllPlayersJoined(TurnBasedMatch match) {
		for (Participant participant : match.getParticipants()) {
			int status = participant.getStatus();
			if (status == Participant.STATUS_INVITED) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Load all cards have been played and add them into cardPlayedLayout
	 * */
	private void loadCardPlayed() {
		cardPlayedLayout.removeAllViews();
		for (int i = 0; i < table.getCardPlayed().size(); i++) {
			CardView cardView = new CardView(this);
			cardView.setCard(table.getCardPlayed().get(i));
			cardPlayedLayout.addView(cardView);
		}
		cardplayedScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
	}

	/**
	 * Load playerViews for current player
	 */
	private void loadCurrentPlayer() {
		PlayerView currentPlayerView = new PlayerView(this);
		if (currentPlayer.isAlive())
			currentPlayerView.setOnDragListener(new PlayerViewDragListener());
		else
			currentPlayerView.setOnDragListener(null);
		currentPlayerView.setPlayer(currentPlayer);
		currentPlayerLayout.removeAllViews();
		currentPlayerLayout.addView(currentPlayerView);
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
		playersLayout.removeAllViews();
		Integer count = 0;
		for (Player player : table.getPlayers()) {
			if (!player.equals(currentPlayer)) {
				PlayerView playerView = new PlayerView(this);
				if (player.isAlive())
					playerView.setOnDragListener(new PlayerViewDragListener());
				else
					playerView.setOnDragListener(null);
				playerView.setPlayer(player);
				playersLayout.addView(playerView);
				count++;
			}
		}
		loadCurrentPlayer();
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
			if (!currentPlayer.isAlive())
				finishTurn();
			// Skip Turn: current player's turn is forced to be skipped
			else if (!table.debug
					&& currentPlayer.getEffects().get(EventType.SkipTurn)) {
				currentPlayer.getEffects().put(EventType.SkipTurn, false);
				finishTurn();
			} else {
				drawButton.setVisibility(View.VISIBLE);
				skipButton.setVisibility(View.VISIBLE);
			}
		} else {
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
}
