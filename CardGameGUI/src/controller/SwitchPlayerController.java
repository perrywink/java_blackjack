package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import model.interfaces.Player;
import view.model.AuxilaryGameEngine;

public class SwitchPlayerController implements ActionListener
{
	private AuxilaryGameEngine auxGE;
	private JComboBox<Player> playerComboBox;
	private Player selectedPlayer;

	public SwitchPlayerController(AuxilaryGameEngine auxGE, JComboBox<Player> playerComboBox)
	{
		this.auxGE = auxGE;
		this.playerComboBox = playerComboBox;
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		// Accounts for when house is auto-selected
		if (playerComboBox.getSelectedItem() == auxGE.getHouse().getPlayerId())
		{
			selectedPlayer = auxGE.getHouse();
		} else
		{
			// Catches the scenario when all players have been removed
			if (playerComboBox.getSelectedItem() == null)
			{
				return;
			}
			selectedPlayer = (Player) playerComboBox.getSelectedItem();
		}
		auxGE.setSelectedPlayer(selectedPlayer);

	}

}
