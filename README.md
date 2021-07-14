# java_blackjack
A Java app with Swing UI where you and you can play against your friends.
This was a first-year uni project (finished in late November 2020) for a Java class I was taking which thought me proper OOP. 

## Why Swing?
There is no denying that Swing is old compared to something like JavaFX but this project still taught me programming patterns which I would later
recognise and use in frameworks like React JS and even game dev in Godot!

## MVC
This project uses the MVC pattern. 
- CardGame: Model dealing with game logic
- CardGameGUI: Contains the controllers and views which syncs with the model
  - Communicates with the underlying model in CardGame by using the `AuxilaryGameEngine` class in `view/model`.






