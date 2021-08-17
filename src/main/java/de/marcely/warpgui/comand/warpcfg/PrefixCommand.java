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

public class PrefixCommand implements SubCommandExecutor {

	@Override
	public void onInvoke(CommandSender sender, String label, String[] args){
		if(args.length == 0){
			sender.sendMessage(Message.Usage.getValue().replace("{usage}", "/warpcfg prefix <warp name> [...]"));
			
			return;
		}
		
		final WarpsContainer container = EssentialsWarpGUI.instance.getContainer();
		
		container.synchronizeWithProvider();
		
		final Warp warp = container.getWarp(args[0]);
		
		if(warp == null){
			sender.sendMessage(Message.DoesntExist_Warp.getValue().replace("{warp}", args[0]));
			
			return;
		}

		final Runnable sendHelp = () -> {
			for(int i=0; i<7; i++)
				sender.sendMessage("");

			if(warp.getPrefix() != null)
				sender.sendMessage(ChatColor.GRAY + "Prefix: " + ChatColor.WHITE + warp.getPrefix());
			else
				sender.sendMessage(Message.No_Prefix.getValue().replace("{warp}", warp.getName()));

			sender.sendMessage("");
			sender.sendMessage(Message.Usage_Change_Prefix.getValue()
					.replace("{usage}", "/" + label + " prefix <warp name> set <prefix>"));
			sender.sendMessage(Message.Usage_Remove_Prefix.getValue()
					.replace("{usage}", "/" + label + " prefix <warp name> remove"));
		};

		if(args.length == 1){
			sendHelp.run();
			return;
		}
		
		switch(args[1].toLowerCase()){
		case "set":
			{
				if(args.length == 2){
					sender.sendMessage(Message.Usage.getValue().replace("{usage}", "/warpcfg prefix " + warp.getName() + " set <prefix>"));
					return;
				}
				
				warp.setPrefix(StringUtil.join(" ", Arrays.copyOfRange(args, 2, args.length)));
				WarpsConfig.save();
				
				sender.sendMessage(Message.Changed_Prefix.getValue().replace("{warp}", warp.getName()));
			}
			break;
			
		case "remove":
			{
				warp.setPrefix(null);
				WarpsConfig.save();
				
				sender.sendMessage(Message.Removed_Prefix.getValue().replace("{warp}", warp.getName()));
			}
			break;

		default:
			sendHelp.run();
			break;
		}
	}
}
