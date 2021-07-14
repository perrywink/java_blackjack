package client;

import model.GameEngineImpl;
import model.SimplePlayer;
import model.interfaces.GameEngine;
import model.interfaces.Player;
import validate.Validator;
import view.GameEngineCallbackImpl;

/**
 * Own Client class to debug program 
 * @author Chuah Yi Jie
 *
 */
public class MyClient
{
	public static void main(String args[])
	{
		final GameEngine gameEngine = new GameEngineImpl();

		// call method in Validator.jar to test *structural* correctness
		// just passing this does not mean it actually works .. you need to test
		// yourself!
		// pass false if you want to disable logging .. (i.e. once it passes)
		Validator.validate(false);
		
		// create two test players
		Player[] players = new Player[]
		{ new SimplePlayer("1", "The Shark", 1000), new SimplePlayer("1", "Bob", 700), new SimplePlayer("11", "The Loser", 500)};
		
		// add logging callback
		gameEngine.addGameEngineCallback(new GameEngineCallbackImpl());

		// main loop to add players, place a bet and receive hand
		for (Player player : players)
		{
			gameEngine.addPlayer(player);
			gameEngine.placeBet(player, 400);
			gameEngine.dealPlayer(player, 0);
		}
		
		// all players have played so now house deals
		// GameEngineCallBack.houseResult() is called to log all players (after results
		// are calculated)
		gameEngine.dealHouse(0);
		
//		gameEngine.removePlayer(gameEngine.getPlayer("11"));
		
		//round 2
		for (Player player : players)
		{
			gameEngine.placeBet(player, 300);
//			gameEngine.removePlayer(gameEngine.getPlayer("11"));
			gameEngine.dealPlayer(player, 0);
		}

		gameEngine.dealHouse(0);
		
		gameEngine.removeGameEngineCallback(new GameEngineCallbackImpl());
		
	}

}
