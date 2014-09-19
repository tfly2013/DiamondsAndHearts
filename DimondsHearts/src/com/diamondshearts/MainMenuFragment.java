package com.diamondshearts;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainMenuFragment extends Fragment {
	
	private MainActivity activity;
	final static int RC_SELECT_PLAYERS = 10000;
	final static int RC_LOOK_AT_MATCHES = 10001;
	
	public MainMenuFragment() {
		this.activity = (MainActivity)getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main_menu, container,
				false);
		return rootView;
	}
	

	// Displays all ongoing games
	public void onCheckGamesClicked(View view) {
		Intent intent = Games.TurnBasedMultiplayer
				.getInboxIntent(activity.apiAgent);
		startActivityForResult(intent, RC_LOOK_AT_MATCHES);
	}

	// Open the player invitation UI
	public void onNewGameClicked(View view) {
		Intent intent = Games.TurnBasedMultiplayer.getSelectOpponentsIntent(activity.apiAgent, 1, 5, true);
		startActivityForResult(intent, RC_SELECT_PLAYERS);
	}

	// Create a one-on-one auto-match game (2 players game)
	public void onQuickGameClicked(View view) {

		Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(1, 1, 0);

		TurnBasedMatchConfig tbmc = TurnBasedMatchConfig.builder()
				.setAutoMatchCriteria(autoMatchCriteria).build();

		activity.showSpinner();

		// Start the match
		ResultCallback<TurnBasedMultiplayer.InitiateMatchResult> cb = new ResultCallback<TurnBasedMultiplayer.InitiateMatchResult>() {
			@Override
			public void onResult(TurnBasedMultiplayer.InitiateMatchResult result) {
				activity.processResult(result);
			}
		};
		Games.TurnBasedMultiplayer.createMatch(activity.apiAgent, tbmc)
				.setResultCallback(cb);
	}
}
