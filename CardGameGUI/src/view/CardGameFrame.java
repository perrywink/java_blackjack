package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import view.model.AuxilaryGameEngine;

@SuppressWarnings("serial")
public class CardGameFrame extends JFrame
{
	private static final int SUMMARY_PANEL_RATIO = 4;
	private CardGameToolbar toolbar;
	private SummaryPanel summaryPanel;
	private JPanel cardPanel;
	private JPanel statusBar;
	private JMenuBar menuBar;

	private AuxilaryGameEngine auxGE;
	private GameEngineCallbackGUI gecGUI;
	
	private final int FRAME_WIDTH = 1280;
	private final int FRAME_HEIGHT = 720;
	private final int MIN_FRAME_WIDTH = 853;
	private final int MIN_FRAME_HEIGHT = 480;
	
	public CardGameFrame(AuxilaryGameEngine auxGE, GameEngineCallbackGUI gecGUI)
	{
		super("Card Game GUI");
		this.auxGE = auxGE;
		this.gecGUI = gecGUI;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		setMinimumSize(new Dimension(MIN_FRAME_WIDTH, MIN_FRAME_HEIGHT));
		populate();

	}

	private void populate()
	{
		menuBar = new CardGameMenuBar(auxGE);
		setJMenuBar(menuBar);

		toolbar = new CardGameToolbar(gecGUI, auxGE, this);
		add(toolbar, BorderLayout.NORTH);

		JPanel midPanel = new JPanel();
		midPanel.setLayout(new BorderLayout());

		summaryPanel = new SummaryPanel(gecGUI, auxGE);
		summaryPanel.addComponentListener(new SummaryPanelResizer());
		midPanel.add(summaryPanel, BorderLayout.NORTH);

		cardPanel = new CardPanel(gecGUI, auxGE);
		midPanel.add(cardPanel, BorderLayout.CENTER);
		add(midPanel, BorderLayout.CENTER);

		statusBar = new StatusBar(auxGE);
		add(statusBar, BorderLayout.SOUTH);
		
		setVisible(true);
	}

	class SummaryPanelResizer extends ComponentAdapter
	{
		@Override
		public void componentResized(ComponentEvent e)
		{
			summaryPanel.setPreferredSize(new Dimension(getWidth(), getHeight() / SUMMARY_PANEL_RATIO));
		}
	}


}
