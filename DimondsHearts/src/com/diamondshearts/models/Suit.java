package com.diamondshearts.models;

public enum Suit {
	Diamond("♦", "", 3),
	Heart("♥", "", 1),
	Club("♣", "", 3),
	Spade("♠", "", 1);

	private String name;
	private String description;
	private Integer frequency;

	Suit(String name, String desc, Integer freq) {
		this.setName(name);
		this.setFrequency(freq);
		this.setDescription(desc);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}
}
