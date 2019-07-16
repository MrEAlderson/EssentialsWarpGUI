/**
* Adds a GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @website https://marcely.de/ 
*/

package de.marcely.warpgui.comand.warpcfg;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import de.marcely.warpgui.EssentialsWarpGUI;
import de.marcely.warpgui.Message;
import de.marcely.warpgui.command.SubCommandExecutor;
import de.marcely.warpgui.components.Warp;
import de.marcely.warpgui.components.WarpsContainer;
import de.marcely.warpgui.config.WarpsConfig;
import de.marcely.warpgui.util.StringUtil;
import de.marcely.warpgui.util.Util;

public class LoreCommand implements SubCommandExecutor {

	@Override
	public void onInvoke(CommandSender sender, String label, String[] args){
		if(args.length == 0){
			sender.sendMessage(Message.Usage.getValue().replace("{usage}", "/warpcfg lore <warp name> [...]"));
			
			return;
		}
		
		final WarpsContainer container = EssentialsWarpGUI.instance.getContainer();
		
		container.synchronizeWithProvider();
		
		final Warp warp = container.getWarp(args[0]);
		
		if(warp == null){
			sender.sendMessage(Message.DoesntExist_Warp.getValue().replace("{warp}", args[0]));
			
			return;
		}
		
		if(args.length == 1){
			sender.sendMessage(ChatColor.DARK_AQUA + "Lore:");
			
			int i=1;
			for(String lore:warp.getLore())
				sender.sendMessage("" + ChatColor.DARK_GREEN + (i++) + ChatColor.GREEN + " " + lore);
			
			sender.sendMessage("");
			sender.sendMessage(Message.Usage_Add_Lore.getValue().replace("{usage}", "/" + label + " lore " + warp.getName() + " add <lore>"));
			sender.sendMessage(Message.Usage_Remove_Lore.getValue().replace("{usage}", "/" + label + " lore " + warp.getName() + " remove <line>"));
			
			return;
		}
		
		switch(args[1].toLowerCase()){
		case "add":
			{
				if(args.length == 2){
					sender.sendMessage(Message.Usage.getValue().replace("{usage}", "/warpcfg lore " + warp.getName() + " add <lore>"));
					return;
				}
				
				warp.addLore(StringUtil.join(" ", Arrays.copyOfRange(args, 2, args.length)));
				WarpsConfig.save();
				
				sender.sendMessage(Message.Added_Lore.getValue().replace("{warp}", warp.getName()));
			}
			break;
			
		case "remove":
			{
				if(args.length == 2){
					sender.sendMessage(Message.Usage.getValue().replace("{usage}", "/warpcfg lore " + warp.getName() + " remove <line>"));
					return;
				}
				
				if(!Util.isInteger(args[2])){
					sender.sendMessage(Message.NotA_Number.getValue().replace("{number}", args[2]));
					return;
				}
				
				final int line = Integer.parseInt(args[2]);
				
				if(line <= 0 || line > warp.getLore().size()){
					sender.sendMessage(Message.Unkown_Line.getValue().replace("{line}", args[2]));
					return;
				}
				
				warp.removeLore(line);
				WarpsConfig.save();
				
				sender.sendMessage(Message.Removed_Lore.getValue().replace("{id}", args[2]).replace("{warp}", warp.getName()));
			}
			break;
		}
	}
}
