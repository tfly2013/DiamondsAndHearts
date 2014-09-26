package com.diamondshearts.models;

/**
 * A suit refers to Hearts, Clubs, Diamonds, Spades
 * */
public enum Suit {
	/**a suit could be Hearts, Clubs, Diamonds, Spades, Suit's properties*/
	Diamond("♦", "", 3),
	Heart("♥", "", 1),
	Club("♣", "", 3),
	Spade("♠", "", 1);

	/**suit name*/
	private String name;
	/**suit description*/
	private String description;
	/**the frequency of a suit*/
	private Integer frequency;

	/**
	 * Initialize a suit
	 * @param name
	 * 			  Suit name
	 * @param desc
	 * 			  Suit description
	 * @param freq
	 * 			  The frequency of a suit
	 * */
	Suit(String name, String desc, Integer freq) {
		this.setName(name);
		this.setFrequency(freq);
		this.setDescription(desc);
	}

	/**
	 * Access the name of a suit
	 * @return name
	 * */
	public String getName() {
		return name;
	}

	/**
	 * Modify the name of a suit
	 * @param name
	 * 			  Suit name
	 * */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Access the frequency of a suit
	 * @return description
	 * */
	public String getDescription() {
		return description;
	}

	/**
	 * Modify the description of a suit
	 * @param description
	 * */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Access the frequency of a suit
	 * @return frequency
	 * */
	public Integer getFrequency() {
		return frequency;
	}

	/**
	 * Modify the frequency of a suit
	 * @param frequency
	 * 				  The frequency of a suit
	 * */
	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}
}