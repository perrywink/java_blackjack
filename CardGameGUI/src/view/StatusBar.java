package view;

import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.interfaces.Player;
import view.model.AuxilaryGameEngine;

@SuppressWarnings("serial")
public class StatusBar extends JPanel implements PropertyChangeListener
{
	private JLabel label1;
	@SuppressWarnings("unused")
	//Auxilary Game Engine never explicitly used but is needed for adding a PropertyChangeListener
	private AuxilaryGameEngine auxGE;
	
	public StatusBar(AuxilaryGameEngine auxGE)
	{
		this.auxGE = auxGE;
		
		auxGE.addPropertyChangeListener(this);
		
		setLayout(new GridLayout(1,1));
		label1 = new JLabel(" ", JLabel.LEFT);
		label1.setBorder(BorderFactory.createEtchedBorder());
		add(label1);
		
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		Player player = (Player)evt.getNewValue();
		if(evt.getPropertyName() == AuxilaryGameEngine.AuxGameEngineEvents.ADDED_PLAYER) {
			label1.setText(String.format("Player %s has been added to the game!", player.getPlayerName()));
		}
		
		else if(evt.getPropertyName() == AuxilaryGameEngine.AuxGameEngineEvents.REMOVED_PLAYER) {
			label1.setText(String.format("Player %s has been removed from the game!", player.getPlayerName()));
		}
		
		else if(evt.getPropertyName() == AuxilaryGameEngine.AuxGameEngineEvents.SWITCH_PLAYERS) {
			label1.setText(String.format("Switched to player %s!", player.getPlayerName()));
		}
		
		else if(evt.getPropertyName() == AuxilaryGameEngine.AuxGameEngineEvents.BET_PLACED) {
			label1.setText(String.format("%s placed a bet!", player.getPlayerName()));
		}
		
		else if(evt.getPropertyName() == AuxilaryGameEngine.AuxGameEngineEvents.NEW_ROUND) {
			label1.setText("New Round Started!");
		}
		
	}
}
