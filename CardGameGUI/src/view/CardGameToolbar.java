package view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;

import controller.AddPlayerController;
import controller.CancelBetController;
import controller.DealController;
import controller.PlaceBetController;
import controller.RemovePlayerController;
import controller.SwitchPlayerController;
import model.interfaces.Player;
import view.model.AuxilaryGameEngine;

@SuppressWarnings("serial")
public class CardGameToolbar extends JToolBar implements PropertyChangeListener
{

	private JButton dealBtn;
	private JButton betBtn;
	private JButton cancelBetBtn;
	private ListCellRenderer<Player> playerListRenderer;
	private JComboBox<Player> playerComboBox;
	private JButton addPlayerBtn;
	private JButton removePlayerBtn;

	private AuxilaryGameEngine auxGE;
	private CardGameFrame cgf;

	public CardGameToolbar(GameEngineCallbackGUI gecGUI, AuxilaryGameEngine auxGE, CardGameFrame cgf)
	{
		this.auxGE = auxGE;
		this.cgf = cgf;
		populate();

		auxGE.addPropertyChangeListener(this);
		gecGUI.addPropertyChangeListener(this);
	}

	private void populate()
	{
		dealBtn = new JButton("Deal");
		betBtn = new JButton("Bet");
		cancelBetBtn = new JButton("Cancel Bet");
		
		playerListRenderer = new PlayerListRenderer();
		playerComboBox = new JComboBox<>();
		playerComboBox.setRenderer(playerListRenderer);
		
		addPlayerBtn = new JButton("Add Player");
		removePlayerBtn = new JButton("Remove Player");
		
		dealBtn.addActionListener(new DealController(auxGE));
		betBtn.addActionListener(new PlaceBetController(auxGE, cgf));
		cancelBetBtn.addActionListener(new CancelBetController(auxGE));
		playerComboBox.addActionListener(new SwitchPlayerController(auxGE, playerComboBox));
		addPlayerBtn.addActionListener(new AddPlayerController(auxGE, cgf));
		removePlayerBtn.addActionListener(new RemovePlayerController(auxGE));

		this.add(dealBtn);
		this.add(betBtn);
		this.add(cancelBetBtn);
		this.add(playerComboBox);
		this.add(addPlayerBtn);
		this.add(removePlayerBtn);

		dealBtn.setEnabled(false);
		betBtn.setEnabled(false);
		cancelBetBtn.setEnabled(false);
		removePlayerBtn.setEnabled(false);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		Player player = (Player) evt.getNewValue();
		checkPlayerIsHouse(player);
		
		if(!auxGE.getGameOver()) {
			enableAllBtns(true);
		}
		
		switch (evt.getPropertyName()){
		
		case AuxilaryGameEngine.AuxGameEngineEvents.ADDED_PLAYER:
			playerComboBox.addItem(player);
			playerComboBox.setSelectedItem(player);
			checkPlayerSelected(player);
			break;
		case AuxilaryGameEngine.AuxGameEngineEvents.REMOVED_PLAYER:
			playerComboBox.removeItem(player);
			checkPlayerSelected(player);
			break;
		case AuxilaryGameEngine.AuxGameEngineEvents.BET_PLACED:
			checkPlayerHasBet(player);
			break;
		
		case AuxilaryGameEngine.AuxGameEngineEvents.NEW_ROUND:
			checkPlayerSelected(auxGE.getSelectedPlayer());
			checkPlayerHasBet(auxGE.getSelectedPlayer());
			break;
		case AuxilaryGameEngine.AuxGameEngineEvents.SWITCH_PLAYERS:
			checkPlayerHasBet(player);
			break;
		case GameEngineCallbackGUI.GECGUIEvents.DEAL_CARD:
			enableAllBtns(false);
			break;
		case GameEngineCallbackGUI.GECGUIEvents.FINISH_DEAL:
			enableAllBtns(true);
			break;
		}
		
		if(auxGE.getGameOver()) {
			enableAllBtns(false);
		}
		checkPlayerHasDealt(auxGE.getSelectedPlayer());
		
		
	}

	private void checkPlayerSelected(Player player) {
		if (player == auxGE.getSelectedPlayer())
		{
			betBtn.setEnabled(true);
			removePlayerBtn.setEnabled(true);
		}
	}
	
	private void checkPlayerHasBet(Player player) {
		if (auxGE.getPlayerStates().get(player).getHasBet())
		{
			cancelBetBtn.setEnabled(true);
			dealBtn.setEnabled(true);
		} else
		{
			cancelBetBtn.setEnabled(false);
			dealBtn.setEnabled(false);
		}
	}
	
	private void checkPlayerHasDealt(Player player) {
		if(auxGE.getPlayerStates().get(player).getHasDealt()) {
			enableAllBtns(false);
		}
	}
	
	private void checkPlayerIsHouse(Player player) {
		
		if(auxGE.getSelectedPlayer() == auxGE.getHouse()) {
			enableAllBtns(false);
		}else {
			enableAllBtns(true);
		}
	}
	
	private void enableAllBtns(boolean enable) {
		addPlayerBtn.setEnabled(enable);
		dealBtn.setEnabled(enable);
		betBtn.setEnabled(enable);
		cancelBetBtn.setEnabled(enable);
		removePlayerBtn.setEnabled(enable);
	}
	

}
