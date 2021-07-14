package view;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.SwingUtilities;

import model.interfaces.GameEngine;
import model.interfaces.Player;
import model.interfaces.PlayingCard;
import view.interfaces.GameEngineCallback;
import view.model.AuxilaryGameEngine;

public class GameEngineCallbackGUI implements GameEngineCallback
{

	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	public static final class GECGUIEvents
	{
		public static final String DEAL_CARD = "Next Card";
		public static final String BUST_CARD = "Bust Card";
		public static final String FINISH_DEAL = "Deal Finished";
		public static final String APPLY_RESULTS = "Apply Results";
	}

	public void addPropertyChangeListener(PropertyChangeListener listener)
	{
		this.pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
		this.pcs.removePropertyChangeListener(listener);
	}

	private AuxilaryGameEngine auxGE;

	public GameEngineCallbackGUI(AuxilaryGameEngine auxGE)
	{
		super();
		this.auxGE = auxGE;
	}

	@Override
	public void nextCard(Player player, PlayingCard card, GameEngine engine)
	{
		auxGE.getPlayerStates().get(player).getPlayerHand().add(card);
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				pcs.firePropertyChange(GECGUIEvents.DEAL_CARD, null, player);
			}
		});
	}

	@Override
	public void bustCard(Player player, PlayingCard card, GameEngine engine)
	{
		auxGE.getPlayerStates().get(player).getPlayerHand().add(card);

		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				pcs.firePropertyChange(GECGUIEvents.BUST_CARD, null, player);
			}
		});
		auxGE.getPlayerStates().get(player).setHasDealt(true);
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				pcs.firePropertyChange(GECGUIEvents.FINISH_DEAL, null, player);
			}
		});
	}

	@Override
	public void result(Player player, int result, GameEngine engine)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				pcs.firePropertyChange(GECGUIEvents.APPLY_RESULTS, null, player);
			}
		});
		
	}

	@Override
	public void nextHouseCard(PlayingCard card, GameEngine engine)
	{
		auxGE.getPlayerStates().get(auxGE.getHouse()).getPlayerHand().add(card);

		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				pcs.firePropertyChange(GECGUIEvents.DEAL_CARD, null, auxGE.getHouse());
			}
		});
	}

	@Override
	public void houseBustCard(PlayingCard card, GameEngine engine)
	{
		auxGE.getPlayerStates().get(auxGE.getHouse()).getPlayerHand().add(card);

		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				pcs.firePropertyChange(GECGUIEvents.BUST_CARD, null, auxGE.getHouse());
			}
		});
		auxGE.getPlayerStates().get(auxGE.getHouse()).setHasDealt(true);
	}

	@Override
	public void houseResult(int result, GameEngine engine)
	{
		for (Player p : auxGE.getAllPlayers())
		{
			auxGE.applyWinLoss(p, result);
		}
	}

}
