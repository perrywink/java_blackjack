package view;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.interfaces.Player;
import view.model.AuxilaryGameEngine;

@SuppressWarnings("serial")
public class SummaryPanel extends JPanel implements PropertyChangeListener
{
	private Collection<Player> players;

	private JTable summaryTable;
	private final String[] columnNames =
	{ "ID", "NAME", "BET", "POINTS", "RESULTS", "LAST GAME WIN/LOSS" };
	private Object[][] playerData;
	private DefaultTableModel tblModel;

	private AuxilaryGameEngine auxGE;

	public SummaryPanel(GameEngineCallbackGUI gecGUI, AuxilaryGameEngine auxGE)
	{
		this.auxGE = auxGE;
		populate();

		auxGE.addPropertyChangeListener(this);
		gecGUI.addPropertyChangeListener(this);
	}

	private void populate()
	{
		setLayout(new BorderLayout());
		summaryTable = new JTable();

		tblModel = new DefaultTableModel()
		{
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};

		tblModel.setColumnIdentifiers(columnNames);

		summaryTable.setModel(tblModel);
		summaryTable.setPreferredScrollableViewportSize(summaryTable.getPreferredSize());
		summaryTable.setFillsViewportHeight(true);

		add(new JScrollPane(summaryTable));
	}

	public void updatePlayers()
	{
		players = auxGE.getAllPlayers();
		playerData = new Object[players.size()][];
		ArrayList<Object[]> tempPlayerData = new ArrayList<>();

		for (Player player : players)
		{
			Object[] playerEntry = getRow(player);
			tempPlayerData.add(playerEntry);
		}

		for (int i = 0; i < players.size(); i++)
		{
			playerData[i] = tempPlayerData.get(i);
		}

		tblModel.setDataVector(playerData, columnNames);
		summaryTable.setModel(tblModel);
		tblModel.fireTableStructureChanged();
		revalidate();
	}

	private Object[] getRow(Player player)
	{
		return new Object[]
		{ player.getPlayerId(), player.getPlayerName(), player.getBet(), player.getPoints(), player.getResult(),
				auxGE.getPlayerStates().get(player).getPrevWinLoss() };
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		if (evt.getPropertyName() == AuxilaryGameEngine.AuxGameEngineEvents.ADDED_PLAYER
				|| evt.getPropertyName() == AuxilaryGameEngine.AuxGameEngineEvents.REMOVED_PLAYER
				|| evt.getPropertyName() == AuxilaryGameEngine.AuxGameEngineEvents.BET_PLACED
				|| evt.getPropertyName() == AuxilaryGameEngine.AuxGameEngineEvents.APPLY_WIN_LOSS
				|| evt.getPropertyName() == AuxilaryGameEngine.AuxGameEngineEvents.NEW_ROUND
				|| evt.getPropertyName() == GameEngineCallbackGUI.GECGUIEvents.APPLY_RESULTS)
		{
			updatePlayers();
		}

	}

}
