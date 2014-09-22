package com.diamondshearts.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Card {
	private static Random gen = new Random();
	private Integer cost;

	List<Action> actions = new ArrayList<Action>();
	List<Event> events = new ArrayList<Event>();
	
	public Card() {
		// add new actions/events
		for (int i=0; i<gen.nextInt(3) + 1; i++)
			actions.add(new Action());
		for (int i=0; i<gen.nextInt(3) + 1; i++)
			events.add(new Event());
		cost = 0;
	}

	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
	}
}
