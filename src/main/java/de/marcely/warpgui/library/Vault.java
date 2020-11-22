/**
* Adds a GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @website https://marcely.de/ 
*/

package de.marcely.warpgui.library;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicesManager;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class Vault implements Library {
	
    public Economy economy = null;
    public Permission permission = null;
    public Chat chat = null;
    
    @Override
	public LibraryType getType(){
		return LibraryType.VAULT;
	}

	@Override
	public void load() {
		final ServicesManager services = Bukkit.getServer().getServicesManager();
		
		{
	    	if(services.getRegistration(Economy.class) != null)
	    		economy = services.getRegistration(Economy.class).getProvider();
	    	
	    	if(services.getRegistration(Permission.class) != null)
	    		permission = services.getRegistration(Permission.class).getProvider();
	    	
	    	if(services.getRegistration(Chat.class) != null)
	    		chat = services.getRegistration(Chat.class).getProvider();
		}
	}

	@Override
	public void unload(){ }
	
	public void giveMoney(Player player, double amount){
		if(economy != null && economy.isEnabled())
			economy.depositPlayer(player, amount);
	}
	
	public Boolean hasPermission(Player player, String perm){
		if(permission != null && permission.isEnabled())
			return permission.has(player, perm);
		
		return null;
	}
	
	public String getPrefix(Player player){
		return getPrefix(player, false);
	}
	
	public String getPrefix(Player player, boolean returnEmpty){
		if(!returnEmpty && chat != null && chat.getPlayerPrefix(player) != null)
			return chat.getPlayerPrefix(player);
		
		return "";
	}
}
