/**
* Adds a GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @website https://marcely.de/ 
*/

package de.marcely.warpgui.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.marcely.ezgui.DecGUIItem;
import de.marcely.ezgui.GUI;
import de.marcely.ezgui.GUIItem;
import de.marcely.warpgui.EssentialsWarpGUI;
import de.marcely.warpgui.Message;
import de.marcely.warpgui.components.Warp;
import de.marcely.warpgui.config.ConfigValue;
import de.marcely.warpgui.util.ItemStackUtil;
import de.marcely.warpgui.util.StringUtil;

public class WarpCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		WarpCommand.onCommand(sender, label, args);
		
		return true;
	}
	
	public static void onCommand(CommandSender sender, String label, String[] args){
		if(!(sender instanceof Player)){
			sender.sendMessage(Message.NotA_Player.getValue());
			return;
		}
		
		// open gui
		if(args.length == 0){
			if(!openGUI((Player) sender))
				sender.sendMessage(Message.No_Warps.getValue());
			
			return;
		}
		
		// warp via command
		{
			final Warp warp = EssentialsWarpGUI.instance.getContainer().getWarp(args[0]);
			
			if(warp == null){
				sender.sendMessage(Message.DoesntExist_Warp.getValue().replace("{warp}", args[0]));
				
				return;
			}
			
			if(!warp.hasUsePermission(sender)){
				sender.sendMessage(Message.No_Permissions.getValue());
				
				return;
			}
			
			warp.warp((Player) sender);
		}
		
		return;
	}
	
	public static boolean openGUI(Player player){
		return openGUI(player, 1);
	}
	
	public static boolean openGUI(final Player player, final int page){
		EssentialsWarpGUI.instance.getContainer().synchronizeWithProvider(); // TODO Make it less
		
		final List<Warp> warps = EssentialsWarpGUI.instance.getContainer().getWarps(player);
		final int maxPerPage = 4*9;
		
		if(warps.size() - maxPerPage*(page-1) <= 0)
			return false;
		
		final GUI gui = new GUI(ConfigValue.inventory_title, Math.max(1, Math.min(6, ConfigValue.gui_height)));
		final int itemsAmount = Math.min(maxPerPage, warps.size() - maxPerPage*(page-1));
		
		// add items
		for(int i=0; i<itemsAmount; i++){
			final Warp warp = warps.get(maxPerPage*(page-1)+i);
			final ItemStack icon = warp.getIcon();
			
			// build icon
			{
				final ItemMeta im = icon.getItemMeta();
				
				// set name
				{
					final StringBuilder name = new StringBuilder();
					
					name.append(ChatColor.WHITE); // remove italic
					
					if(warp.getPrefix() != null)
						name.append(warp.getPrefix());

					if(warp.getDisplayName() != null)
						name.append(warp.getDisplayName());
					else
						name.append(warp.getName());
					
					if(warp.getSuffix() != null)
						name.append(warp.getSuffix());
					
					im.setDisplayName(StringUtil.translateToColorCodes(name.toString()));
				}
				
				// add lore
				{
					final List<String> newLore = new ArrayList<>(warp.getLore().size());
					
					for(String lore:warp.getLore())
						newLore.add(StringUtil.translateToColorCodes(lore));
					
					im.setLore(newLore);
				}
				
				icon.setItemMeta(im);
			}
			
			// add to gui
			final GUIItem gi = new GUIItem(icon){
				public void onClick(Player whoClicked, boolean leftClick, boolean shiftClick){
					gui.close();
					warp.warp(player);
				}
			};

			if(warp.getForceSlot() == null)
				gui.addItem(gi);
			else{
				final int slot = Math.max(0, Math.min(6*9-1, warp.getForceSlot()));
				final int height = (int) Math.ceil((slot+1) / 9D);

				if(height > gui.getHeight())
					gui.setHeight(height);

				gui.setItemAt(gi, slot);
			}
		}
		
		// add footer for moving between pages
		if(warps.size() > maxPerPage){
			gui.setHeight(6);
			
			for(int ix=0; ix<9; ix++)
				gui.setItemAt(new DecGUIItem(ItemStackUtil.BLACK_STAINED_GLASS_PANE.clone()), ix, 4);
			
			// go back
			if(page >= 2){
				gui.setItemAt(new GUIItem(ItemStackUtil.rename(ItemStackUtil.LIME_STAINED_GLASS_PANE.clone(), ChatColor.GREEN + "<--")){
					public void onClick(Player whoClicked, boolean leftClick, boolean shiftClick){
						openGUI(player, page-1);
					}
				}, 0, 5);
			}
			
			// go forward
			if(warps.size() - maxPerPage*(page) > 0){
				gui.setItemAt(new GUIItem(ItemStackUtil.rename(ItemStackUtil.LIME_STAINED_GLASS_PANE.clone(), ChatColor.GREEN + "-->")){
					public void onClick(Player whoClicked, boolean leftClick, boolean shiftClick){
						openGUI(player, page+1);
					}
				}, 8, 5);
			}
		}
		
		gui.open(player);
		
		return true;
	}
}
