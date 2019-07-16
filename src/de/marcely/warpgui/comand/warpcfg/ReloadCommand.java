/**
* Adds a GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @website https://marcely.de/ 
*/

package de.marcely.warpgui.comand.warpcfg;

import org.bukkit.command.CommandSender;

import de.marcely.warpgui.EssentialsWarpGUI;
import de.marcely.warpgui.Message;
import de.marcely.warpgui.command.SubCommandExecutor;

public class ReloadCommand implements SubCommandExecutor {

	@Override
	public void onInvoke(CommandSender sender, String label, String[] args){
		EssentialsWarpGUI.instance.reloadConfig();
		
		sender.sendMessage(Message.Reloaded_Config.getValue());
	}
}
