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

public class SuffixCommand implements SubCommandExecutor {

	@Override
	public void onInvoke(CommandSender sender, String label, String[] args){
		if(args.length == 0){
			sender.sendMessage(Message.Usage.getValue().replace("{usage}", "/warpcfg suffix <warp name> [...]"));
			
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
			for(int i=0; i<7; i++)
				sender.sendMessage("");
			
			if(warp.getSuffix() != null)
				sender.sendMessage(ChatColor.GRAY + "Suffix: " + ChatColor.WHITE + warp.getSuffix());
			else
				sender.sendMessage(Message.No_Suffix.getValue().replace("{warp}", warp.getName()));
			
			sender.sendMessage("");
			sender.sendMessage(Message.Usage_Change_Suffix.getValue().replace("{usage}", "/" + label + " suffix <warp name> set <suffix>"));
			sender.sendMessage(Message.Usage_Remove_Suffix.getValue().replace("{usage}", "/" + label + " suffix <warp name> remove"));
			
			return;
		}
		
		switch(args[1].toLowerCase()){
		case "set":
			{
				if(args.length == 2){
					sender.sendMessage(Message.Usage.getValue().replace("{usage}", "/warpcfg suffix " + warp.getName() + " set <suffix>"));
					return;
				}
				
				warp.setSuffix(StringUtil.join(" ", Arrays.copyOfRange(args, 2, args.length)));
				WarpsConfig.save();
				
				sender.sendMessage(Message.Changed_Suffix.getValue().replace("{warp}", warp.getName()));
			}
			break;
			
		case "remove":
			{
				warp.setSuffix(null);
				WarpsConfig.save();
				
				sender.sendMessage(Message.Removed_Suffix.getValue().replace("{warp}", warp.getName()));
			}
			break;
		}
	}
}
