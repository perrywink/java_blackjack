package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import model.interfaces.PlayingCard;
import model.interfaces.PlayingCard.Suit;
import view.model.AuxilaryGameEngine;

@SuppressWarnings("serial")
public class CardPanel extends JPanel implements PropertyChangeListener
{
	private static final int NUM_CARDS = 6;
	private static final int RATIO_QUARTER = 4;
	private static final int EXTRA_PANEL_WIDTH = 100;
	private static final int PADDING_BETWEEN_CARDS = 40;

	private int x;
	private int y;
	private int cardBottomCornerX;
	private int cardBottomCornerY;
	private int cardWidth;
	private int cardHeight;
	private int arc = 20;

	private int panelWidth;
	private int panelHeight;

	private int cardIndex = 0;

	private int fontSize;
	private final double FONT_SCALE_RATIO = 0.001;
	private int style = Font.BOLD;
	Font fontCard;
	FontMetrics metrics;
	private String cardValue;

	private Suit suit;
	private ImageIcon suitIcon;
	private Image scaledSuitImage;

	private boolean bustCard;
	private boolean dealing;
	private AuxilaryGameEngine auxGE;
	private List<PlayingCard> currDisplayedHand;

	CardPanel(GameEngineCallbackGUI gecGUI, AuxilaryGameEngine auxGE)
	{
		this.auxGE = auxGE;

		currDisplayedHand = new ArrayList<>();
		bustCard = false;
		dealing = false;

		gecGUI.addPropertyChangeListener(this);
		auxGE.addPropertyChangeListener(this);
	}

	private void paintCardBG(Graphics g, int cardIndex, boolean bustCard, boolean dealing)
	{
		panelWidth = getWidth();
		panelHeight = getHeight();

		x = ((panelWidth - EXTRA_PANEL_WIDTH) / NUM_CARDS) * cardIndex;
		// Translate to the right
		x += PADDING_BETWEEN_CARDS;
		y = panelHeight / RATIO_QUARTER;

		cardWidth = (panelWidth / NUM_CARDS) - PADDING_BETWEEN_CARDS;
		// Card Height * 1.5
		cardHeight = (cardWidth * 3) / 2;

		cardBottomCornerX = x + cardWidth;
		cardBottomCornerY = y + cardHeight;

		g.setColor(Color.WHITE);
		if (!dealing)
		{
			bustCard = true;
		}
		if (bustCard && cardIndex == currDisplayedHand.size() - 1)
		{
			g.setColor(Color.gray);
		}
		g.fillRoundRect(x, y, cardWidth, cardHeight, arc, arc);
	}

	private void drawCardSuit(Graphics g, Suit suit)
	{
		suitIcon = SuitIconFactory.getImageIcon(suit);

		int newIconWidth = cardWidth / 6;
		int newIconHeight = (newIconWidth * suitIcon.getIconHeight()) / suitIcon.getIconWidth();
		suitIcon = scaleSuitImage(suitIcon, newIconWidth, newIconHeight);
		
		g.drawImage(suitIcon.getImage(), x + (cardWidth / 2) - (suitIcon.getIconWidth() / 2),
				y + (cardHeight / 2) - (suitIcon.getIconHeight() / 2), null);
	}

	private void drawCardValue(Graphics g, String cardValue)
	{
		Graphics2D g2 = (Graphics2D) g;
		fontCard = new Font("Arial", style, 20);
		this.cardValue = cardValue;

		AffineTransform at = new AffineTransform();
		FontRenderContext frc = new FontRenderContext(at, true, true);
		double textWidth = fontCard.getStringBounds(cardValue, frc).getWidth();
		double textHeight = fontCard.getStringBounds(cardValue, frc).getHeight();

		if (suit == Suit.CLUBS || suit == Suit.SPADES)
			g2.setColor(Color.BLACK);
		else if (suit == Suit.HEARTS || suit == Suit.DIAMONDS)
			g2.setColor(Color.RED);

		g2.setFont(fontCard);
		fontSize = fontCard.getSize();
		g2.setFont(fontCard.deriveFont((float) (fontSize * (panelWidth * FONT_SCALE_RATIO))));
		metrics = g2.getFontMetrics(fontCard);

		int horizontalPadding = cardWidth / 7;
		int verticalPadding = cardHeight / 10;

		//For centering
		int textHalfWidth = (int) textWidth / 2;
		int textHalfHeight = (int) textHeight / 2;

		g2.drawString(cardValue, x + horizontalPadding - textHalfWidth,
				y + (int) textHeight + verticalPadding - textHalfHeight);
		g2.drawString(cardValue, cardBottomCornerX - (int) textWidth - horizontalPadding + textHalfWidth,
				cardBottomCornerY - verticalPadding + textHalfHeight);
	}

	private ImageIcon scaleSuitImage(ImageIcon suitIcon, int newIconWidth, int newIconHeight)
	{
		scaledSuitImage = suitIcon.getImage().getScaledInstance(newIconWidth, newIconHeight, Image.SCALE_SMOOTH);
		return new ImageIcon(scaledSuitImage);
	}

	private String getValueString(PlayingCard card)
	{
		switch (card.getValue())
		{
		case JACK:
			return "J";
		case QUEEN:
			return "Q";
		case KING:
			return "K";
		case TEN:
			return "T";
		case ACE:
			return "A";
		default:
			break;
		}
		return String.valueOf(card.getScore());
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		// Reset cardIndex
		cardIndex = 0;
		// A buffer is used to avoid ConcurrentModificationException which occurs when
		// the delay is short
		List<PlayingCard> bufferHand = new ArrayList<>(currDisplayedHand);

		for (PlayingCard p : bufferHand)
		{
			paintCardBG(g, cardIndex, bustCard, dealing);

			// Draw the Card Suit
			suit = p.getSuit();
			drawCardSuit(g, suit);

			// Draw The Card Value
			cardValue = getValueString(p);
			drawCardValue(g, cardValue);
			cardIndex += 1;
		}

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		if (evt.getPropertyName() == GameEngineCallbackGUI.GECGUIEvents.DEAL_CARD)
		{
			dealing = true;
			bustCard = false;
			currDisplayedHand = auxGE.getPlayerStates().get(auxGE.getSelectedPlayer()).getPlayerHand();
			repaint();
		} else if (evt.getPropertyName() == GameEngineCallbackGUI.GECGUIEvents.BUST_CARD)
		{
			currDisplayedHand = auxGE.getPlayerStates().get(auxGE.getSelectedPlayer()).getPlayerHand();
			bustCard = true;
			dealing = false;
			repaint();
		} else if (evt.getPropertyName() == AuxilaryGameEngine.AuxGameEngineEvents.SWITCH_PLAYERS)
		{
			currDisplayedHand = auxGE.getPlayerStates().get(auxGE.getSelectedPlayer()).getPlayerHand();
			repaint();
		} else if (evt.getPropertyName() == AuxilaryGameEngine.AuxGameEngineEvents.NEW_ROUND)
		{
			currDisplayedHand.clear();
			repaint();
		}

	}

}
