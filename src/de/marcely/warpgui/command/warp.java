/**
* Adds an GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @version 1.4
* @website http://marcely.de/ 
*/

package de.marcely.warpgui.command;

import java.util.ArrayList;
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
import de.marcely.warpgui.Language;
import de.marcely.warpgui.main;

public class warp implements CommandExecutor {
	private static int MAXPERPAGE = 36;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player){
			Player player = (Player) sender;
			if(args.length == 0){
				if(player.hasPermission("essentials.warp")){
					player.openInventory(getWarpInventory(player, 1));
				}else{
					sender.sendMessage(Language.No_Permissions.getMessage());
				}
			}else{
				String warp = main.getRealName(args[0]);
				if(warp != null){
					if(sender.hasPermission("essentials.warps." + warp.toLowerCase()))
						teleportToWarp(player, warp);
					else
						sender.sendMessage(Language.No_Permissions.getMessage());
				}else{
					player.sendMessage(Language.DoesntExist_Wrarp.getMessage().replace("{warp}", args[0]));
				}
			}
		}else{
			sender.sendMessage(Language.NotA_Player.getMessage());
		}
		return true;
	}
	
	
	public static void onInventoryClick(InventoryClickEvent event){
		Player player = (Player) event.getWhoClicked();
		Inventory inv = event.getInventory();
		ItemStack is = event.getCurrentItem();
		if(inv.getTitle() != null && inv.getTitle().startsWith(main.CONFIG_INVTITLE) && is != null && is.getType() != null && is.getType() != Material.AIR){
			event.setCancelled(true);
			
			// get page
			int page = 1;
			String p = inv.getTitle().replace(main.CONFIG_INVTITLE + " " + ChatColor.DARK_AQUA, "");
			if(main.isNumeric(p))
				page = Integer.valueOf(p);
			
			Warp warp = getWarpAt(player, event.getSlot());
			if(warp != null){
				player.closeInventory();
				teleportToWarp(player, warp.getName());
			}else{
				ItemMeta im = is.getItemMeta();
				if(im != null && im.getDisplayName() != null){
					String name = im.getDisplayName();
					if(name.equals(ChatColor.GREEN + "->")){
						
						Inventory newInv = getWarpInventory(player, page + 1);
						for(int i=0; i<newInv.getSize(); i++)
							inv.setItem(i, newInv.getItem(i));
						
					}else if(name.equals(ChatColor.GREEN + "<-")){
						
						Inventory newInv = getWarpInventory(player, page - 1);
						for(int i=0; i<newInv.getSize(); i++)
							inv.setItem(i, newInv.getItem(i));
						
					}
				}
			}
		}
	}
	
	public static void teleportToWarp(Player player, String warpname){
		player.sendMessage(Language.Teleporting.getMessage().replace("{warp}", warpname));
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
	
	public static Inventory getWarpInventory(Player player, int page){
		List<Warp> warps = main.getWarps(player);
		Inventory inv = null;
		if(warps.size() > MAXPERPAGE)
			inv = Bukkit.createInventory(player, getInvSize(warps.size()), main.CONFIG_INVTITLE + " " + ChatColor.DARK_AQUA + page);
		else
			inv = Bukkit.createInventory(player, getInvSize(warps.size()), main.CONFIG_INVTITLE);
		
		for(ItemStack is:getWarpsByPage(player, page))
			inv.addItem(is);
		
		if(page < warps.size() / MAXPERPAGE)
			inv.setItem(MAXPERPAGE - 9, main.getItemStack(new ItemStack(Material.PAPER), ChatColor.GREEN + "->"));
		if(page > 1)
			inv.setItem(MAXPERPAGE - 1, main.getItemStack(new ItemStack(Material.PAPER), ChatColor.GREEN + "<-"));
		
		for(int i=54; i<44; i++)
			inv.setItem(i, main.getItemStack(new ItemStack(Material.STAINED_GLASS_PANE), " "));
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
		for(Warp warp:main.getWarps(player)){
			ItemStack is = warp.getIcon();
			ItemMeta im = is.getItemMeta();
			
			// name
			im.setDisplayName(ChatColor.WHITE + warp.getPrefix() + main.firstCharCaps(warp.getName()));
			
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
