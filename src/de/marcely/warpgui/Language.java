/**
* Adds a GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @version 1.6
* @website http://marcely.de/ 
*/

package de.marcely.warpgui;

import java.util.HashMap;

import org.bukkit.ChatColor;

public enum Language {
	No_Permissions(ChatColor.RED + "You have got no permissions for this command!"),
	No_Prefix(ChatColor.RED + "The warp " + ChatColor.DARK_RED + "{warp} " + ChatColor.RED + "has no prefix"),
	No_Warps(ChatColor.RED + "There're no avaible warps for you!"),
	DoesntExist_Warp(ChatColor.RED + "The warp " + ChatColor.DARK_RED + "{warp} " + ChatColor.RED + "doesn't exist"),
	NotA_Player(ChatColor.RED + "You are not a player!"),
	NotA_Number(ChatColor.DARK_RED + "{number} " + ChatColor.RED + "is not a number!"),
	Changed_Icon(ChatColor.GREEN + "The icon has been changed to " + ChatColor.DARK_GREEN + "{icon}"),
	Changed_Prefix(ChatColor.GREEN + "The prefix by the warp " + ChatColor.DARK_GREEN + "{warp}" + ChatColor.GREEN + " has been changed"),
	Removed_Prefix(ChatColor.GREEN + "The prefix by the warp " + ChatColor.DARK_GREEN + "{warp}" + ChatColor.GREEN + " has been removed"),
	Added_Lore(ChatColor.GREEN + "A lore has been added to the warp " + ChatColor.DARK_GREEN + "{warp}"),
	Removed_Lore(ChatColor.GREEN + "The lore with the ID " + ChatColor.DARK_GREEN + "{id} " + ChatColor.GREEN + "has been removed from the warp " + ChatColor.DARK_GREEN + "{warp}"),
	Unkown_Material(ChatColor.RED + "Unkown material " + ChatColor.DARK_RED + "{material}"),
	Unkown_Argument(ChatColor.RED + "Unkown argument " + ChatColor.DARK_RED + "{arg}"),
	Unkown_ID(ChatColor.RED + "Unkown ID " + ChatColor.DARK_RED + "{id}"),
	Usage(ChatColor.GOLD + "Usage: " + ChatColor.YELLOW + "{usage}"),
	Usage_Add_Lore(ChatColor.GOLD + "Write " + ChatColor.YELLOW + "{usage} " + ChatColor.GOLD + "to add a lore"),
	Usage_Remove_Lore(ChatColor.GOLD + "Write " + ChatColor.YELLOW + "{usage} " + ChatColor.GOLD + "to remove a lore"),
	Usage_Change_Prefix(ChatColor.GOLD + "Write " + ChatColor.YELLOW + "{usage} " + ChatColor.GOLD + "to change the prefix"),
	Usage_Remove_Prefix(ChatColor.GOLD + "Write " + ChatColor.YELLOW + "{usage} " + ChatColor.GOLD + "to remove the prefix"),
	Reloaded_Config(ChatColor.GREEN + "The config has been successfully reloaded!"),
	Teleporting(ChatColor.GOLD + "Teleporting to " + ChatColor.YELLOW + "{warp}" + ChatColor.GOLD + "..."),
	Teleporting_Secounds(ChatColor.GOLD + "Teleporting to " + ChatColor.YELLOW + "{warp}" + ChatColor.GOLD + " in " + ChatColor.YELLOW + "{seconds}" + ChatColor.GOLD + " seconds..."),
	Teleporting_Stopped(ChatColor.RED + "Stopped teleport to " + ChatColor.DARK_RED + "{warp}"),
	List_Warps(ChatColor.GOLD + "Avaible warps: " + ChatColor.YELLOW + "{warps}"),
	Info_MadeBy(ChatColor.GREEN + "Made by " + ChatColor.DARK_GREEN + "{info}"),
	Info_Version(ChatColor.GREEN + "Version " + ChatColor.DARK_GREEN + "{info}");
	
	private String selected_msg;
	private static HashMap<Language, String> translations = new HashMap<Language, String>();
	
	private Language(String msg){
		this.selected_msg = msg;
	}
	
	public String getMessage(){
		if(translations.containsKey(this))
			return translations.get(this);
		else
			return this.selected_msg;
	}
	
	public static String chatColorToString(String str){
		for(ChatColor c:ChatColor.values()){
			str = str.replace("" + c, "&" + c.getChar());
		}
		
		return str;
	}
	
	public static String stringToChatColor(String str){
		for(ChatColor c:ChatColor.values()){
			str = str.replace("&" + c.getChar(), "" + c);
		}
		
		return str;
	}
	
	public static Language getLanguage(String str){
		for(Language l:Language.values()){
			if(l.name().equalsIgnoreCase(str) ||
			   l.getMessage().equalsIgnoreCase(str))
				return l;
		}
		
		return null;
	}
	
	public static void setTranslation(Language language, String message){
		if(language == null || message == null){
			new NullPointerException().printStackTrace();
			return;
		}
		if(translations.containsKey(language))
			translations.replace(language, message);
		else
			translations.put(language, message);
	}
}
