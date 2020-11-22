/**
* Adds a GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @website https://marcely.de/ 
*/

package de.marcely.warpgui.comand.warpcfg;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import de.marcely.warpgui.EssentialsWarpGUI;
import de.marcely.warpgui.Message;
import de.marcely.warpgui.command.SubCommand;
import de.marcely.warpgui.command.SubCommandExecutor;
import de.marcely.warpgui.command.WarpcfgCommand;

public class HelpCommand implements SubCommandExecutor {
	
	private final WarpcfgCommand parent;
	
	public HelpCommand(WarpcfgCommand parent){
		this.parent = parent;
	}
	
	@Override
	public void onInvoke(CommandSender sender, String label, String[] args){
		sender.sendMessage(ChatColor.YELLOW + " ------------ " + ChatColor.GOLD + "Commands" + ChatColor.YELLOW + " ------------ ");
		
		for(SubCommand cmd:parent.getSubCommands())
			sender.sendMessage(ChatColor.DARK_AQUA + "/warpcfg " + cmd.getName() + " " + ChatColor.AQUA + cmd.getParameter());
		
		sender.sendMessage("");
		sender.sendMessage(Message.Info_MadeBy.getValue().replace("{info}", "Marcely1199"));
		sender.sendMessage(Message.Info_Version.getValue().replace("{info}", "" + EssentialsWarpGUI.getVersion()));
	}
}
