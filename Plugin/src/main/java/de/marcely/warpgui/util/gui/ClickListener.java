package de.marcely.warpgui.util.gui;

import org.bukkit.entity.Player;

/**
 * Listens for clicks on items
 */
public interface ClickListener {

	/**
	 * Gets called whenever a player clicked on a item in the GUI
	 * 
	 * @param player The player who clicked
	 * @param leftClick If it was a left or right click
	 * @param shiftClick If he pressed shift while clicking on it
	 */
	void onClick(Player player, boolean leftClick, boolean shiftClick);
	
	
	
	/**
	 * An implementation of ClickListener that does effectively nothing
	 */
	class Silent implements ClickListener {
		
		public static ClickListener INSTANCE = new Silent();
		
		private Silent(){ }
		
		@Override
		public final void onClick(Player player, boolean leftClick, boolean shiftClick){ }
	}
}
