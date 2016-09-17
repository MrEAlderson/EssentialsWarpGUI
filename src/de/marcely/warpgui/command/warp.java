/**
* Adds an GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @version 1.5.3
* @website http://marcely.de/ 
*/

package de.marcely.warpgui.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.marcely.warpgui.Util;
import de.marcely.warpgui.Warp;
import de.marcely.warpgui.Language;
import de.marcely.warpgui.main;

public class warp implements CommandExecutor {
	private static int MAXPERPAGE = 36;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		onCommand(sender, label, args);
		return true;
	}
	
	public static void onCommand(CommandSender sender, String label, String[] args){
		if(sender instanceof Player){
			Player player = (Player) sender;
			if(args.length == 0){
				if(Util.getWarps(player).size() >= 1)
					player.openInventory(getWarpInventory(player, 1));
				else
					player.sendMessage(Language.No_Warps.getMessage());
			}else{
				Warp warp = main.warps.getWarp(args[0]);
				if(warp != null){
					if(Util.hasPermission(sender, "essentials.warps." + warp.getName().toLowerCase()) || Util.hasPermission(sender, "essentials.warps.*"))
						warp.warp(player);
					else
						sender.sendMessage(Language.No_Permissions.getMessage());
				}else
					player.sendMessage(Language.DoesntExist_Warp.getMessage().replace("{warp}", args[0]));
			}
		}else{
			sender.sendMessage(Language.NotA_Player.getMessage());
		}
	}
	
	
	public static void onInventoryClickEvent(InventoryClickEvent event){
		Player player = (Player) event.getWhoClicked();
		Inventory inv = event.getClickedInventory();
		ItemStack is = event.getCurrentItem();
		
		if(inv != null && inv.getTitle() != null && inv.getTitle().startsWith(main.CONFIG_INVTITLE) && is != null && is.getType() != null && is.getType() != Material.AIR){
			event.setCancelled(true);
			
			if(is == null || is.getType() == null || is.getType() == Material.AIR)
				return;
			
			// get page
			int page = 1;
			String p = inv.getTitle().replace(main.CONFIG_INVTITLE + " " + ChatColor.DARK_AQUA, "");
			if(Util.isInteger(p))
				page = Integer.valueOf(p);
			
			// get warp
			Warp warp = getWarpAt(
					player, 
					event.getSlot(), 
					page);
			
			// teleport
			if(warp != null){
				player.closeInventory();
				warp.warp(player);
			}
			
			// change page if --> or <--
			else{
				String name = Util.getItemStackName(is);
				
				if(name != null){
					
					int newPage = page;
					
					if(name.equals(ChatColor.GREEN + "-->"))
						newPage++;
					else if(name.equals(ChatColor.RED + "<--"))
						newPage--;
					
					// change page
					if(newPage != page){
						
						Inventory newInv = getWarpInventory(player, newPage);
						player.openInventory(newInv);
					}
				}
			}
		}
	}
	
	public static void onInventoryDragEvent(InventoryDragEvent event){
		Inventory inv = event.getInventory();
		if(inv.getTitle() != null && inv.getTitle().startsWith(main.CONFIG_INVTITLE))
			event.setCancelled(true);
	}
	
	public static Inventory getWarpInventory(Player player, int page){
		List<Warp> warps = Util.getWarps(player);
		Inventory inv = null;
		if(warps.size() > MAXPERPAGE)
			inv = Bukkit.createInventory(player, getInvSize(warps.size()), main.CONFIG_INVTITLE + " " + ChatColor.DARK_AQUA + page);
		else
			inv = Bukkit.createInventory(player, getInvSize(warps.size()), main.CONFIG_INVTITLE);
		
		for(ItemStack is:getWarpsByPage(player, page))
			inv.addItem(is);
		if(page < (double) warps.size() / MAXPERPAGE && inv.getSize() > MAXPERPAGE + 17)
			inv.setItem(MAXPERPAGE + 17, Util.getItemStack(new ItemStack(Material.STAINED_CLAY, 1, (short) 5), ChatColor.GREEN + "-->"));
		if(page > 1 && inv.getSize() > MAXPERPAGE + 9)
			inv.setItem(MAXPERPAGE + 9, Util.getItemStack(new ItemStack(Material.STAINED_CLAY, 1, (short) 14), ChatColor.RED + "<--"));
		if(warps.size() > MAXPERPAGE){
			for(int i=MAXPERPAGE; i<MAXPERPAGE + 9; i++)
				inv.setItem(i, Util.getItemStack(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15), " "));
		}
		return inv;
	}
	
	private static List<ItemStack> getWarpsByPage(Player player, int page){
		List<ItemStack> warps = new ArrayList<ItemStack>();
		int c = 1;
		for(ItemStack is:getWarps(player)){
			if(c >= (page - 1) * MAXPERPAGE && c <= page * MAXPERPAGE){
				warps.add(is);
			}
			c++;
		}
		return warps;
	}
	
	private static List<ItemStack> getWarps(Player player){
		List<ItemStack> warps = new ArrayList<ItemStack>();
		for(Warp warp:Util.getWarps(player)){
			ItemStack is = warp.getIcon();
			ItemMeta im = is.getItemMeta();
			
			// name
			im.setDisplayName(ChatColor.WHITE + warp.getPrefix() + Util.firstCharCaps(warp.getName()));
			
			// lores
			List<String> lores = new ArrayList<String>();
			for(String lore:warp.getLores())	
				lores.add(ChatColor.GRAY + lore);
			im.setLore(lores);
			
			is.setItemMeta(im);
			warps.add(is);
		}
		return warps;
	}
	
	public static int getInvSize(int size){
		for(int i=1; i<=6; i++){
			if(size >= i*9-9 && size < i*9)
				return i*9;
		}
		return 6*9;
	}
	
	public static Warp getWarpAt(Player player, int at, int page){
		List<Warp> warps = Util.getWarps(player);
		
		if(at > MAXPERPAGE)
			return null;
		
		int slot = (page - 1) * MAXPERPAGE + at;
		if(page > 1)
			slot--;
		
		if(slot < warps.size())
			return warps.get(slot);
		else
			return null;
		/*
		int i=0, slot = 0;
		
		for(Warp warp:warps){
			int p = i + 1/ MAXPERPAGE;
			
			if(p == page){
				System.out.println(p + " " + page + " " + slot + " " + at);
				if(slot == at)
					return warp;
			
				slot++;
			}
			
			i++;
		}
		
		return null;*/
	}
}
