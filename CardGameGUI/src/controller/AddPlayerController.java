package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import model.SimplePlayer;
import view.CardGameFrame;
import view.model.AuxilaryGameEngine;

public class AddPlayerController implements ActionListener
{
	private AuxilaryGameEngine auxGE;
	private SimplePlayer player;
	private CardGameFrame cgf;
	// auto-generated playerID
	private static int playerId = 1;
	private JTextField nameField = new JTextField();
	private JTextField initPointsField = new JTextField();
	private String playerName;
	private String initPoints;
	
	
	public AddPlayerController(AuxilaryGameEngine auxGE, CardGameFrame cgf)
	{
		this.auxGE = auxGE;
		this.cgf = cgf;
	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{
		Object[] message =
		{ "Name:", nameField, "Initial Points:", initPointsField };
		int option = JOptionPane.showConfirmDialog(null, message, "Input Player Data", JOptionPane.OK_CANCEL_OPTION);


		if (option == JOptionPane.OK_OPTION)
		{
			playerName = nameField.getText();
			initPoints = initPointsField.getText();
		}

		if (option == JOptionPane.CLOSED_OPTION || option == JOptionPane.CANCEL_OPTION)
		{
			JOptionPane.showMessageDialog(cgf, "No player added.");
		} else
		{
			try
			{
				player = new SimplePlayer(String.valueOf(playerId), playerName, Integer.parseInt(initPoints));
				playerId += 1;
				
				auxGE.addPlayer(player);
				
			} catch (NumberFormatException ne)
			{
				JOptionPane.showMessageDialog(cgf, "Invalid Input given");
			}
			;
		}
		//Reset textfields
		nameField.setText("");
		initPointsField.setText("");
	}

	

}
