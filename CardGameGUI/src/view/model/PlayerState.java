package view.model;

import java.util.ArrayList;

import model.interfaces.PlayingCard;

public class PlayerState
{
	private boolean hasDealt;
	private boolean hasBet;
	private String prevWinLoss;
	private ArrayList<PlayingCard> playerHand;
	
	public PlayerState()
	{
		this.hasDealt = false;
		this.hasBet = false;
		this.prevWinLoss = "-";
		playerHand = new ArrayList<>();
	}

	public boolean getHasDealt()
	{
		return hasDealt;
	}

	public void setHasDealt(boolean hasDealt)
	{
		this.hasDealt = hasDealt;
	}

	public boolean getHasBet()
	{
		return hasBet;
	}

	public void setHasBet(boolean hasBet)
	{
		this.hasBet = hasBet;
	}
	
	public String getPrevWinLoss()
	{
		return prevWinLoss;
	}

	public void setPrevWinLoss(String prevWinLoss)
	{
		this.prevWinLoss = prevWinLoss;
	}

	public ArrayList<PlayingCard> getPlayerHand()
	{
		return playerHand;
	}
	
	public void resetPlayerState(){
		this.hasDealt = false;
		this.hasBet = false;
		playerHand.clear();
	}

}
