/**
* Adds an GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @version 1.4
* @website http://marcely.de/ 
*/

package de.marcely.warpgui;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

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
		public void onInventoryClick(InventoryClickEvent event){
			warp.onInventoryClick(event);
		}
	};
	
	public static ArrayList<Warp> getWarps(Player player){
		ArrayList<Warp> list = new ArrayList<Warp>();
		for(String warp:es.getWarps().getList()){
			if(player.hasPermission("essentials.warps." + warp.toLowerCase())){
				Warp w = warps.getWarp(warp);
				if(w == null)
					list.add(new Warp(warp, new ItemStack(Material.CLAY_BALL)));
				else
					list.add(w);
			}
		}
		return list;
	}
	
	public static String firstCharCaps(String str){
		if(CONFIG_FIRSTCHARCAPS == true)
			return Character.toUpperCase(str.charAt(0)) + str.substring(1);
		return str;
	}
	
	public static String getRealName(String name){
		for(String warp:es.getWarps().getList()){
			if(warp.equalsIgnoreCase(name))
				return warp;
		}
		return null;
	}
	
	public static ItemStack getItemStack(ItemStack is, String name){
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		is.setItemMeta(im);
		return is;
	}
	
	public static boolean isNumeric(String str){
		try{
			Integer.valueOf(str);
			return true;
		}catch(Exception e){ }
		return false;
	}
	
	public static String getVersion(){
		return plugin.getDescription().getVersion();
	}
}
