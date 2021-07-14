package view;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import model.interfaces.PlayingCard.Suit;

public class SuitIconFactory
{
	private static final String FILE_PATH = String.format("img%s", File.separator);

	private static Map<Suit, ImageIcon> suitMap;

	public static ImageIcon getImageIcon(Suit suit)
	{
		// lazy instantiation and initialisation (on demand)
		if (suitMap == null)
			createImageIcons();

		return suitMap.get(suit);
	}

	private static void createImageIcons()
	{
		suitMap = new HashMap<Suit, ImageIcon>();

		for (Suit suit : Suit.values())
			suitMap.put(suit, new ImageIcon(getFullPath(suit)));
	}

	// build filename from Color constant
	// do an extra check to add trailing path if necessary
	private static String getFullPath(Suit suit)
	{
		return String.format("%s%s.png",FILE_PATH, suit.toString().toLowerCase());
		
	}

}
