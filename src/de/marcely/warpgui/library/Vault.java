/**
* Adds a GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @version 1.6
* @website http://marcely.de/ 
*/

package de.marcely.warpgui.library;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;

public class Vault {
    public static Economy economy = null;
    public static net.milkbowl.vault.permission.Permission permission = null;
    public static Chat chat = null;
    
    //private static Chat chat = null;
    
	public static void onEnable(){
        // permissions
		/*RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }*/
        
        // enconomy
		
        if(Bukkit.getServer().getPluginManager().getPlugin("Vault") != null){
        	if(Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class) != null)
        		economy = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class).getProvider();
        	if(Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class) != null)
        		permission = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class).getProvider();
        	if(Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class) != null)
        		chat = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class).getProvider();
        }
        // chat
        /*RegisteredServiceProvider<Chat> chatProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }*/
	}
	
	public static void giveMoney(Player player, double amount){
		if(economy != null && economy.isEnabled())
			economy.depositPlayer(player, amount);
	}
	
	public static Boolean hasPermission(Player player, String perm){
		if(permission != null && permission.isEnabled())
			return permission.has(player, perm);
		return null;
	}
	
	public static String getPrefix(Player player){
		return getPrefix(player, false);
	}
	
	public static String getPrefix(Player player, boolean returnEmpty){
		if(!returnEmpty && chat != null && chat.getPlayerPrefix(player) != null)
			return chat.getPlayerPrefix(player);
		
		return "";
	}
}
