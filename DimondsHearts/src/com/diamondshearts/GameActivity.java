package com.diamondshearts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.diamondshearts.models.Card;
import com.diamondshearts.models.Player;
import com.diamondshearts.models.Table;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.example.games.basegameutils.BaseGameActivity;
import com.google.gson.Gson;

public class GameActivity extends BaseGameActivity {

	private static final String TURN_BASED_MATCH_KEY = "com.diamondshearts.match";

	private Table table;
	private LinearLayout playersUpLayout;
	private LinearLayout playersDownLayout;
	private LinearLayout currentPlayerLayout;
	private LinearLayout handLayout;
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
		for (int i = 1; i < table.getPlayers().size(); i++) {
			PlayerView playerView = new PlayerView(this);
			playerView.setPlayer(table.getPlayers().get(i));
			if (i < 3)
				playersUpLayout.addView(playerView);
			else
				playersDownLayout.addView(playerView);
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
		
		if (table.getCurrentPlayer().getName().equals("TestPlayerName")){
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
		
		if (table.getCurrentPlayer().getName().equals("TestPlayerName")){
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
