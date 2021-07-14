package view;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import model.interfaces.Player;

@SuppressWarnings("serial")
public class PlayerListRenderer extends JLabel implements ListCellRenderer<Player>
{

	public PlayerListRenderer()
	{
		setOpaque(true);

	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Player> list, Player player, int index,
			boolean isSelected, boolean cellHasFocus)
	{
		try{
			setText(player.getPlayerName());
		}catch (NullPointerException e){
			//To catch a Null Pointer Exception when there are no players present
		}

		if (isSelected)

		{
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else
		{
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		return this;
	}

}
