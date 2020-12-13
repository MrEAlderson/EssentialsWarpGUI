package de.marcely.ezgui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;

public class EventsManager implements Listener {
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event){
		final Player player = (Player) event.getWhoClicked();
		final BaseGUI gui = GUI.openInventories.get(player);
		
		if(gui == null)
			return;
		
		// cancel shift to put item into inv
		if(event.getClickedInventory() != null && event.getClickedInventory().getType() == InventoryType.PLAYER && event.isShiftClick() && gui.isCancellable()){
			event.setCancelled(true);
			return;
		}
		
		// cancell if clicking on item
		if((event.getClickedInventory() != null && event.getClickedInventory().getType() != InventoryType.PLAYER || event.getClick() == ClickType.DOUBLE_CLICK) && gui.isCancellable()){
			event.setCancelled(true);
			
			if(gui instanceof Clickable){
				final Clickable cgui = (Clickable) gui;
				final GUIItem item = cgui.getItemAt(event.getSlot());
				
				if(item != null)
					item.onClick(player, event.isLeftClick(), event.isShiftClick());
			
			}
		}
	}
	
	@EventHandler
	public void onInventoryCloseEvent(InventoryCloseEvent event){
		final Player player = (Player) event.getPlayer();
		
		// remove player from openInventories if he's inside
		if(GUI.openInventories.containsKey(player)){
			if(GUI.cachePlayers.contains(player))
				GUI.cachePlayers.remove(player);
			else
				GUI.openInventories.get(player).onClose(player);
			
			GUI.openInventories.remove(player);
		}
	}
	
	@EventHandler
	public void onInventoryDragEvent(InventoryDragEvent event){
		final Player player = (Player) event.getWhoClicked();
		final BaseGUI gui = GUI.openInventories.get(player);
		
		// cancel if clicking on GUI
		if(gui != null && event.getInventory().getType() != InventoryType.PLAYER && gui.isCancellable())
			event.setCancelled(true);
	}
}
