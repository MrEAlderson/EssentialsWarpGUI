/**
* Adds a GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @website https://marcely.de/ 
*/

package de.marcely.warpgui.config;

import de.marcely.configmanager2.EZConfigManager;
import de.marcely.warpgui.EssentialsWarpGUI;
import de.marcely.warpgui.util.StringUtil;
import de.marcely.warpgui.util.Util;
import org.bukkit.ChatColor;

public class BaseConfig {
	
	public static EZConfigManager manager = new EZConfigManager(Util.FILE_CONFIG_BASE, false);
	
	public static void load(){
		manager.load();
		
		final String version = manager.getDescription("config-version");
		
		{
			ConfigValue.inventory_title = StringUtil.translateToColorCodes(
					manager.getConfigString("inv-title", "&3Warps"));
			ConfigValue.include_command_warps = manager.getConfigBoolean("includecmd-warps", true);
			ConfigValue.gui_height = manager.getConfigInt("gui-height", 0);
		}
		
		if(version == null || version != null && !version.equals(EssentialsWarpGUI.getVersion()))
			save();
	}
	
	public static void save(){
		manager.clear();
		manager.addDescription("config-version", EssentialsWarpGUI.getVersion());
		
		manager.addEmptyLine();
		
		manager.addComment("Set the title from the inventory");
		manager.addConfig("inv-title", StringUtil.formattedChatColorStringToReadable(ConfigValue.inventory_title));
		
		manager.addEmptyLine();
		
		manager.addComment("If it's enabled, /warps will open the GUI too");
		manager.addConfig("includecmd-warps", ConfigValue.include_command_warps);

		manager.addEmptyLine();

		manager.addComment("The minimum height of the gui. Shouldn't be greater than 6");
		manager.addConfig("gui-height", ConfigValue.gui_height);
		
		manager.save();
	}
}
