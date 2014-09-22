package com.diamondshearts;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.turnbased.OnTurnBasedMatchUpdateReceivedListener;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer;
import com.google.example.games.basegameutils.BaseGameActivity;

public class MainActivity extends BaseGameActivity implements
		OnInvitationReceivedListener, OnTurnBasedMatchUpdateReceivedListener {
	public static final String TAG = "DrawingActivity";

	// Local convenience pointers
	public TextView dataView;
	public TextView turnCounterView;
	public GoogleApiClient apiAgent;

	private AlertDialog alertDialog;

	// Intents handling
	final static int RC_SELECT_PLAYERS = 10000;
	final static int RC_LOOK_AT_MATCHES = 10001;

	// How long to show toasts.
	final static int TOAST_DELAY = 2000;

	// whether showing the turn UI or not
	public boolean isDoingTurn = false;

	// The current match, null if not loaded
	public TurnBasedMatch match;

	// This is the current match data after being unpersisted.
	// Do not retain references to match data once you have
	// taken an action on the match, such as takeTurn()
	public Turn turnData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		apiAgent = getApiClient();
		// Setup sign in and sign out button
		findViewById(R.id.sign_out_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						signOut();
						setViewVisibility();
					}
				});

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						beginUserInitiatedSignIn();
						findViewById(R.id.sign_in_button).setVisibility(
								View.GONE);

					}
				});
	}

	// Displays all ongoing games
	public void onCheckGamesClicked(View view) {
		Intent intent = Games.TurnBasedMultiplayer
				.getInboxIntent(apiAgent);
		startActivityForResult(intent, RC_LOOK_AT_MATCHES);
	}

	// Open the player invitation UI
	public void onNewGameClicked(View view) {
		Intent intent = Games.TurnBasedMultiplayer.getSelectOpponentsIntent(apiAgent, 1, 5, true);
		startActivityForResult(intent, RC_SELECT_PLAYERS);
	}

	// Create a one-on-one auto-match game (2 players game)
	public void onQuickGameClicked(View view) {
		
		Intent intent = new Intent(this,GameActivity.class);
		startActivity(intent);
//		Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(1, 1, 0);
//
//		TurnBasedMatchConfig tbmc = TurnBasedMatchConfig.builder()
//				.setAutoMatchCriteria(autoMatchCriteria).build();
//
//		showSpinner();
//
//		// Start the match
//		ResultCallback<TurnBasedMultiplayer.InitiateMatchResult> cb = new ResultCallback<TurnBasedMultiplayer.InitiateMatchResult>() {
//			@Override
//			public void onResult(TurnBasedMultiplayer.InitiateMatchResult result) {
//				processResult(result);
//			}
//		};
//		Games.TurnBasedMultiplayer.createMatch(apiAgent, tbmc)
//				.setResultCallback(cb);
	}

	// In-game controls

	// Cancel the game. Should possibly wait until the game is canceled before
	// giving up on the view.
	public void onCancelClicked(View view) {
		showSpinner();
		Games.TurnBasedMultiplayer.cancelMatch(apiAgent,
				match.getMatchId()).setResultCallback(
				new ResultCallback<TurnBasedMultiplayer.CancelMatchResult>() {
					@Override
					public void onResult(
							TurnBasedMultiplayer.CancelMatchResult result) {
						processResult(result);
					}
				});
		isDoingTurn = false;
		setViewVisibility();
	}

	// Leave the game during your turn. Note that there is a separate
	// Games.TurnBasedMultiplayer.leaveMatch() if you want to leave NOT on your
	// turn.
	public void onLeaveClicked(View view) {
		showSpinner();
		String nextParticipantId = getNextParticipantId();

		Games.TurnBasedMultiplayer.leaveMatchDuringTurn(apiAgent,
				match.getMatchId(), nextParticipantId).setResultCallback(
				new ResultCallback<TurnBasedMultiplayer.LeaveMatchResult>() {
					@Override
					public void onResult(
							TurnBasedMultiplayer.LeaveMatchResult result) {
						processResult(result);
					}
				});
		setViewVisibility();
	}

	// Finish the game. Sometimes, this is your only choice.
	public void onFinishClicked(View view) {
		showSpinner();
		Games.TurnBasedMultiplayer.finishMatch(apiAgent,
				match.getMatchId()).setResultCallback(
				new ResultCallback<TurnBasedMultiplayer.UpdateMatchResult>() {
					@Override
					public void onResult(
							TurnBasedMultiplayer.UpdateMatchResult result) {
						processResult(result);
					}
				});

		isDoingTurn = false;
		setViewVisibility();
	}

	// Upload your new gamestate, then take a turn, and pass it on to the next
	// player.
	public void onDoneClicked(View view) {
		showSpinner();

		String nextParticipantId = getNextParticipantId();
		// Create the next turn
		turnData.turnCounter += 1;
		turnData.data = dataView.getText().toString();

		showSpinner();

		Games.TurnBasedMultiplayer
				.takeTurn(apiAgent, match.getMatchId(),
						turnData.persist(), nextParticipantId)
				.setResultCallback(
						new ResultCallback<TurnBasedMultiplayer.UpdateMatchResult>() {
							@Override
							public void onResult(
									TurnBasedMultiplayer.UpdateMatchResult result) {
								processResult(result);
							}
						});

		turnData = null;
	}

	// Sign-in, Sign out behavior
	// Update the visibility based on what state we're in.
	public void setViewVisibility() {
		if (!isSignedIn()) {
			findViewById(R.id.login_layout).setVisibility(View.VISIBLE);
			findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
			findViewById(R.id.matchup_layout).setVisibility(View.GONE);
			findViewById(R.id.gameplay_layout).setVisibility(View.GONE);

			if (alertDialog != null) {
				alertDialog.dismiss();
			}
			return;
		}

		((TextView) findViewById(R.id.name_field))
				.setText(getString(R.string.welcome)
						+ Games.Players.getCurrentPlayer(getApiClient())
								.getDisplayName());
		findViewById(R.id.login_layout).setVisibility(View.GONE);

		if (isDoingTurn) {
			findViewById(R.id.matchup_layout).setVisibility(View.GONE);
			findViewById(R.id.gameplay_layout).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.matchup_layout).setVisibility(View.VISIBLE);
			findViewById(R.id.gameplay_layout).setVisibility(View.GONE);
		}
	}

	@Override
	public void onSignInFailed() {
		setViewVisibility();
	}

	@Override
	public void onSignInSucceeded() {
		if (mHelper.getTurnBasedMatch() != null) {
			// GameHelper will cache any connection hint it gets. In this case,
			// it can cache a TurnBasedMatch that it got from choosing a
			// turn-based
			// game notification. If that's the case, you should go straight
			// into the game.
			updateMatch(mHelper.getTurnBasedMatch());
			return;
		}

		setViewVisibility();


		// Registering this activity as a handler for invitation and match
		// events.
		Games.Invitations.registerInvitationListener(getApiClient(), this);

		// Registering the MatchUpdateListener, which will replace notifications
		// players get.
		Games.TurnBasedMultiplayer.registerMatchUpdateListener(getApiClient(),
				this);
	}

	// Switch to game play view.
	public void setGameplayUI() {
		isDoingTurn = true;
		setViewVisibility();
		dataView.setText(turnData.data);
		turnCounterView.setText("Turn " + turnData.turnCounter);
	}

	// Spinner dialogs
	public void showSpinner() {
		findViewById(R.id.progressLayout).setVisibility(View.VISIBLE);
	}

	public void dismissSpinner() {
		findViewById(R.id.progressLayout).setVisibility(View.GONE);
	}

	// Generic warning/info dialog
	public void showWarning(String title, String message) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle(title).setMessage(message);

		// set dialog message
		alertDialogBuilder.setCancelable(false).setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, close
						// current activity
					}
				});

		// create alert dialog
		alertDialog = alertDialogBuilder.create();

		// show dialog
		alertDialog.show();
	}

	// Rematch dialog
	public void askForRematch() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		alertDialogBuilder.setMessage("Do you want a rematch?");

		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Sure, rematch!",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								rematch();
							}
						})
				.setNegativeButton("No.",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
							}
						});

		alertDialogBuilder.show();
	}

	// This function is what gets called when you return from either the Play
	// Games built-in inbox, or else the create game built-in interface.
	@Override
	public void onActivityResult(int request, int response, Intent data) {
		super.onActivityResult(request, response, data);
		// Returning from the 'Select Match' dialog
		if (request == RC_LOOK_AT_MATCHES) {

			if (response != Activity.RESULT_OK) {
				// user canceled
				return;
			}

			TurnBasedMatch match = data
					.getParcelableExtra(Multiplayer.EXTRA_TURN_BASED_MATCH);

			if (match != null) {
				updateMatch(match);
			}

			Log.d(TAG, "Match = " + match);
			// Returned from 'Select players to Invite' dialog
		} else if (request == RC_SELECT_PLAYERS) {

			if (response != Activity.RESULT_OK) {
				// user canceled
				return;
			}

			// get the invitee list
			final ArrayList<String> invitees = data
					.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);

			// get automatch criteria
			Bundle autoMatchCriteria = null;

			int minAutoMatchPlayers = data.getIntExtra(
					Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
			int maxAutoMatchPlayers = data.getIntExtra(
					Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);

			if (minAutoMatchPlayers > 0) {
				autoMatchCriteria = RoomConfig.createAutoMatchCriteria(
						minAutoMatchPlayers, maxAutoMatchPlayers, 0);
			} else {
				autoMatchCriteria = null;
			}

			TurnBasedMatchConfig tbmc = TurnBasedMatchConfig.builder()
					.addInvitedPlayers(invitees)
					.setAutoMatchCriteria(autoMatchCriteria).build();

			// Start the match
			Games.TurnBasedMultiplayer
					.createMatch(apiAgent, tbmc)
					.setResultCallback(
							new ResultCallback<TurnBasedMultiplayer.InitiateMatchResult>() {
								@Override
								public void onResult(
										TurnBasedMultiplayer.InitiateMatchResult result) {
									processResult(result);
								}
							});
			showSpinner();
		}
	}

	// startMatch() happens in response to the createTurnBasedMatch()
	// above. This is only called on success, so we should have a
	// valid match object. We're taking this opportunity to setup the
	// game, saving our initial state. Calling takeTurn() will
	// callback to OnTurnBasedMatchUpdated(), which will show the game
	// UI.
	public void startMatch(TurnBasedMatch match) {
//		startActivity(new Intent(this, GameActivity.class));
		turnData = new Turn();
		// Some basic turn data
		turnData.data = "First turn";

		this.match = match;

		String playerId = Games.Players.getCurrentPlayerId(apiAgent);
		String myParticipantId = match.getParticipantId(playerId);

		showSpinner();

		Games.TurnBasedMultiplayer.takeTurn(apiAgent, match.getMatchId(),
				turnData.persist(), myParticipantId).setResultCallback(
				new ResultCallback<TurnBasedMultiplayer.UpdateMatchResult>() {
					@Override
					public void onResult(
							TurnBasedMultiplayer.UpdateMatchResult result) {
						processResult(result);
					}
				});
	}

	// If you choose to rematch, then call it and wait for a response.
	public void rematch() {
		showSpinner();
		Games.TurnBasedMultiplayer
				.rematch(apiAgent, match.getMatchId())
				.setResultCallback(
						new ResultCallback<TurnBasedMultiplayer.InitiateMatchResult>() {
							@Override
							public void onResult(
									TurnBasedMultiplayer.InitiateMatchResult result) {
								processResult(result);
							}
						});
		match = null;
		isDoingTurn = false;
	}

	/**
	 * Get the next participant. In this function, we assume that we are
	 * round-robin, with all known players going before all automatch players.
	 * This is not a requirement; players can go in any order. However, you can
	 * take turns in any order.
	 * 
	 * @return participantId of next player, or null if automatching
	 */
	public String getNextParticipantId() {

		String playerId = Games.Players.getCurrentPlayerId(apiAgent);
		String myParticipantId = match.getParticipantId(playerId);

		ArrayList<String> participantIds = match.getParticipantIds();

		int desiredIndex = -1;

		for (int i = 0; i < participantIds.size(); i++) {
			if (participantIds.get(i).equals(myParticipantId)) {
				desiredIndex = i + 1;
			}
		}

		if (desiredIndex < participantIds.size()) {
			return participantIds.get(desiredIndex);
		}

		if (match.getAvailableAutoMatchSlots() <= 0) {
			// You've run out of automatch slots, so we start over.
			return participantIds.get(0);
		} else {
			// You have not yet fully automatched, so null will find a new
			// person to play against.
			return null;
		}
	}

	// This is the main function that gets called when players choose a match
	// from the inbox, or else create a match and want to start it.
	public void updateMatch(TurnBasedMatch match) {
		this.match = match;

		int status = match.getStatus();
		int turnStatus = match.getTurnStatus();

		switch (status) {
		case TurnBasedMatch.MATCH_STATUS_CANCELED:
			showWarning("Canceled!", "This game was canceled!");
			return;
		case TurnBasedMatch.MATCH_STATUS_EXPIRED:
			showWarning("Expired!", "This game is expired.  So sad!");
			return;
		case TurnBasedMatch.MATCH_STATUS_AUTO_MATCHING:
			showWarning("Waiting for auto-match...",
					"We're still waiting for an automatch partner.");
			return;
		case TurnBasedMatch.MATCH_STATUS_COMPLETE:
			if (turnStatus == TurnBasedMatch.MATCH_TURN_STATUS_COMPLETE) {
				showWarning(
						"Complete!",
						"This game is over; someone finished it, and so did you!  There is nothing to be done.");
				break;
			}

			// Note that in this state, you must still call "Finish" yourself,
			// so we allow this to continue.
			showWarning("Complete!",
					"This game is over; someone finished it!  You can only finish it now.");
		}

		// OK, it's active. Check on turn status.
		switch (turnStatus) {
		case TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN:
			turnData = Turn.unpersist(match.getData());
			setGameplayUI();
			return;
		case TurnBasedMatch.MATCH_TURN_STATUS_THEIR_TURN:
			// Should return results.
			showWarning("Alas...", "It's not your turn.");
			break;
		case TurnBasedMatch.MATCH_TURN_STATUS_INVITED:
			showWarning("Good inititative!",
					"Still waiting for invitations.\n\nBe patient!");
		}

		turnData = null;

		setViewVisibility();
	}

	public void processResult(TurnBasedMultiplayer.CancelMatchResult result) {
		dismissSpinner();

		if (!checkStatusCode(null, result.getStatus().getStatusCode())) {
			return;
		}

		isDoingTurn = false;

		showWarning("Match",
				"This match is canceled.  All other players will have their game ended.");
	}

	public void processResult(TurnBasedMultiplayer.InitiateMatchResult result) {
		TurnBasedMatch match = result.getMatch();
		dismissSpinner();

		if (!checkStatusCode(match, result.getStatus().getStatusCode())) {
			return;
		}

		if (match.getData() != null) {
			// This is a game that has already started, so I'll just start
			updateMatch(match);
			return;
		}

		startMatch(match);
	}

	public void processResult(TurnBasedMultiplayer.LeaveMatchResult result) {
		TurnBasedMatch match = result.getMatch();
		dismissSpinner();
		if (!checkStatusCode(match, result.getStatus().getStatusCode())) {
			return;
		}
		isDoingTurn = false;
		showWarning("Left", "You've left this match.");
		setViewVisibility();
	}

	public void processResult(TurnBasedMultiplayer.UpdateMatchResult result) {
		TurnBasedMatch match = result.getMatch();
		dismissSpinner();
		if (!checkStatusCode(match, result.getStatus().getStatusCode())) {
			return;
		}
		if (match.canRematch()) {
			askForRematch();
		}

		isDoingTurn = (match.getTurnStatus() == TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN);

		if (isDoingTurn) {
			updateMatch(match);
			return;
		}

		setViewVisibility();
	}

	// Handle notification events.
	@Override
	public void onInvitationReceived(Invitation invitation) {
		Toast.makeText(
				this,
				"An invitation has arrived from "
						+ invitation.getInviter().getDisplayName(), TOAST_DELAY)
				.show();
	}

	@Override
	public void onInvitationRemoved(String invitationId) {
		Toast.makeText(this, "An invitation was removed.", TOAST_DELAY).show();
	}

	@Override
	public void onTurnBasedMatchReceived(TurnBasedMatch match) {
		Toast.makeText(this, "A match was updated.", TOAST_DELAY).show();
	}

	@Override
	public void onTurnBasedMatchRemoved(String matchId) {
		Toast.makeText(this, "A match was removed.", TOAST_DELAY).show();

	}

	public void showErrorMessage(TurnBasedMatch match, int statusCode,
			int stringId) {

		showWarning("Warning", getResources().getString(stringId));
	}

	// Returns false if something went wrong, probably. This should handle
	// more cases, and probably report more accurate results.
	private boolean checkStatusCode(TurnBasedMatch match, int statusCode) {
		switch (statusCode) {
		case GamesStatusCodes.STATUS_OK:
			return true;
		case GamesStatusCodes.STATUS_NETWORK_ERROR_OPERATION_DEFERRED:
			// This is OK; the action is stored by Google Play Services and will
			// be dealt with later.
			Toast.makeText(
					this,
					"Stored action for later.  (Please remove this toast before release.)",
					TOAST_DELAY).show();
			// NOTE: This toast is for informative reasons only; please remove
			// it from your final application.
			return true;
		case GamesStatusCodes.STATUS_MULTIPLAYER_ERROR_NOT_TRUSTED_TESTER:
			showErrorMessage(match, statusCode,
					R.string.status_multiplayer_error_not_trusted_tester);
			break;
		case GamesStatusCodes.STATUS_MATCH_ERROR_ALREADY_REMATCHED:
			showErrorMessage(match, statusCode,
					R.string.match_error_already_rematched);
			break;
		case GamesStatusCodes.STATUS_NETWORK_ERROR_OPERATION_FAILED:
			showErrorMessage(match, statusCode,
					R.string.network_error_operation_failed);
			break;
		case GamesStatusCodes.STATUS_CLIENT_RECONNECT_REQUIRED:
			showErrorMessage(match, statusCode,
					R.string.client_reconnect_required);
			break;
		case GamesStatusCodes.STATUS_INTERNAL_ERROR:
			showErrorMessage(match, statusCode, R.string.internal_error);
			break;
		case GamesStatusCodes.STATUS_MATCH_ERROR_INACTIVE_MATCH:
			showErrorMessage(match, statusCode,
					R.string.match_error_inactive_match);
			break;
		case GamesStatusCodes.STATUS_MATCH_ERROR_LOCALLY_MODIFIED:
			showErrorMessage(match, statusCode,
					R.string.match_error_locally_modified);
			break;
		default:
			showErrorMessage(match, statusCode, R.string.unexpected_status);
			Log.d(TAG, "Did not have warning or string to deal with: "
					+ statusCode);
		}

		return false;
	}
}
