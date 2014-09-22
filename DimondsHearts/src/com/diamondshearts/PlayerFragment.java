package com.diamondshearts;

import com.diamondshearts.models.Player;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PlayerFragment extends Fragment {

	private Player player;

	public PlayerFragment(Player player) {
		this.player = player;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_player, container,
				false);
		return rootView;

	}

	public void update() {
		((TextView) getActivity().findViewById(R.id.diamond_view))
				.setText(player.getName());
		((TextView) getActivity().findViewById(R.id.diamond_view))
				.setText(player.getDiamond().toString());
		((TextView) getActivity().findViewById(R.id.diamond_view))
				.setText(player.getHeart().toString());
		((TextView) getActivity().findViewById(R.id.diamond_view))
				.setText(player.getHand().size() + "");
	}
}
