/**
* Adds a GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @website https://marcely.de/ 
*/

package de.marcely.warpgui;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import de.marcely.warpgui.command.WarpCommand;
import de.marcely.warpgui.components.Warp;
import de.marcely.warpgui.components.Warp.WarpingPlayer;
import de.marcely.warpgui.config.ConfigValue;

public class EventsManager implements Listener {
	
	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent event){
		
	}
	
	@EventHandler
	public void onInventoryDragEvent(InventoryDragEvent event){
		
	}
	
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event){
		final Location from = event.getFrom();
		final Location to = event.getTo();
		
		if(from.getBlockX() == to.getBlockX() &&
		   from.getBlockZ() == to.getBlockZ() &&
		   from.distance(to) < 0.42)
		return;
		
		WarpingPlayer wp = Warp.getWarpingPlayer(event.getPlayer());
		
		if(wp != null){
			event.getPlayer().sendMessage(Message.Teleporting_Stopped.getValue().replace("{warp}", wp.getWarp().getName()));
			wp.cancel();
		}
	}
	
	@EventHandler
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event){
		if(!ConfigValue.include_command_warps)
			return;
		
		final String[] parts = event.getMessage().split(" ");
		final String label = parts[0].replace("/", "");
		final String[] args = Arrays.copyOfRange(parts, 1, parts.length);
		
		if(label.equalsIgnoreCase("warps")){
			WarpCommand.onCommand(event.getPlayer(), label, args);
			
			event.setCancelled(true);
		}
	}
}
