/**
* Adds a GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @version 1.6
* @website http://marcely.de/ 
*/

package de.marcely.warpgui;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import de.marcely.warpgui.Warp.WarpingPlayer;
import de.marcely.warpgui.command.warp;
import de.marcely.warpgui.config.LanguageConfig;
import de.marcely.warpgui.config.WarpConfig;
import de.marcely.warpgui.config.Config;
import net.ess3.api.IEssentials;

public class main extends JavaPlugin {	
	public static Plugin plugin;
	
	public static IEssentials es = null;
	
	public static String CONFIG_INVTITLE = ChatColor.DARK_AQUA + "Warps";
	public static boolean CONFIG_FIRSTCHARCAPS = false;
	public static boolean CONFIG_INCLCMD_WARPS = true;
	
	public static WarpConfig warps = new WarpConfig();
	
	public void onEnable(){
		plugin = this;
		
		// get essentials variable
		es = (IEssentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
		if(es == null) // if he isn't using the spigot version of spigot
			es = (IEssentials) Bukkit.getServer().getPluginManager().getPlugin("EssentialsX");
		if(es == null) // he isn't using essentials or essentialsX
			new NullPointerException("You aren't using the spigot version of Essentials!").printStackTrace();
		
		// setup
		getServer().getPluginManager().registerEvents(listener, this);
		getCommand("warp").setExecutor(new de.marcely.warpgui.command.warp());
		getCommand("warpcfg").setExecutor(new de.marcely.warpgui.command.warpcfg());
		
		// load config
		File dir = new File("plugins/Essentials_WarpGUI");
		if(!dir.exists()) dir.mkdir();
		
		Config.load();
		LanguageConfig.load();
		if(WarpConfig.exists()) warps = WarpConfig.load();
	}
	
	private Listener listener = new Listener(){
		@EventHandler
		public void onInventoryClickEvent(InventoryClickEvent event){
			warp.onInventoryClickEvent(event);
		}
		
		@EventHandler
		public void onInventoryDragEvent(InventoryDragEvent event){
			warp.onInventoryDragEvent(event);
		}
		
		@EventHandler
		public void onPlayerMoveEvent(PlayerMoveEvent event){
			Location from = event.getFrom();
			Location to = event.getTo();
			
			if(from.getBlockX() == to.getBlockX() &&
			   from.getBlockZ() == to.getBlockZ() &&
			   from.distance(to) < 0.42)
			return;
			
			WarpingPlayer wp = Warp.getWarpingPlayer(event.getPlayer());
			
			if(wp != null){
				event.getPlayer().sendMessage(Language.Teleporting_Stopped.getMessage().replace("{warp}", wp.getWarp().getName()));
				wp.cancel();
			}
		}
		
		@EventHandler
		public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event){
			String[] strs = event.getMessage().split(" ");
			String label = strs[0].replace("/", "");
			String[] args = new String[strs.length - 1];
			
			for(int i=1; i<strs.length; i++)
				args[i - 1] = strs[i];
			
			if(CONFIG_INCLCMD_WARPS && label.equalsIgnoreCase("warps")){
				warp.onCommand(event.getPlayer(), label, args);
				event.setCancelled(true);
			}
		}
	};
	
	public static String getVersion(){
		return plugin.getDescription().getVersion();
	}
}
