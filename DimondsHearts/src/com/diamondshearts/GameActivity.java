package com.diamondshearts;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.diamondshearts.models.Card;
import com.diamondshearts.models.Player;
import com.diamondshearts.models.Table;
import com.google.example.games.basegameutils.BaseGameActivity;

public class GameActivity extends BaseGameActivity {

	private Table table;
	private LinearLayout playersUpLayout;
	private LinearLayout playersDownLayout;
	private LinearLayout handLayout;
	private Player currentPlayer;

	@Override
	protected void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.activity_game);
		playersUpLayout = (LinearLayout)findViewById(R.id.players_up_layout);
		playersDownLayout = (LinearLayout)findViewById(R.id.players_down_layout);
		handLayout = (LinearLayout)findViewById(R.id.hand_layout);
		table = new Table();
		currentPlayer = table.getCurrentPlayer();
		for (int i = 1; i< table.getPlayers().size(); i++){
			PlayerView playerView = new PlayerView(this);
			playerView.setPlayer(table.getPlayers().get(i));
			if (i < 3)
				playersUpLayout.addView(playerView);
			else
				playersDownLayout.addView(playerView);
		}
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
