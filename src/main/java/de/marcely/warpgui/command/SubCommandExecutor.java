/**
* Adds a GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @website https://marcely.de/ 
*/

package de.marcely.warpgui.command;

import org.bukkit.command.CommandSender;

public interface SubCommandExecutor {
	
	public void onInvoke(CommandSender sender, String label, String[] args);
}
