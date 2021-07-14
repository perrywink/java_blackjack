package model;

import model.interfaces.PlayingCard;

public class PlayingCardImpl implements PlayingCard
{

	private final Suit suit;
	private final Value value;

	public PlayingCardImpl(Suit suit, Value value)
	{
		if (suit == null || value == null)
			throw new IllegalArgumentException("Null value(s) parsed as argument");
		this.suit = suit;
		this.value = value;
	}

	@Override
	public Suit getSuit()
	{
		return this.suit;
	}

	@Override
	public Value getValue()
	{
		return this.value;
	}

	@Override
	public int getScore()
	{
		switch (this.value)
		{
		case EIGHT:
			return 8;
		case NINE:
			return 9;
		case ACE:
			return 11;
		default:
			return 10;
			//Accounts for TEN, JACK, QUEEN AND KING
		}
	}
	
	/**
	 * Private helper method for formatting enum names into capitalized strings
	 * @param str
	 * @return capitalized version of str
	 */
	private String capitalize(String str)
	{
		return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
	}

	public String toString()
	{
		return String.format("Suit: %s, Value: %s, Score: %d", capitalize(this.suit.name()),
				capitalize(this.value.name()), this.getScore());
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((suit == null) ? 0 : suit.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(PlayingCard card)
	{
		return (this.getValue() == card.getValue() && this.getSuit() == card.getSuit());
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof PlayingCard)
		{
			PlayingCard playingCard = (PlayingCard) obj;
			return equals(playingCard);
		}
		return false;
	}

}
