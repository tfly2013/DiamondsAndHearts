package com.diamondshearts;

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
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.example.games.basegameutils.BaseGameActivity;
import com.google.gson.Gson;

public class GameActivity extends BaseGameActivity {

	private static final String TURN_BASED_MATCH_KEY = "com.diamondshearts.match";

	private LinearLayout playersUpLayout;
	private LinearLayout playersDownLayout;
	private LinearLayout currentPlayerLayout;
	private LinearLayout handLayout;
	private TextView roundView;

	private Table table;
	private Player currentPlayer;
	private TurnBasedMatch match;

	private Gson gson;

	@Override
	protected void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.activity_game);

		gson = new Gson();
		if (getIntent().hasExtra(TURN_BASED_MATCH_KEY)
				&& (match = getIntent()
						.getParcelableExtra(TURN_BASED_MATCH_KEY)) != null) {
			String tableData = new String(match.getData());
			table = gson.fromJson(tableData, Table.class);
		} else {
			// ERROR!!
			table = new Table("test");
		}

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
						roundView.animate().alpha(0f).setDuration(2000).setStartDelay(2000)
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
	public void onBackPressed() {

		if (table.getCurrentPlayer().getName().equals("TestPlayerName")) {
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
					}
				});
		alertDialogBuilder.show();
	}

	public void onDoneClicked(View view) {

		if (table.getCurrentPlayer().getName().equals("TestPlayerName")) {
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
