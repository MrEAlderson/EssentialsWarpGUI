/**
* Adds a GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @version 1.6.1
* @website http://marcely.de/ 
*/

package de.marcely.warpgui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Warp implements Serializable {
	private static final long serialVersionUID = 100042053024876811L;
	
	private String name;
	private String iconName;
	private short iconID;
	private String prefix = "";
	private List<String> lores = new ArrayList<String>();
	
	private static List<WarpingPlayer> warpingPlayers = new ArrayList<WarpingPlayer>();
	
	public Warp(String name, ItemStack icon){
		setName(name);
		setIcon(icon);
	}
	
	public Warp(String name, ItemStack icon, String prefix){
		setName(name);
		setIcon(icon);
		setPrefix(prefix);
	}
	
	public void addLore(String lore){
		this.lores.add(lore);
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setIcon(ItemStack icon){
		this.iconName = icon.getType().name();
		this.iconID = icon.getDurability();
	}
	
	public void setPrefix(String prefix){
		this.prefix = prefix;
	}
	
	public String getName(){
		return this.name;
	}
	
	public ItemStack getIcon(){
		return new ItemStack(Material.getMaterial(this.iconName), 1, this.iconID);
	}
	
	public String getPrefix(){
		return this.prefix;
	}
	
	public List<String> getLores(){
		return this.lores;
	}
	
	public boolean removeLore(String lore){
		return this.lores.remove(lore);
	}
	
	public void warp(Player player){
		double delay = EssentialsWarpGUI.es.getConfig().getDouble("teleport-delay");
		
		// teleport in delay
		if(delay > 0){
			
			WarpingPlayer wp = getWarpingPlayer(player);
			
			if(wp != null){
				player.sendMessage(Language.Teleporting_Stopped.getMessage().replace("{warp}", wp.getWarp().getName()));
				wp.cancel();
			}
			
			if(Util.isInteger(delay))
				player.sendMessage(Language.Teleporting_Secounds.getMessage().replace("{warp}", Util.firstCharCaps(getName())).replace("{seconds}", "" + (int) delay));
			else
				player.sendMessage(Language.Teleporting_Secounds.getMessage().replace("{warp}", Util.firstCharCaps(getName())).replace("{seconds}", "" + delay));
			
			
			warpingPlayers.add(WarpingPlayer.create(player, this, (long) delay));
			
			
		// teleport instantly
		}else{
			player.sendMessage(Language.Teleporting.getMessage().replace("{warp}", Util.firstCharCaps(getName())));
			try{
				player.teleport(EssentialsWarpGUI.es.getWarps().getWarp(getName()));
			}catch(Exception e){
				player.sendMessage(ChatColor.RED + e.getMessage());
			}
		}
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
						player.teleport(EssentialsWarpGUI.es.getWarps().getWarp(warp.getName()));
					}catch(Exception e){
						player.sendMessage(ChatColor.RED + e.getMessage());
					}
					Warp.warpingPlayers.remove(wp);
				}
			});
			
			wp.getTask().runTaskLater(EssentialsWarpGUI.plugin, delay * 20);
			
			return wp;
		}
	}
}
