package de.marcely.warpgui.command;

import java.util.HashMap;
import java.util.List;

import net.ess3.api.InvalidWorldException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.earth2me.essentials.commands.WarpNotFoundException;

import de.marcely.warpgui.Warp;
import de.marcely.warpgui.language;
import de.marcely.warpgui.main;

public class warp implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player){
			Player player = (Player) sender;
			if(args.length == 0){
				if(player.hasPermission("essentials.warp")){
					player.openInventory(getWarpInventory(player));
				}else{
					sender.sendMessage(ChatColor.DARK_RED + language.noPermissions);
				}
			}else{
				String warp = main.getRealName(args[0]);
				if(warp != null){
					if(sender.hasPermission("essentials.warps." + warp.toLowerCase()))
						teleportToWarp(player, warp);
					else
						sender.sendMessage(ChatColor.DARK_RED + language.noPermissions);
				}else{
					player.sendMessage(ChatColor.DARK_RED + language.theWarp +  " " + ChatColor.RED + args[0] + ChatColor.DARK_RED + " " + language.doesntExists);
				}
			}
		}else{
			sender.sendMessage(ChatColor.DARK_RED + language.notAPlayer);
		}
		return true;
	}
	
	
	public static void onInventoryClick(InventoryClickEvent event){
		Player player = (Player) event.getWhoClicked();
		Inventory inv = event.getInventory();
		ItemStack is = event.getCurrentItem();
		if(inv.getTitle() != null && inv.getTitle() == main.CONFIG_INVTITLE && is != null && is.getType() != null && is.getType() != Material.AIR){
			event.setCancelled(true);
			player.closeInventory();
			
			Warp warp = getWarpAt(player, event.getSlot());
			if(warp != null)
				teleportToWarp(player, warp.getName());
		}
	}
	
	public static void teleportToWarp(Player player, String warpname){
		player.sendMessage(language.teleportingTo.replace("{warp}", warpname));
		try{
			player.teleport(main.es.getWarps().getWarp(warpname));
		}catch(WarpNotFoundException e){
			player.sendMessage(ChatColor.RED + "A error occured: WarpNotFoundException");
			e.printStackTrace();
		}catch(InvalidWorldException e){
			player.sendMessage(ChatColor.RED + "A error occured: InvalidWorldException");
			e.printStackTrace();
		}
	}
	
	public Inventory getWarpInventory(Player player){
		List<Warp> warps = main.getWarps(player);
		Inventory inv = Bukkit.createInventory(player, getInvSize(warps.size()), main.CONFIG_INVTITLE);
		for(Warp warp:main.getWarps(player)){
			ItemStack is = warp.getIcon();
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', warp.getPrefix() + main.firstCharCaps(warp.getName())));
			is.setItemMeta(im);
			
			inv.addItem(is);
		}
		return inv;
	}
	
	public static int getInvSize(int size){
		for(int i=1; i<=10; i++){
			if(size >= i*9-9 && size < i*9)
				return i*9;
		}
		return 10*9;
	}
	
	public static Warp getWarpAt(Player player, int at){
		// todo: faster
		List<Warp> warps = main.getWarps(player);
		HashMap<Integer, Warp> list = new HashMap<Integer, Warp>();
		int i=0;
		for(Warp warp:warps){
			list.put(i, warp);
			i++;
		}
		for(i=0; i<getInvSize(warps.size()); i++){
			if(i == at && list.containsKey(at))
				return list.get(at);
		}
		
		return null;
	}
}
