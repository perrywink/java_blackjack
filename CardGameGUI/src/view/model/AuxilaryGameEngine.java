package view.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import model.SimplePlayer;
import model.interfaces.GameEngine;
import model.interfaces.Player;
import model.interfaces.PlayingCard;
import view.CardGameFrame;
import view.interfaces.GameEngineCallback;

public class AuxilaryGameEngine implements GameEngine
{
	private GameEngine ge;
	private CardGameFrame cgf;

	private Player selectedPlayer;
	private Player house = new SimplePlayer("H1", "House", 0);
	private Map<Player, PlayerState> playerStates;

	private int houseDelayDeal = 100;
	private boolean gameOver;

	// property change events
	public static class AuxGameEngineEvents
	{
		public static final String ADDED_PLAYER = "player added";
		public static final String REMOVED_PLAYER = "player removed";
		public static final String BET_PLACED = "bet placed";
		public static final String APPLY_WIN_LOSS = "apply win loss";
		public static final String SWITCH_PLAYERS = "switch players";
		public static final String NEW_ROUND = "new round";
		public static final String GAME_OVER = "game over";
	}

	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	public AuxilaryGameEngine(GameEngine ge)
	{
		this.ge = ge;
		playerStates = new HashMap<>();
	}

	public Player getHouse()
	{
		return house;
	}

	public Player getSelectedPlayer()
	{
		return selectedPlayer;
	}

	public void setSelectedPlayer(Player selectedPlayer)
	{
		this.selectedPlayer = selectedPlayer;
		pcs.firePropertyChange(AuxGameEngineEvents.SWITCH_PLAYERS, null, selectedPlayer);
	}

	public Map<Player, PlayerState> getPlayerStates()
	{
		return playerStates;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener)
	{
		this.pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
		this.pcs.removePropertyChangeListener(listener);
	}

	public boolean checkAllPlayersDealt()
	{
		boolean dealHouse = true;

		Map<Player, PlayerState> tempPlayerStates = playerStates;
		tempPlayerStates.remove(house);

		for (PlayerState ps : tempPlayerStates.values())
		{
			if (ps.getHasDealt() == false)
			{
				dealHouse = false;
			}
		}

		return dealHouse;
	}

	@Override
	public void dealPlayer(Player player, int delay) throws IllegalArgumentException
	{
		ge.dealPlayer(player, delay);

		if (checkAllPlayersDealt())
		{
			dealHouse(houseDelayDeal);
		}
		;
		playerStates.get(player).setHasDealt(true);
	}

	@Override
	public void dealHouse(int delay) throws IllegalArgumentException
	{
		// This house mirrors the house in GEI.
		// House player state will only be initialized if the map does not have it.
		if (!playerStates.containsKey(house))
		{
			playerStates.put(house, new PlayerState());
		}

		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				pcs.firePropertyChange(AuxGameEngineEvents.ADDED_PLAYER, null, house);
			}
		});
		new Thread()
		{
			@Override
			public void run()
			{
				ge.dealHouse(delay);
				gameOver = true;
				newRound();
			}
		}.start();
		

	}

	private void checkPlayersNoPoints()
	{
		for (Player p : getAllPlayers())
		{
			if (p.getPoints() <= 0)
			{
				playerStates.remove(p);
				ge.removePlayer(p);
				SwingUtilities.invokeLater(new Runnable()
				{
					@Override
					public void run()
					{
						pcs.firePropertyChange(AuxGameEngineEvents.REMOVED_PLAYER, null, p);
					}
				});
				JOptionPane.showMessageDialog(cgf,
						String.format("Player %s had no points left and was removed", p.getPlayerName()));
			}
		}
	}

	public void newRound()
	{
		checkPlayersNoPoints();
		int option = JOptionPane.showConfirmDialog(cgf, "Start A New Round?");
		switch (option)
		{
		case JOptionPane.OK_OPTION:
			resetAllPlayerStates();
			//bet is automatically reset
			resetResults();
			gameOver = false;
			SwingUtilities.invokeLater(new Runnable()
			{
				@Override
				public void run()
				{
					pcs.firePropertyChange(AuxGameEngineEvents.NEW_ROUND, null, null);
					pcs.firePropertyChange(AuxGameEngineEvents.REMOVED_PLAYER, null, house);
				}
			});
			break;
		case JOptionPane.CLOSED_OPTION:
		case JOptionPane.CANCEL_OPTION:
			//Rather instantaneous so no need for use of execution using invoke later
			pcs.firePropertyChange(AuxGameEngineEvents.GAME_OVER, null, null);
			break;
		default:
			System.exit(0);
		}
	}

	private void resetResults() {
		for (Player p: getAllPlayers()) {
			p.setResult(0);
		}
	}
	
	@Override
	public void applyWinLoss(Player player, int houseResult)
	{
		// GEI not called because applyWinLoss occurs implicitly in GEI
		if (player.getResult() > houseResult)
		{
			playerStates.get(player).setPrevWinLoss("WIN");
		} else if (player.getResult() < houseResult)
		{
			playerStates.get(player).setPrevWinLoss("LOSS");
		} else
		{
			playerStates.get(player).setPrevWinLoss("DRAW");
		}
		
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				pcs.firePropertyChange(AuxGameEngineEvents.APPLY_WIN_LOSS, null, null);
			}
		});
		
	}

	@Override
	public void addPlayer(Player player)
	{
		ge.addPlayer(player);
		playerStates.put(player, new PlayerState());
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				pcs.firePropertyChange(AuxGameEngineEvents.ADDED_PLAYER, null, player);
			}
		});
	}

	@Override
	public Player getPlayer(String id)
	{
		return ge.getPlayer(id);
	}

	@Override
	public boolean removePlayer(Player player)
	{
		playerStates.remove(player);
		boolean hasRemovedPlayer = ge.removePlayer(player);
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				pcs.firePropertyChange(AuxGameEngineEvents.REMOVED_PLAYER, null, player);
			}
		});
		if (checkAllPlayersDealt())
		{
			dealHouse(houseDelayDeal);
		}
		return hasRemovedPlayer;
	}

	@Override
	public boolean placeBet(Player player, int bet)
	{
		boolean betState = ge.placeBet(player, bet);
		if (betState)
		{
			playerStates.get(player).setHasBet(true);
			pcs.firePropertyChange(AuxGameEngineEvents.BET_PLACED, null, player);
		}
		return betState;
	}

	public void cancelBet(Player player)
	{
		playerStates.get(player).setHasBet(false);
		pcs.firePropertyChange(AuxilaryGameEngine.AuxGameEngineEvents.BET_PLACED, null, player);
	}

	@Override
	public void addGameEngineCallback(GameEngineCallback gameEngineCallback)
	{
		ge.addGameEngineCallback(gameEngineCallback);
	}

	@Override
	public boolean removeGameEngineCallback(GameEngineCallback gameEngineCallback)
	{
		return ge.removeGameEngineCallback(gameEngineCallback);
	}

	@Override
	public Collection<Player> getAllPlayers()
	{
		return ge.getAllPlayers();
	}

	@Override
	public Deque<PlayingCard> getShuffledHalfDeck()
	{
		return ge.getShuffledHalfDeck();
	}

	private void resetAllPlayerStates()
	{
		for (PlayerState ps : playerStates.values())
		{
			ps.resetPlayerState();
		}
	}

	public boolean getGameOver()
	{
		return gameOver;
	}

}
