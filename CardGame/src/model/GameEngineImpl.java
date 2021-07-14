package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import model.interfaces.GameEngine;
import model.interfaces.Player;
import model.interfaces.PlayingCard;
import model.interfaces.PlayingCard.Suit;
import model.interfaces.PlayingCard.Value;
import view.interfaces.GameEngineCallback;

public class GameEngineImpl implements GameEngine
{
	private Map<String, Player> players;
	private LinkedList<PlayingCard> deck;
	private ArrayList<GameEngineCallback> callbacks;
	// house is implemented as a player
	Player house = new SimplePlayer("H1", "House", 0);

	public GameEngineImpl()
	{
		players = new TreeMap<>();
		deck = new LinkedList<>();
		callbacks = new ArrayList<>();
	}

	/**
	 * Method to validate the delay If input is valid, method will delay thread
	 * 
	 * @param delay number of milliseconds delay will last for
	 */
	private void sleep(int delay, Player player) throws IllegalArgumentException
	{
		// validate delay
		if (delay < 0)
		{
			throw new IllegalArgumentException("Negative delay value given.");
		} else if (delay > 1000 && !(player == house))
		{
			throw new IllegalArgumentException("Delay too long");
		} else
		{
			try
			{
				Thread.sleep(delay);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}

	}

	/**
	 * Decides to call the player or house version of nextCard()
	 * 
	 * @param player
	 * @param card
	 */
	private void decideNextCard(Player player, PlayingCard card)
	{
		if (player == house)
		{
			for (GameEngineCallback gec : callbacks)
				gec.nextHouseCard(card, this);
		} else
		{
			for (GameEngineCallback gec : callbacks)
				gec.nextCard(player, card, this);
		}
	}

	/**
	 * Decides to call the player or house version of bustCard()
	 * 
	 * @param player
	 * @param card
	 */
	private void decideBustCard(Player player, PlayingCard card)
	{
		if (player == house)
		{
			for (GameEngineCallback gec : callbacks)
				gec.houseBustCard(card, this);
		} else
		{
			for (GameEngineCallback gec : callbacks)
				gec.bustCard(player, card, this);
		}
	}

	private void validatePlayer(Player player) throws IllegalArgumentException, NullPointerException
	{
		if (player == null)
		{
			throw new NullPointerException("Player cannot be null.");
		}
		if (!players.containsKey(player.getPlayerId()) && !(player == house))
		{
			throw new IllegalArgumentException("Player not in system");
		}
	}

	@Override
	public void dealPlayer(Player player, int delay) throws IllegalArgumentException
	{
		validatePlayer(player);

		int playerScore = 0;

		while (playerScore <= BUST_LEVEL)
		{
			// Initializes the deck and also refills it with a newly shuffled half deck when
			// no cards are left
			if (deck.isEmpty())
				getShuffledHalfDeck();

			PlayingCard cardDrawn = deck.pop();
			playerScore += cardDrawn.getScore();

			if (playerScore < BUST_LEVEL)
			{
				decideNextCard(player, cardDrawn);
				player.setResult(playerScore);

			} else if (playerScore > BUST_LEVEL)
			{
				decideBustCard(player, cardDrawn);
			}
			// else if playerScore = 42 then nothing occurs
			
			sleep(delay, player);
		}

		for (GameEngineCallback gameEngineCallback : callbacks)
		{
			gameEngineCallback.result(player, player.getResult(), this);
		}

	}

	@Override
	public void dealHouse(int delay) throws IllegalArgumentException
	{
		dealPlayer(house, delay);

		// Apply Win Loss to each player
		for (Player player : players.values())
		{
			this.applyWinLoss(player, house.getResult());
		}
		for (GameEngineCallback gameEngineCallback : callbacks)
		{
			gameEngineCallback.houseResult(house.getResult(), this);
		}
		for (Player player : players.values())
		{
			player.resetBet();
		}
	}

	@Override
	public void applyWinLoss(Player player, int houseResult)
	{
		if (player.getResult() > houseResult)
		{
			player.setPoints(player.getPoints() + player.getBet());
		} else if (player.getResult() < houseResult)
		{
			player.setPoints(player.getPoints() - player.getBet());
		}
		// else a draw occurs. No points are set.
	}

	@Override
	public void addPlayer(Player player)
	{
		if (player == house)
		{
			throw new IllegalArgumentException("ID already taken by House");
		}
		// put() automatically replaces the in-system duplicate
		players.put(player.getPlayerId(), player);
	}

	@Override
	public Player getPlayer(String id)
	{
		return players.get(id);
	}

	@Override
	public boolean removePlayer(Player player)
	{
		Player removedPlayer = players.remove(player.getPlayerId());
		return removedPlayer == null ? false : true;
	}

	@Override
	public boolean placeBet(Player player, int bet)
	{
		validatePlayer(player);
		return player.setBet(bet);
	}

	@Override
	public void addGameEngineCallback(GameEngineCallback gameEngineCallback)
	{
		if (gameEngineCallback == null)
		{
			throw new NullPointerException("Null value passed as argument.");
		} else
		{
			callbacks.add(gameEngineCallback);
		}
	}

	@Override
	public boolean removeGameEngineCallback(GameEngineCallback gameEngineCallback)
	{
		return callbacks.remove(gameEngineCallback);
	}

	@Override
	public Collection<Player> getAllPlayers()
	{
		// Sorted Map Values cast to Sorted Collection
		Collection<Player> playerCollection = new TreeSet<>(players.values());
		return Collections.unmodifiableCollection(playerCollection);
	}

	@Override
	public Deque<PlayingCard> getShuffledHalfDeck()
	{
		populateHalfDeck();
		Collections.shuffle(deck);
		return deck;
	}

	/**
	 * Populates half deck. Does not shuffle deck.
	 */
	private void populateHalfDeck()
	{
		for (Suit suit : Suit.values())
		{
			for (Value value : Value.values())
			{
				PlayingCard card = new PlayingCardImpl(suit, value);
				// Ensures no duplicates
				if (!deck.contains(card))
					deck.add(card);
			}
		}

	}

}
