/**
* Adds an GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @version 1.5.1
* @website http://marcely.de/ 
*/

package de.marcely.warpgui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.marcely.warpgui.library.Vault;

public class Util {
	
	public static List<Warp> getWarps(Player player){
		if(hasPermission(player, "essentials.warps.*"))
			return main.warps.getWarps();
		
		List<Warp> list = new ArrayList<Warp>();
		for(String warp:main.es.getWarps().getList()){
			if(hasPermission(player, "essentials.warps." + warp.toLowerCase())){
				Warp w = main.warps.getWarp(warp);
				if(w == null)
					list.add(new Warp(warp, new ItemStack(Material.CLAY_BALL)));
				else
					list.add(w);
			}
		}
		return list;
	}
	
	public static String firstCharCaps(String str){
		if(main.CONFIG_FIRSTCHARCAPS == true)
			return Character.toUpperCase(str.charAt(0)) + str.substring(1);
		return str;
	}
	
	public static String getRealName(String name){
		for(String warp:main.es.getWarps().getList()){
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
	
	public static String getItemStackName(ItemStack is){
		if(is == null || is.getItemMeta() == null)
			return null;
		else
			return is.getItemMeta().getDisplayName();
	}
	
	public static boolean hasPermission(CommandSender sender, String permission){
		if(sender instanceof Player){
			
			Player player = (Player) sender;
			Boolean bl = Vault.hasPermission(player, permission);
			if(bl != null)
				return bl;
			else
				return player.hasPermission(permission);
		}else
			return true;
	}
}
