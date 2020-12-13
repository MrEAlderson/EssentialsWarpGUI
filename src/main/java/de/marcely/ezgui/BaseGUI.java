package de.marcely.ezgui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.entity.Player;

public abstract class BaseGUI {
	
	public static Map<Player, BaseGUI> openInventories = new HashMap<>();
	
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
	 * @param silent If it should execute the onClose event
	 */
	public void close(){
		final Iterator<Entry<Player, BaseGUI>> it = openInventories.entrySet().iterator();
		
		while(it.hasNext()){
			final Entry<Player, BaseGUI> e = it.next();
			
			if(e.getValue() != this)
				continue;
			
			it.remove();
			
			e.getKey().closeInventory();
		}
	}
	
	/**
	 * 
	 * Closes the GUI for the specific player
	 * @param silent If it should execute the onClose event
	 */
	public void close(Player player){
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
