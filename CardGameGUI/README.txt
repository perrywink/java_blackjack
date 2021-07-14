GUIDE TO RUN CARDGAMEGUI PROJECT
********************************
1. Click on add player to add player. 
2. Click on bet button and enter your bet for the round.
	- Note that the functionality of the buttons in the JToolbar correspond to the currently selected player in the combo box 
	(except for add player).	
3. Repeat adding the player and placing bets for each player (for however many players you want)
4. When ready, press the "deal" button on the player you wish to deal cards to (combo box selected player).
5. When all players have been dealt their cards, the system will automatically deal the cards to the house
6. When house's cards have been dealt, a dialog will appear asking if you wish to continue:
	- If you want to look at the round's hands before moving on, you can press "CANCEL" or the cross button to get rid of the pop-up.
	- If you want to immediately move on to the next round, you can press "YES"
	- If you wish to exit the game, press "NO"
7. Note that if you did not immediately move on to the next round, you can use "Start New Round" under "Master Game Controls" in the 
   menu bar to do so.

Notes:
- "Remove Player" is based on which player is selected at the moment.
- You can overwrite your placed bet after placing it 2 ways: 
	- By clicking on cancel bet followed by clicking on the bet button again OR
	- By just clicking the bet button (One way is definitely shorter than the other) 
- The AuxilaryGE is the ViewModel. Everything else has pretty straightforward names.