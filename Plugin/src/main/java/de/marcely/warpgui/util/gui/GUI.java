package de.marcely.warpgui.util.gui;

import org.bukkit.entity.Player;

import java.util.Collection;

/**
 * Interface that includes anything that any kind of GUI have in common
 */
public interface GUI {
	
	/**
	 * Opens the GUI for the player
	 * 
	 * @param player The player who shall see the inventory
	 */
	void open(Player player);
	
	/**
	 * Closes this GUI for any player
	 */
	void closeAll();
	
	/**
	 * Set the title of the GUI. Keep in mind that not every type supports this
	 * 
	 * @param title The new title
	 */
	void setTitle(String title);
	
	/**
	 * Returns the set title
	 * 
	 * @return The title
	 */
	String getTitle();
	
	/**
	 * Returns if this type allows its items to get moved around and to be dropped
	 * 
	 * @return If the items are moveable/dragable
	 */
	boolean areItemsMoveable();
	
	/**
	 * Ignore it. Only for internal use
	 * 
	 * @return Something you probably don't need
	 */
	default boolean ignoresCancelEvent(){
		return false;
	}
	
	/**
	 * Returns the player for whom the GUI is currently open
	 * 
	 * @return The players for who the GUI has been opened
	 */
	Collection<Player> getPlayers();
	
	/**
	 * Resets its content
	 */
	void clear();
	
	/**
	 * Event method that's getting called whenever a player closes the inventory
	 * 
	 * @param player The player who closed the GUI
	 */
	default void onClose(Player player){ }
}
