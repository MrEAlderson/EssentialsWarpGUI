/**
* Adds a GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @version 1.6
* @website http://marcely.de/ 
*/

package de.marcely.warpgui.config;

import de.marcely.warpgui.Language;
import de.marcely.warpgui.main;

public class Config {
	public static ConfigManager manager = new ConfigManager(main.plugin.getName(), "config.yml");
	
	public static void load(){
		manager.load();
		String version = manager.getConfigString("config-version");
		String invtitle = manager.getConfigString("inv-title");
		boolnull firstcharcaps = manager.getConfigBoolean("first-char-caps");
		boolnull incl_warps = manager.getConfigBoolean("includecmd-warps");
		
		if(invtitle != null)
			main.CONFIG_INVTITLE = Language.stringToChatColor(invtitle);
		if(firstcharcaps != boolnull.NULL)
			main.CONFIG_FIRSTCHARCAPS = firstcharcaps.toBoolean();
		if(incl_warps != boolnull.NULL)
			main.CONFIG_INCLCMD_WARPS = incl_warps.toBoolean();
		
		if(version == null || version != null && !version.equals(main.getVersion()))
			save();
	}
	
	public static void save(){
		manager.clear();
		manager.addComment("Don't change this");
		manager.addConfig("config-version", main.getVersion());
		
		manager.addEmptyLine();
		
		manager.addComment("Set the title from the inventory");
		manager.addConfig("inv-title", Language.chatColorToString(main.CONFIG_INVTITLE));
		
		manager.addEmptyLine();
		
		manager.addComment("If it's enabled, the first character in the name of the warp is in caps");
		manager.addConfig("first-char-caps", main.CONFIG_FIRSTCHARCAPS);
		
		manager.addEmptyLine();
		
		manager.addComment("If it's enabled, /warps will open the GUI too");
		manager.addConfig("includecmd-warps", main.CONFIG_INCLCMD_WARPS);
		
		manager.save();
	}
}
