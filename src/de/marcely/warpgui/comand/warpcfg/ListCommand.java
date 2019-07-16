/**
* Adds a GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @website https://marcely.de/ 
*/

package de.marcely.warpgui.comand.warpcfg;

import java.util.Collection;

import org.bukkit.command.CommandSender;

import de.marcely.warpgui.EssentialsWarpGUI;
import de.marcely.warpgui.Message;
import de.marcely.warpgui.command.SubCommandExecutor;
import de.marcely.warpgui.components.Warp;

public class ListCommand implements SubCommandExecutor {

	@Override
	public void onInvoke(CommandSender sender, String label, String[] args){
		final Collection<Warp> list = EssentialsWarpGUI.instance.getContainer().getWarps();
		final StringBuilder builder = new StringBuilder();
		
		int i=0;
		for(Warp warp:list){
			builder.append(warp.getName());
			
			if(++i < list.size())
				builder.append(", ");
		}
		
		sender.sendMessage(Message.List_Warps.getValue().replace("{warps}", builder.toString()));
	}
}
