package com.diamondshearts;

import android.os.Bundle;

import com.google.example.games.basegameutils.BaseGameActivity;

public class GameActivity extends BaseGameActivity {

	@Override
	protected void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.activity_game);
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
