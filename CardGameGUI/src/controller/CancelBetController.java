package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.interfaces.Player;
import view.model.AuxilaryGameEngine;

public class CancelBetController implements ActionListener
{
	private AuxilaryGameEngine auxGE;
	private Player selectedPlayer;
	
	
	public CancelBetController(AuxilaryGameEngine auxGE)
	{
		this.auxGE = auxGE;
	}


	@Override
	public void actionPerformed(ActionEvent e)
	{
		selectedPlayer = auxGE.getSelectedPlayer();
		selectedPlayer.resetBet();
		auxGE.cancelBet(selectedPlayer);

	}
	
}
