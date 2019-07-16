package de.marcely.ezgui;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.entity.Player;

public abstract class BaseGUI {
	
	public static Map<Player, BaseGUI> openInventories = new HashMap<Player, BaseGUI>();
	public static Map<Player, BaseGUI> openInventoriesDelayed = new HashMap<Player, BaseGUI>();
	
	/**
	 * 
	 * @param player for which player it should open
	 */
	public abstract void open(Player player);
	
	/**
	 * 
	 * make sure to override this when creating the gui (not a reqiurement)
	 * @param player which player closed the inventory
	 */
	public abstract void onClose(Player player);
	
	public abstract void setTitle(String title);
	
	public abstract String getTitle();
	
	public abstract boolean isCancellable();
	
	public abstract boolean hasAntiDrop();
	
	
	/**
	 * 
	 * Closes the GUI for every player
	 */
	public void close(){
		close(false);
	}
	
	/**
	 * 
	 * Closes the GUI for every player
	 * @param silent If it should execute the onClose event
	 */
	public void close(boolean silent){
		for(Entry<Player, BaseGUI> e:openInventories.entrySet()){
			if(e.getValue().equals(this))
				close(e.getKey(), silent);
		}
	}
	
	/**
	 * 
	 * Closes the GUI for the specific player
	 * @param silent If it should execute the onClose event
	 */
	public void close(Player player){
		close(player, false);
	}
	
	/**
	 * 
	 * Closes the GUI for the specific player
	 */
	public void close(Player player, boolean silent){
		if(silent)
			openInventories.remove(player);
		
		player.closeInventory();
	}
	
	/**
	 * 
	 * Removes the player from the open list. By that the player is able to e.g. drag&drop items without cancell
	 */
	public void undepend(Player player){
		openInventories.remove(player);
	}
}
