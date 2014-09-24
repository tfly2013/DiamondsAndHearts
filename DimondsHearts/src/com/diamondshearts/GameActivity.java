package com.diamondshearts;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diamondshearts.models.Card;
import com.diamondshearts.models.Player;
import com.diamondshearts.models.Table;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.example.games.basegameutils.BaseGameActivity;
import com.google.gson.Gson;

public class GameActivity extends BaseGameActivity {
	/** Player Layout (Up) */
	private LinearLayout playersUpLayout;
	/** Player Layout (Down) */
	private LinearLayout playersDownLayout;
	/** Current Player Layout */
	private LinearLayout currentPlayerLayout;
	/** Layout of current player's hand */
	private LinearLayout handLayout;
	/** The text view to show round */
	private TextView roundView;

	/** The table state of game */
	private Table table;
	/** Current Player */
	private Player currentPlayer;
	/** The match */
	private TurnBasedMatch match;
	/** The Gson object */
	private Gson gson;

	@Override
	/**
	 * Initialize the activity and match data.
	 */
	protected void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.activity_game);

		// Retrive match data from MainActivity
		gson = new Gson();
		String matchKey = "com.diamondshearts.match";
		if (getIntent().hasExtra(matchKey)
				&& (match = getIntent().getParcelableExtra(matchKey)) != null) {
			String tableData = new String(match.getData());
			table = gson.fromJson(tableData, Table.class);
		} else {
			// ERROR!!
			table = new Table(true);
		}
		String playerId = null;
		String playerIdKey = "com.diamondshearts.playerid";
		if (getIntent().hasExtra(playerIdKey))
			playerId = getIntent().getStringExtra(playerIdKey);

		if (!table.debug) {
			// Update Table data
			ArrayList<Player> players = new ArrayList<Player>();
			ArrayList<Participant> participants = match.getParticipants();
			for (Participant participant : participants)
				players.add(new Player(table, participant.getParticipantId(),
						participant.getDisplayName()));
			table.setPlayers(players);

			// Set current player
			String currnetParticipantId = match.getParticipantId(playerId);
			String currentPlayerName = match.getParticipant(
					currnetParticipantId).getDisplayName();
			table.setCurrentPlayer(new Player(table, currnetParticipantId,
					currentPlayerName));
			table.setTurnCounter(table.getTurnCounter() + 1);
		}		

		// Get layouts
		playersUpLayout = (LinearLayout) findViewById(R.id.players_up_layout);
		playersDownLayout = (LinearLayout) findViewById(R.id.players_down_layout);
		currentPlayerLayout = (LinearLayout) findViewById(R.id.current_player_layout);
		handLayout = (LinearLayout) findViewById(R.id.hand_layout);
		currentPlayer = table.getCurrentPlayer();

		// Show players
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
		currentPlayerLayout.addView(currentPlayerView);

		// Show Cards in hand
		for (Card card : currentPlayer.getHand()) {
			CardView cardView = new CardView(this);
			cardView.setCard(card);
			handLayout.addView(cardView);
		}

		// Show Round
		roundView = (TextView) findViewById(R.id.round_view);
		roundView.setVisibility(View.VISIBLE);
		roundView.setAlpha(0f);
		roundView.setText("Round " + table.getRound());
		// Add a fade in fade out animation
		roundView.animate().alpha(1f).setDuration(2000)
				.setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						roundView.animate().alpha(0f).setDuration(2000)
								.setStartDelay(2000)
								.setListener(new AnimatorListenerAdapter() {
									@Override
									public void onAnimationEnd(
											Animator animation) {
										roundView.setVisibility(View.GONE);
									}
								});
					}
				});
	}

	@Override
	public void onSignInFailed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSignInSucceeded() {
		// TODO Auto-generated method stub

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

		alertDialogBuilder.setMessage("Are you sure to leave this game?");

		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								// Leave Match
								String nextParticipantId = table
										.getNextParticipantId(match
												.getAvailableAutoMatchSlots());
								Games.TurnBasedMultiplayer
										.leaveMatchDuringTurn(getApiClient(),
												match.getMatchId(),
												nextParticipantId);
								finish();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// Do Nothing
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

		String nextParticipantId = table.getNextParticipantId(match
				.getAvailableAutoMatchSlots());

		// Create the next turn
		Games.TurnBasedMultiplayer.takeTurn(getApiClient(), match.getMatchId(),
				gson.toJson(table, Table.class).getBytes(), nextParticipantId);
		finish();
	}
}
