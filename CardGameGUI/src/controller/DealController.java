package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.interfaces.Player;
import view.model.AuxilaryGameEngine;

public class DealController implements ActionListener
{
	
	private AuxilaryGameEngine auxGE;
	
	private Player player;
	private static final int delay = 100;
	
	public DealController(AuxilaryGameEngine auxGE)
	{
		this.auxGE = auxGE;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		player = auxGE.getSelectedPlayer();
		
		new Thread()
		{
			public void run() {
				auxGE.dealPlayer(player, delay);
			}
		}.start();
		
	}

}
