/**
* Adds a GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @website https://marcely.de/ 
*/

package de.marcely.warpgui.comand.warpcfg;

import de.marcely.warpgui.EssentialsWarpGUI;
import de.marcely.warpgui.Message;
import de.marcely.warpgui.command.SubCommandExecutor;
import de.marcely.warpgui.components.Warp;
import de.marcely.warpgui.components.WarpsContainer;
import de.marcely.warpgui.config.WarpsConfig;
import de.marcely.warpgui.util.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class DisplayNameCommand implements SubCommandExecutor {

	@Override
	public void onInvoke(CommandSender sender, String label, String[] args){
		if(args.length == 0){
			sender.sendMessage(Message.Usage.getValue().replace("{usage}", "/warpcfg displayname <warp name> [...]"));
			
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

			if(warp.getDisplayName() != null)
				sender.sendMessage(ChatColor.GRAY + "Display name: " + ChatColor.WHITE + warp.getDisplayName());
			else
				sender.sendMessage(Message.No_DisplayName.getValue().replace("{warp}", warp.getName()));

			sender.sendMessage("");
			sender.sendMessage(Message.Usage_Change_DisplayName.getValue()
					.replace("{usage}", "/" + label + " displayname <warp name> set <name>"));
			sender.sendMessage(Message.Usage_Remove_DisplayName.getValue()
					.replace("{usage}", "/" + label + " displayname <warp name> remove"));
		};

		if(args.length == 1){
			sendHelp.run();
			return;
		}
		
		switch(args[1].toLowerCase()){
		case "set":
			{
				if(args.length == 2){
					sender.sendMessage(Message.Usage.getValue().replace("{usage}", "/warpcfg displayname " + warp.getName() + " set <name>"));
					return;
				}
				
				warp.setDisplayName(StringUtil.join(" ", Arrays.copyOfRange(args, 2, args.length)));
				WarpsConfig.save();
				
				sender.sendMessage(Message.Changed_DisplayName.getValue().replace("{warp}", warp.getName()));
			}
			break;
			
		case "remove":
			{
				warp.setDisplayName(null);
				WarpsConfig.save();
				
				sender.sendMessage(Message.Removed_DisplayName.getValue().replace("{warp}", warp.getName()));
			}
			break;

		default:
			sendHelp.run();
			break;
		}
	}
}
