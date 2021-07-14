package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import model.interfaces.Player;
import view.CardGameFrame;
import view.model.AuxilaryGameEngine;

public class PlaceBetController implements ActionListener
{
	private AuxilaryGameEngine auxGE;
	private CardGameFrame cgf;
	
	
	public PlaceBetController(AuxilaryGameEngine auxGE, CardGameFrame cgf)
	{
		this.auxGE = auxGE;
		this.cgf = cgf;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String bet = JOptionPane.showInputDialog(cgf, "Place the player's bet:");
		Player selectedPlayer = auxGE.getSelectedPlayer();


		if (bet == null || selectedPlayer == null)
		{
			JOptionPane.showMessageDialog(cgf, "Bet Cancelled");
		} else
		{
			try
			{
				//Placement of bet occurs here
				if (!auxGE.placeBet(selectedPlayer, Integer.parseInt(bet)))
				{
					JOptionPane.showMessageDialog(cgf, "You betted more points than you have!");
				}

			} catch (NumberFormatException ne)
			{
				JOptionPane.showMessageDialog(cgf, "Invalid input. Bet has to be a number!");
			}

		}

	}

}
