/**
* Adds a GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @website https://marcely.de/ 
*/

package de.marcely.warpgui.util;

import java.io.File;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.marcely.warpgui.config.ConfigValue;
import de.marcely.warpgui.library.Vault;

public class Util {
	
	public static final File FOLDER_BASE = new File("plugins/Essentials_WarpGUI");
	
	public static final File FILE_CONFIG_BASE = new File(FOLDER_BASE, "config.yml");
	public static final File FILE_CONFIG_MESSAGES = new File(FOLDER_BASE, "messages.yml");
	public static final File FILE_CONFIG_WARPS = new File(FOLDER_BASE, "warps.yml");
	
	@Deprecated public static final File FILE_CONFIG_WARPS_OLD = new File(FOLDER_BASE, "warps.cfg");
	
	
	public static File[] getFolders(){
		return new File[]{ FOLDER_BASE };
	}
	
	public static String firstCharCaps(String str){
		if(ConfigValue.first_char_caps == true)
			return Character.toUpperCase(str.charAt(0)) + str.substring(1);
		return str;
	}
	
	public static boolean isInteger(String str){
		try{
			Integer.valueOf(str);
			return true;
		}catch(Exception e){ }
		return false;
	}
	
	public static boolean isInteger(double d){
		return String.valueOf(d).endsWith(".0");
	}
	
	public static boolean hasPermission(CommandSender sender, String permission){
		if(sender instanceof Player){
			final Player player = (Player) sender;
			
			if(player.isOp())
				return true;
			
			Boolean bl = Vault.hasPermission(player, permission);
			if(bl != null)
				return bl;
			else
				return Vault.hasPermission(player, permission);
		}else
			return true;
	}
}
