package com.diamondshearts.models;

public enum Suit {
	Diamond("D", "", 3),
	Heart("H", "", 1),
	Club("C", "", 3),
	Spade("S", "", 1);

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
