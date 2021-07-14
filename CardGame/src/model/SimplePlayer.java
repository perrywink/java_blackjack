package model;

import model.interfaces.Player;

public class SimplePlayer implements Player
{

	private String id;
	private String playerName;
	private int initialPoints;
	private int bet;
	private int result;

	public SimplePlayer(String id, String playerName, int initialPoints)
	{
		if (id == null || playerName == null)
			throw new IllegalArgumentException("Null value(s) parsed as argument");
		this.id = id;
		this.playerName = playerName;
		this.initialPoints = initialPoints;
	}

	@Override
	public String getPlayerName()
	{
		return playerName;
	}

	@Override
	public void setPlayerName(String playerName)
	{
		this.playerName = playerName;
	}

	@Override
	public int getPoints()
	{
		return initialPoints;
	}

	@Override
	public void setPoints(int points)
	{
		this.initialPoints = points;
	}

	@Override
	public String getPlayerId()
	{
		return id;
	}

	@Override
	public boolean setBet(int bet)
	{
		// Note if the bet is invalid the game will continue with the bet not set
		if (bet > 0 && bet <= this.getPoints())
		{
			this.bet = bet;
			return true;
		}
		return false;
	}

	@Override
	public int getBet()
	{
		return bet;
	}

	@Override
	public void resetBet()
	{
		this.bet = 0;
	}

	@Override
	public int getResult()
	{
		return result;
	}

	@Override
	public void setResult(int result)
	{
		this.result = result;

	}

	@Override
	public boolean equals(Player player)
	{
		return this.getPlayerId().equals(player.getPlayerId());
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Player)
		{
			Player player = (Player) obj;
			return equals(player);
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public int compareTo(Player player)
	{
		return this.getPlayerId().compareTo(player.getPlayerId());
	}

	@Override
	/**
	 * A toString() override used to output data relating to the player instance
	 * 
	 * @return A string of data describing the player instance
	 */
	public String toString()
	{
		return String.format("Player: id=%s, name=%s, bet=%d, points=%d, RESULT .. %d", this.getPlayerId(),
				this.getPlayerName(), this.getBet(), this.getPoints(), this.getResult());
	}

}
