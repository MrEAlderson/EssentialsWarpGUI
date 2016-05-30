/**
* Adds an GUI for the essentials command /kit
* https://www.spigotmc.org/resources/essentials-kit-gui-opensource.15160/
*
* @author  Marcely1199
* @version 1.3
* @website http://marcely.de/ 
*/

package de.marcely.kitgui.command;

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

import com.earth2me.essentials.User;

import de.marcely.kitgui.Kit;
import de.marcely.kitgui.Language;
import de.marcely.kitgui.main;

public class kit implements CommandExecutor {
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
				if(player.hasPermission("essentials.kit")){
					player.openInventory(getKitInventory(player));
				}else{
					sender.sendMessage(Language.No_Permissions.getMessage());
				}
			}else{
				String kitname = args[0];
				com.earth2me.essentials.Kit kit = main.getKit(kitname.toLowerCase());
				if(kit != null){
					if(sender.hasPermission("essentials.kits." + kitname.toLowerCase()))
						giveKit(player, kit);
					else
						sender.sendMessage(Language.No_Permissions.getMessage());
				}else{
					player.sendMessage(Language.DoesntExist_Kit.getMessage().replace("{kit}", args[0]));
				}
			}
		}else{
			sender.sendMessage(Language.NotA_Player.getMessage());
		}
	}
	
	
	public static void onInventoryClickEvent(InventoryClickEvent event){
		Player player = (Player) event.getWhoClicked();
		Inventory inv = event.getInventory();
		ItemStack is = event.getCurrentItem();
		if(inv.getTitle() != null && inv.getTitle() == main.CONFIG_INVTITLE && is != null && is.getType() != null && is.getType() != Material.AIR){
			event.setCancelled(true);
			player.closeInventory();
			
			com.earth2me.essentials.Kit kit = main.getKit(getKitAt(player, event.getSlot(), 1).getName());
			
			giveKit(player, kit);
		}
	}
	
	public static void onInventoryDragEvent(InventoryDragEvent event){
		Inventory inv = event.getInventory();
		if(inv.getTitle() != null && inv.getTitle().startsWith(main.CONFIG_INVTITLE))
			event.setCancelled(true);
	}
	
	public static void giveKit(Player player, com.earth2me.essentials.Kit kit){
		User user = main.es.getUser(player);
		
		// check, if he is allowed to
		try { kit.checkDelay(user); } catch (Exception e) { return; }
		
		player.sendMessage(Language.Giving.getMessage().replace("{kit}", main.firstCharCaps(kit.getName())));
		
		// give items
		try { kit.expandItems(user); } catch (Exception e) { }
		
		// add to the scheduler from essentials
		try { kit.setTime(user); } catch (Exception e) { e.printStackTrace(); }
	}
	
	public static Inventory getKitInventory(Player player){
		ArrayList<Kit> kits = main.getKits(player);
		Inventory inv = Bukkit.createInventory(player, getInvSize(kits.size()), main.CONFIG_INVTITLE);
		for(Kit kit:kits){
			ItemStack is = kit.getIcon();
			ItemMeta im = is.getItemMeta();
			
			// name
			im.setDisplayName(ChatColor.WHITE + Language.stringToChatColor(kit.getPrefix()) + main.firstCharCaps(kit.getName()));
			
			// lores
			List<String> lores = new ArrayList<String>();
			for(String lore:kit.getLores())
				lores.add(ChatColor.GRAY + lore);
			im.setLore(lores);
			
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
	
	public static Kit getKitAt(Player player, int at, int page){
		List<Kit> kits = main.getKits(player);
		
		if(at > MAXPERPAGE)
			return null;
		
		int slot = (page - 1) * MAXPERPAGE + at;
		if(page > 1)
			slot--;
		
		if(slot < kits.size())
			return kits.get(slot);
		else
			return null;
	}
}
