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

import com.diamondshearts.models.Player;
import com.diamondshearts.models.Table;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.turnbased.OnTurnBasedMatchUpdateReceivedListener;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer;
import com.google.example.games.basegameutils.BaseGameActivity;
import com.thoughtworks.xstream.XStream;

/**
 * Main Game Activity, Launcher Activity. Include a main menu, and
 * communications to google play game service. A Part of code is from google
 * play service skeleton code.
 * 
 * @author Fei Tang
 */
public class MainActivity extends BaseGameActivity implements
		OnInvitationReceivedListener, OnTurnBasedMatchUpdateReceivedListener {
	public static final String TAG = "DrawingActivity";

	/** Google play game service Api Client. */
	public GoogleApiClient apiAgent;

	/** Intents handling for select players. */
	final static int RC_SELECT_PLAYERS = 10000;

	/** Intents handling for match. */
	final static int RC_LOOK_AT_MATCHES = 10001;

	/** How long to show toasts. */
	final static int TOAST_DELAY = 2000;

	/** The current match, null if not loaded. */
	public TurnBasedMatch match;

	/** The table state of game, null if not loaded. */
	// public Table table;

	/** The XStream object. */
	private XStream xStream;

	@Override
	/**
	 * Initialize variables and authentication when activity created
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Initialize objects
		apiAgent = getApiClient();
		xStream = new XStream();
		xStream.alias("table", Table.class);

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

	/**
	 * Show a dialog that ask player for rematch.
	 */
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

	/**
	 * Dismiss the spinner showed.
	 */
	public void dismissSpinner() {
		findViewById(R.id.progressLayout).setVisibility(View.GONE);
	}

	@Override
	/** 
	 * This function gets called when return from either the Play
	 * Games built-in inbox or else the create game built-in interface.
	 */
	public void onActivityResult(int request, int response, Intent intent) {
		super.onActivityResult(request, response, intent);
		// Returning from the 'Select Match' dialog
		if (request == RC_LOOK_AT_MATCHES) {

			if (response != Activity.RESULT_OK) {
				// user canceled
				return;
			}

			TurnBasedMatch match = intent
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
			final ArrayList<String> invitees = intent
					.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);

			// get automatch criteria
			Bundle autoMatchCriteria = null;

			int minAutoMatchPlayers = intent.getIntExtra(
					Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
			int maxAutoMatchPlayers = intent.getIntExtra(
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

	/**
	 * Displays all ongoing games .
	 * 
	 * @param view
	 *            The button.
	 */
	public void onCheckGamesClicked(View view) {
		checkGames();
	}

	public void checkGames() {
		Intent intent = Games.TurnBasedMultiplayer.getInboxIntent(apiAgent);
		startActivityForResult(intent, RC_LOOK_AT_MATCHES);
	}

	@Override
	/**
	 * Handle notification when a invitation is arrived.
	 */
	public void onInvitationReceived(Invitation invitation) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Invitation Received");
		alertDialogBuilder.setMessage("An invitation has arrived from "
				+ invitation.getInviter().getDisplayName() + ".");

		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("View",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								checkGames();
							}
						})
				.setNegativeButton("Ignore",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
							}
						});
		alertDialogBuilder.show();
	}

	@Override
	/**
	 * Handle notification when a invitation is removed.
	 */
	public void onInvitationRemoved(String invitationId) {
		Toast.makeText(this, "An invitation was removed.", TOAST_DELAY).show();
	}

	/**
	 * Open the player invitation UI.
	 * 
	 * @param view
	 *            The button.
	 */
	public void onNewGameClicked(View view) {
		Intent intent = Games.TurnBasedMultiplayer.getSelectOpponentsIntent(
				apiAgent, 1, 4, true);
		startActivityForResult(intent, RC_SELECT_PLAYERS);
	}

	/**
	 * Create a one-on-one auto-match game (2 players game)
	 * 
	 * @param view
	 *            The button.
	 */
	public void onQuickGameClicked(View view) {
		// Create a auto match criteria
		Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(1, 1, 0);
		TurnBasedMatchConfig tbmc = TurnBasedMatchConfig.builder()
				.setAutoMatchCriteria(autoMatchCriteria).build();

		// Start the match
		showSpinner();
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
	}

	@Override
	/**
	 * Show sign in layout when sign in failed
	 */
	public void onSignInFailed() {
		setViewVisibility();
	}

	@Override
	/**
	 * Check match data and initialize when sign in succeed
	 */
	public void onSignInSucceeded() {
		setViewVisibility();

		// if (mHelper.getTurnBasedMatch() != null) {
		// // Handle players that come from a notification click. Go striaght
		// // into game.
		// updateMatch(mHelper.getTurnBasedMatch());
		// return;
		// }

		// Registering this activity as a handler for invitation and match
		// events.
		Games.Invitations.registerInvitationListener(getApiClient(), this);

		// Registering the MatchUpdateListener, which will replace notifications
		// players get.
		Games.TurnBasedMultiplayer.registerMatchUpdateListener(getApiClient(),
				this);
	}

	/**
	 * DEBUG ONLY. Start a test game.
	 * 
	 * @param view
	 *            The button.
	 */
	public void onTestGameClicked(View view) {
		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);
	}

	@Override
	public void onTurnBasedMatchReceived(final TurnBasedMatch match) {
		String tableData = new String(match.getData());
		Table table = (Table) xStream.fromXML(tableData);
		if (!table.isPreGame()) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder.setTitle("Match Updated");
			alertDialogBuilder
					.setMessage("A match has been updated. Do you want to show that match?");

			alertDialogBuilder
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									updateMatch(match);
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									// Do Nothing
								}
							});
			alertDialogBuilder.show();
		}

	}

	@Override
	public void onTurnBasedMatchRemoved(String string) {
		Toast.makeText(this, "An match was removed.", TOAST_DELAY).show();
	}

	/**
	 * Call for rematch and wait for a response.
	 */
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
	}

	/**
	 * Handling error message from checkStatusCode
	 * 
	 * @param match
	 *            The match.
	 * @param statusCode
	 *            The statusCode.
	 * @param stringId
	 *            The error message.
	 */
	public void showErrorMessage(TurnBasedMatch match, int statusCode,
			int stringId) {

		showWarning("Warning", getResources().getString(stringId));
	}

	/**
	 * Show a waiting spinner.
	 */
	public void showSpinner() {
		findViewById(R.id.progressLayout).setVisibility(View.VISIBLE);
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
					}
				});
		// create and show alert dialog
		alertDialogBuilder.create().show();

	}
	
	/**
	 * Handling Game status from server.
	 * 
	 * @param match
	 *            The match.
	 * @param statusCode
	 *            The statusCode.
	 * @return If there is something wrong
	 */
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

	/**
	 * Set up table and start a new match. It there are any auto match player,
	 * then the game will wait until all players available. If all players are
	 * available, then the first player start his turn.
	 * 
	 * @param match
	 *            The match.
	 */
	private void initiateMatch(TurnBasedMatch match) {
		this.match = match;

		Table table = new Table();
		// Set players
		ArrayList<Player> players = new ArrayList<Player>();
		ArrayList<Participant> participants = match.getParticipants();
		for (Participant participant : participants)
			players.add(new Player(table, participant.getParticipantId(),
					participant.getDisplayName()));
		table.setPlayers(players);

		// Set current player
		String playerId = Games.Players.getCurrentPlayerId(apiAgent);
		String currnetParticipantId = match.getParticipantId(playerId);
		String currentPlayerName = match.getParticipant(currnetParticipantId)
				.getDisplayName();
		Player currentPlayer = new Player(table, currnetParticipantId,
				currentPlayerName);
		table.setCurrentPlayer(currentPlayer);
		table.setPlayerThisTurn(currentPlayer);

		table.setPreGame(true);
		String nextParticipantId = table.getNextParticipantId();
		table.setPlayerThisTurn(table.getPlayerById(nextParticipantId));
		Games.TurnBasedMultiplayer
				.takeTurn(getApiClient(), match.getMatchId(),
						xStream.toXML(table).getBytes(), nextParticipantId)
				.setResultCallback(
						new ResultCallback<TurnBasedMultiplayer.UpdateMatchResult>() {

							@Override
							public void onResult(TurnBasedMultiplayer.UpdateMatchResult result) {
								TurnBasedMatch match = result.getMatch();
								dismissSpinner();
								if (!checkStatusCode(match, result.getStatus().getStatusCode())) {
									return;
								}
								updateMatch(match);
							}
						});
		;

	}

	/**
	 * Handle server result when player initiate a match.
	 * 
	 * @param result
	 *            Result from server.
	 */
	private void processResult(TurnBasedMultiplayer.InitiateMatchResult result) {
		TurnBasedMatch match = result.getMatch();
		dismissSpinner();

		if (!checkStatusCode(match, result.getStatus().getStatusCode())) {
			return;
		}

		if (match.getData() != null) {
			// This is a game that has already started
			updateMatch(match);
			return;
		}
		initiateMatch(match);
	}
//
//	/**
//	 * Handle server result when player update a match.
//	 * 
//	 * @param result
//	 *            Result from server.
//	 */
//	private void processResult(TurnBasedMultiplayer.UpdateMatchResult result) {
//		TurnBasedMatch match = result.getMatch();
//		dismissSpinner();
//		if (!checkStatusCode(match, result.getStatus().getStatusCode())) {
//			return;
//		}
//	}

	/**
	 * Set menu visibility base on user sign in and sign out
	 */
	private void setViewVisibility() {
		// Not Signed in
		if (!isSignedIn()) {
			findViewById(R.id.login_layout).setVisibility(View.VISIBLE);
			findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
			findViewById(R.id.menu_layout).setVisibility(View.GONE);
		}
		// Signed in
		((TextView) findViewById(R.id.name_field))
				.setText(getString(R.string.welcome)
						+ Games.Players.getCurrentPlayer(getApiClient())
								.getDisplayName());
		findViewById(R.id.login_layout).setVisibility(View.GONE);
		findViewById(R.id.menu_layout).setVisibility(View.VISIBLE);
	}

	/**
	 * This is the main function that gets called when players choose a match
	 * from the inbox, or else create a match and want to start it.
	 * 
	 * @param match
	 *            The match.
	 */
	private void updateMatch(TurnBasedMatch match) {
		this.match = match;
		int status = match.getStatus();
		int turnStatus = match.getTurnStatus();

		// Handling errors
		switch (status) {
		case TurnBasedMatch.MATCH_STATUS_CANCELED:
			showWarning("Canceled!", "This game was canceled!");
			return;
		case TurnBasedMatch.MATCH_STATUS_EXPIRED:
			showWarning("Expired!", "This game is expired!");
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
			showWarning("Complete!",
					"This game is over; someone finished it!  You can only finish it now.");
		}

		// Check on turn status.
		switch (turnStatus) {
		case TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN:
			// Open game UI
			showGameUI(match);
			return;

		case TurnBasedMatch.MATCH_TURN_STATUS_THEIR_TURN:
			// Open game UI
			showGameUI(match);
			return;

		case TurnBasedMatch.MATCH_TURN_STATUS_INVITED:
			showWarning("Good inititative!",
					"Still waiting for invitations.\n\nBe patient!");
			return;
		}
	}

	/**
	 * @param match
	 */
	private void showGameUI(TurnBasedMatch match) {
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra("com.diamondshearts.match", match);
		intent.putExtra("com.diamondshearts.playerid",
				Games.Players.getCurrentPlayerId(apiAgent));
		startActivity(intent);
	}
}
