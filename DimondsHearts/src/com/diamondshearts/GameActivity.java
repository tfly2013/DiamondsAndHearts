package com.diamondshearts;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.diamondshearts.models.Card;
import com.diamondshearts.models.Player;
import com.diamondshearts.models.Table;
import com.google.example.games.basegameutils.BaseGameActivity;

public class GameActivity extends BaseGameActivity {

	private Table table;
	// private LinearLayout playersLayout;
	private LinearLayout handLayout;
	private Player currentPlayer;

	@Override
	protected void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.activity_game);
		// playersLayout = (LinearLayout)findViewById(R.id.players_layout);
		handLayout = (LinearLayout)findViewById(R.id.hand_layout);
		table = new Table();
		currentPlayer = table.getCurrentPlayer();
		for (Player player : table.getPlayers())
			getSupportFragmentManager().beginTransaction()
					.add(R.id.players_layout, new PlayerFragment(player))
					.commit();
		for (Card card : currentPlayer.getHand()){
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
		// Dialog to leave match
		super.onBackPressed();
	}

}
