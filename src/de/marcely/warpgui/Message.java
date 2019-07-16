/**
* Adds a GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @website https://marcely.de/ 
*/

package de.marcely.warpgui;

import org.bukkit.ChatColor;

import de.marcely.warpgui.util.Validate;
import lombok.Getter;

public enum Message {
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
	
	@Getter private final String defaultValue;
	
	@Getter private String value;
	
	private Message(String defaultValue){
		this.defaultValue = defaultValue;
		this.value = defaultValue;
	}
	
	public void setValue(String value){
		Validate.checkNotNull(value);
		
		this.value = value;
	}
	
	public static Message getMessage(String str){
		for(Message msg:Message.values()){
			if(msg.name().equalsIgnoreCase(str))
				return msg;
		}
		
		return null;
	}
}
