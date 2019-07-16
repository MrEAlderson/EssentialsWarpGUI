/**
* Adds a GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @website https://marcely.de/ 
*/

package de.marcely.warpgui;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import de.marcely.warpgui.config.MessagesConfig;
import de.marcely.warpgui.config.WarpConfig;
import de.marcely.warpgui.config.WarpsConfig;
import de.marcely.warpgui.library.LibraryType;
import de.marcely.warpgui.library.WarpsProvider;
import de.marcely.warpgui.util.Util;
import lombok.Getter;
import de.marcely.warpgui.command.WarpCommand;
import de.marcely.warpgui.components.WarpsContainer;
import de.marcely.warpgui.config.BaseConfig;

@SuppressWarnings("deprecation")
public class EssentialsWarpGUI extends JavaPlugin {	
	
	public static EssentialsWarpGUI instance;
	
	@Getter private WarpsContainer container;
	
	public void onEnable(){
		instance = this;
		
		LibraryType.initAll();
		
		// search for provider & create container
		{
			final WarpsProvider provider = LibraryType.findInstanceWithInterface(WarpsProvider.class);
			
			if(provider == null){
				this.getLogger().warning("An error occured: Failed to find a proper provider for the warps (Essentials is missing). Stopping the plugin");
				Bukkit.getPluginManager().disablePlugin(this);
				
				return;
			}
			
			this.container = new WarpsContainer(provider);
		}
		
		// register listener & commands
		{
			getServer().getPluginManager().registerEvents(new EventsManager(), this);
			getServer().getPluginManager().registerEvents(new de.marcely.ezgui.EventsManager(), this);
			
			getCommand("warp").setExecutor(new WarpCommand());
			// getCommand("warpcfg").setExecutor(new de.marcely.warpgui.command.warpcfg());
		}
		
		// prepare filesystem & load configs
		{
			for(File folder:Util.getFolders())
				folder.mkdir();
			
			BaseConfig.load();
			MessagesConfig.load();
			WarpsConfig.load();
			
			// load&convert old warps if they still exist
			if(WarpConfig.exists()){
				getLogger().info("Found old warps! Converting them...");
				
				{
					WarpConfig config = WarpConfig.load();
					
					for(de.marcely.warpgui.Warp old:config.warps){
						getLogger().info("Converting the warp '" + old.getName() + "'");
						this.container.addWarp(old.convertToNew());
					}
				}
				
				Util.FILE_CONFIG_WARPS_OLD.delete();
				WarpsConfig.save();
			}
		}
	}
	
	public static String getVersion(){
		return instance.getDescription().getVersion();
	}
}
