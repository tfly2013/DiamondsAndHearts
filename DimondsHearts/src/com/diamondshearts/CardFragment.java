package com.diamondshearts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diamondshearts.models.Action;
import com.diamondshearts.models.Card;
import com.diamondshearts.models.Event;

public class CardFragment extends Fragment {

	private Card card;
	private LinearLayout actionsLayout;
	private LinearLayout eventsLayout;

	public CardFragment(Card card) {
		this.card = card;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_card, container,
				false);
		return rootView;
	}

	@Override
	public void onStart() {		
		actionsLayout = (LinearLayout) getActivity().findViewById(
				R.id.actions_layout);
		eventsLayout = (LinearLayout) getActivity().findViewById(
				R.id.events_layout);
		update();
		super.onStart();
	}

	public void update() {
		for (Action action : card.getActions()) {
			TextView suitView = new TextView(getActivity());
			suitView.setText(action.getSuit().getName());
			TextView rankView = new TextView(getActivity());
			rankView.setText(action.getRank().toString());
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			layoutParams.rightMargin = 5;
			rankView.setLayoutParams(layoutParams);
			actionsLayout.addView(suitView);
			actionsLayout.addView(rankView);
		}
		for (Event event : card.getEvents()){
			TextView evenView = new TextView(getActivity());
			evenView.setText(event.getName());
			eventsLayout.addView(evenView);
		}
	}
}
