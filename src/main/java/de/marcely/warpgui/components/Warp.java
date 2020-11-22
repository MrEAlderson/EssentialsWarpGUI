/**
* Adds a GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @website https://marcely.de/ 
*/

package de.marcely.warpgui.components;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import de.marcely.warpgui.EssentialsWarpGUI;
import de.marcely.warpgui.Message;
import de.marcely.warpgui.util.Util;
import de.marcely.warpgui.util.Validate;
import lombok.Getter;
import lombok.Setter;

public abstract class Warp {
	
	@Getter private String name;
	private ItemStack icon;
	@Getter @Setter private String prefix, suffix;
	private List<String> lores = new ArrayList<String>();
	
	private static List<WarpingPlayer> warpingPlayers = new ArrayList<WarpingPlayer>();
	
	public Warp(String name){
		this.name = name;
		this.icon = new ItemStack(Material.CLAY_BALL);
	}
	
	public abstract WarpsContainer getContainer();
	
	
	public void setName(String name){
		Validate.checkNotNull(name);
		
		getContainer().removeWarp(this);
		
		this.name = name;
		
		getContainer().addWarp(this);
	}
	
	public void setIcon(ItemStack icon){
		Validate.checkNotNull(icon);
		
		this.icon = icon.clone();
	}
	
	public ItemStack getIcon(){
		return this.icon.clone();
	}
	
	public void addLore(String lore){
		Validate.checkNotNull(lore);
		
		this.lores.add(lore);
	}
	
	public boolean removeLore(String lore){
		return this.lores.remove(lore);
	}
	
	public void removeLore(int line){
		this.lores.remove(line-1);
	}
	
	public void removeAllLore(){
		this.lores.clear();
	}
	
	public List<String> getLore(){
		return new ArrayList<>(this.lores);
	}
	
	public Location getTeleportLocation(){
		return getContainer().getProvider().getWarpLocation(getName());
	}
	
	public void warp(Player player){
		double delay = 0;// EssentialsWarpGUI.es.getConfig().getDouble("teleport-delay");
		
		// teleport in delay
		if(delay > 0){
			
			WarpingPlayer wp = getWarpingPlayer(player);
			
			if(wp != null){
				player.sendMessage(Message.Teleporting_Stopped.getValue().replace("{warp}", wp.getWarp().getName()));
				wp.cancel();
			}
			
			if(Util.isInteger(delay))
				player.sendMessage(Message.Teleporting_Secounds.getValue().replace("{warp}", getName()).replace("{seconds}", "" + (int) delay));
			else
				player.sendMessage(Message.Teleporting_Secounds.getValue().replace("{warp}", getName()).replace("{seconds}", "" + delay));
			
			
			warpingPlayers.add(WarpingPlayer.create(player, this, (long) delay));
			
			
		// teleport instantly
		}else{
			player.sendMessage(Message.Teleporting.getValue().replace("{warp}", getName()));
			
			try{
				player.teleport(EssentialsWarpGUI.instance.getContainer().getProvider().getWarpLocation(getName()));
			}catch(Exception e){
				player.sendMessage(ChatColor.RED + e.getMessage());
			}
		}
	}
	
	public boolean hasUsePermission(CommandSender sender){
		return this.getContainer().getProvider().hasUsePermission(sender, this.getName());
	}
	
	public static WarpingPlayer getWarpingPlayer(Player player){
		
		for(WarpingPlayer wp:warpingPlayers){
			if(wp.getPlayer().equals(player))
				return wp;
		}
		
		return null;
	}
	
	
	
	
	public static class WarpingPlayer {
		
		private Player player;
		private Warp warp;
		private BukkitRunnable task;
		
		public WarpingPlayer(Player player, Warp warp, BukkitRunnable task){
			this.player = player;
			this.warp = warp;
			this.task = task;
		}
		
		public void setTask(BukkitRunnable task){ this.task = task; }
		
		public Player getPlayer(){ return this.player; }
		public Warp getWarp(){ return this.warp; }
		public BukkitRunnable getTask(){ return this.task; }
		
		public void cancel(){
			task.cancel();
			Warp.warpingPlayers.remove(this);
		}
		
		public static WarpingPlayer create(final Player player, final Warp warp, long delay){
			final WarpingPlayer wp = new WarpingPlayer(player, warp, null);
			
			wp.setTask(new BukkitRunnable(){
				public void run(){
					try{
						player.teleport(warp.getTeleportLocation());
					}catch(Exception e){
						e.printStackTrace();
						player.sendMessage(ChatColor.RED + e.getMessage());
					}
					
					Warp.warpingPlayers.remove(wp);
				}
			});
			
			wp.getTask().runTaskLater(EssentialsWarpGUI.instance, delay * 20);
			
			return wp;
		}
	}
}
