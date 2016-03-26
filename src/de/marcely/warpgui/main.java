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
import org.bukkit.plugin.java.JavaPlugin;

import de.marcely.warpgui.command.warp;
import de.marcely.warpgui.config.WarpConfig;
import de.marcely.warpgui.config.config;
import net.ess3.api.IEssentials;

public class main extends JavaPlugin {
	public static String version = "1.3";
	
	public static IEssentials es = null;
	
	public static String CONFIG_INVTITLE = ChatColor.DARK_AQUA + "Warps";
	public static boolean CONFIG_FIRSTCHARCAPS = false;
	public static LanguageType CONFIG_LANGUAGE = LanguageType.English;
	
	public static WarpConfig warps = new WarpConfig();
	
	@Override
	public void onEnable(){
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
		
		config.load();
		if(WarpConfig.exists()) warps = WarpConfig.load();
	}
	
	@Override
	public void onDisable(){
		
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
}
