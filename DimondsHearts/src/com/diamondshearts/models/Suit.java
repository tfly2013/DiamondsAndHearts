package com.diamondshearts.models;

/**
 * A suit refers to Hearts, Clubs, Diamonds, Spades
 * */
public enum Suit {
	/**A suit could be Hearts, Clubs, Diamonds, Spades, Suit's properties*/
	Diamond("♦", "", -1),
	Heart("♥", "", 1),
	Club("♣", "", 1.4),
	Spade("♠", "", 1.2);

	/**Suit name*/
	private String name;
	/**Suit description*/
	private String description;
	/**The value coefficient of a suit*/
	private double valueCoefficient;

	/**
	 * Initialize a suit
	 * @param name
	 * 			  Suit name
	 * @param desc
	 * 			  Suit description
	 * @param freq
	 * 			  The frequency of a suit
	 * */
	Suit(String name, String desc, double cofficient) {
		this.setName(name);
		this.setValueCoefficient(cofficient);
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
	 * Access the value coefficient
	 * @return the valueCoefficient
	 */
	public double getValueCoefficient() {
		return valueCoefficient;
	}

	/**
	 * Modify the value coefficient
	 * @param valueCoefficient the valueCoefficient to set
	 */
	public void setValueCoefficient(double valueCoefficient) {
		this.valueCoefficient = valueCoefficient;
	}
}