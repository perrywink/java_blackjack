package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.interfaces.Player;
import view.model.AuxilaryGameEngine;

public class RemovePlayerController implements ActionListener
{
	private AuxilaryGameEngine auxGE;

	public RemovePlayerController(AuxilaryGameEngine auxGE)
	{
		this.auxGE = auxGE;
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		Player playerToBeRemoved = auxGE.getSelectedPlayer();
		auxGE.removePlayer(playerToBeRemoved);
	}

}
