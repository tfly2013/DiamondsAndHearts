package com.diamondshearts;

import com.badlogic.gdx.Game;
import com.diamondshearts.screens.MainMenuScreen;

public class DiamondsHearts extends Game {

	@Override
	public void create () {	
		this.setScreen(new MainMenuScreen());
	}

	@Override
	public void render () {
		super.render();
	}
}
