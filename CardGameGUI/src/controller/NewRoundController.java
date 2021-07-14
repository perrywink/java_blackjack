package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import view.model.AuxilaryGameEngine;

public class NewRoundController implements ActionListener
{
	private AuxilaryGameEngine auxGE;
		
	public NewRoundController(AuxilaryGameEngine auxGE)
	{
		this.auxGE = auxGE;
	}


	@Override
	public void actionPerformed(ActionEvent e)
	{
		auxGE.newRound();
	}

}
