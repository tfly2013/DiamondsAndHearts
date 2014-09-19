package com.diamondshearts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GamePlayFragment extends Fragment {
	
	public TextView dataView;
	public TextView turnCounterView;
	
	public GamePlayFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_game_play, container,
				false);
		dataView = ((TextView)getActivity().findViewById(R.id.data_view));
		turnCounterView = ((TextView) getActivity().findViewById(R.id.turn_counter_view));
		return rootView;
	}
}
