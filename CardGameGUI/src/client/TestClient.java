package client;

import validate.Validator;

import javax.swing.SwingUtilities;

import model.GameEngineImpl;

import model.interfaces.GameEngine;
import view.CardGameFrame;
import view.GameEngineCallbackGUI;
import view.GameEngineCallbackImpl;
import view.interfaces.GameEngineCallback;
import view.model.AuxilaryGameEngine;

public class TestClient
{
	public static void main(String[] args)
	{
		final GameEngine ge = new GameEngineImpl();
		final AuxilaryGameEngine auxGE = new AuxilaryGameEngine(ge);
		
		final GameEngineCallbackGUI gecGUI = new GameEngineCallbackGUI(auxGE);
		final GameEngineCallback gecConsole = new GameEngineCallbackImpl();

		ge.addGameEngineCallback(gecConsole);
	    ge.addGameEngineCallback(gecGUI);
	    
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				new CardGameFrame(auxGE, gecGUI);
			}

		});

		Validator.validate(false);

	}
}
