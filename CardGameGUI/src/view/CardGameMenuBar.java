package view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import controller.ExitGameController;
import controller.NewRoundController;
import view.model.AuxilaryGameEngine;

@SuppressWarnings("serial")
public class CardGameMenuBar extends JMenuBar implements PropertyChangeListener
{
	//menus
	private JMenu gameMenu;
	private JMenuItem newRound;
	private JMenuItem exitGame;
    
    public CardGameMenuBar(AuxilaryGameEngine auxGE)
	{
    	auxGE.addPropertyChangeListener(this);;
    	
    	gameMenu = new JMenu("Master Game Controls");
    	
    	newRound = new JMenuItem("Start New Round");
    	newRound.addActionListener(new NewRoundController(auxGE));
    	gameMenu.add(newRound);
    	newRound.setEnabled(false);
    	
    	exitGame = new JMenuItem("Exit");
    	exitGame.addActionListener(new ExitGameController());
    	gameMenu.add(exitGame);
    	
    	add(gameMenu);
    	
    	setVisible(true);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		if(evt.getPropertyName() == AuxilaryGameEngine.AuxGameEngineEvents.GAME_OVER) {
			newRound.setEnabled(true);
		}
		if(evt.getPropertyName() == AuxilaryGameEngine.AuxGameEngineEvents.NEW_ROUND) {
			newRound.setEnabled(false);
		}
	}    
    
    
    
    
}
