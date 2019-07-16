package de.marcely.warpgui.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
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
import de.marcely.warpgui.util.Util;

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
			
			if(!Util.hasPermission(sender, "essentials.warps." + warp.getName().toLowerCase()) &&
			   !Util.hasPermission(sender, "essentials.warps.*")){
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
		
		final List<Warp> warps = new ArrayList<>(EssentialsWarpGUI.instance.getContainer().getWarps(player));
		final int maxPerPage = 2;
		
		if(warps.size() - maxPerPage*(page-1) <= 0)
			return false;
		
		final GUI gui = new GUI(ConfigValue.inventory_title, 1);
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
					
					name.append(warp.getName());
					
					im.setDisplayName(StringUtil.readableStringToFormattedChatColor(name.toString()));
				}
				
				// add lore
				im.setLore(warp.getLore());
				
				icon.setItemMeta(im);
			}
			
			// add to gui
			gui.addItem(new GUIItem(icon){
				public void onClick(Player whoClicked, boolean leftClick, boolean shiftClick){
					gui.close();
					warp.warp(player);
				}
			});
		}
		
		// add footer for moving between pages
		if(warps.size() > maxPerPage){
			gui.setHeight(6);
			
			for(int ix=0; ix<9; ix++)
				gui.setItemAt(new DecGUIItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15)), ix, 4);
			
			// go back
			if(page >= 2){
				gui.setItemAt(new GUIItem(ItemStackUtil.rename(new ItemStack(Material.STAINED_CLAY, 1, (short) 5), ChatColor.GREEN + "<--")){
					public void onClick(Player whoClicked, boolean leftClick, boolean shiftClick){
						openGUI(player, page-1);
					}
				}, 0, 5);
			}
			
			// go forward
			if(warps.size() - maxPerPage*(page) > 0){
				gui.setItemAt(new GUIItem(ItemStackUtil.rename(new ItemStack(Material.STAINED_CLAY, 1, (short) 5), ChatColor.GREEN + "-->")){
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
