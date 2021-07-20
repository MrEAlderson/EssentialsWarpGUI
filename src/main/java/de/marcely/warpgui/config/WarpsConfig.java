/**
* Adds a GUI for the essentials command /warp
* https://www.spigotmc.org/resources/essentials-warp-gui-opensource.13571/
*
* @author  Marcely1199
* @website https://marcely.de/ 
*/

package de.marcely.warpgui.config;

import java.util.logging.Logger;

import org.bukkit.inventory.ItemStack;

import de.marcely.configmanager2.EZConfigManager;
import de.marcely.configmanager2.objects.Config;
import de.marcely.configmanager2.objects.ListItem;
import de.marcely.configmanager2.objects.Tree;
import de.marcely.warpgui.EssentialsWarpGUI;
import de.marcely.warpgui.components.Warp;
import de.marcely.warpgui.components.WarpsContainer;
import de.marcely.warpgui.util.ItemStackUtil;
import de.marcely.warpgui.util.Util;

public class WarpsConfig {
	
	public static EZConfigManager cm = new EZConfigManager(Util.FILE_CONFIG_WARPS, true);
	
	public static void load(Logger logger){
		EssentialsWarpGUI.instance.getContainer().removeAllWarps();
		
		cm.load();
		
		for(Tree warpTree:cm.getRootTree().getTreeChilds()){
			final Warp warp = new Warp(warpTree.getName()){
				public WarpsContainer getContainer(){
					return EssentialsWarpGUI.instance.getContainer();
				}
			};
			
			// load base configs
			{
				final Config config_icon = warpTree.getConfigChild("icon");
				final Config config_prefix = warpTree.getConfigChild("prefix");
				final Config config_suffix = warpTree.getConfigChild("suffix");
				final Config config_displayName = warpTree.getConfigChild("display-name");
				final Config config_forceSlot = warpTree.getConfigChild("force-slot");
				
				if(config_icon != null){
					final ItemStack is = ItemStackUtil.ofString(config_icon.getValue());
					
					if(is != null)
						warp.setIcon(is);
					else
						logger.warning("Failed to parse icon of warp \"" + warp.getName() + "\": It has been reset back to default");
				}
				
				if(config_prefix != null)
					warp.setPrefix(config_prefix.getValue());
				
				if(config_suffix != null)
					warp.setSuffix(config_suffix.getValue());

				if(config_displayName != null)
					warp.setDisplayName(config_displayName.getValue());

				if(config_forceSlot != null)
					warp.setForceSlot(config_forceSlot.getValueAsInt());
			}
			
			// load lore
			final Tree loreTree = warpTree.getTreeChild("lore");
			
			if(loreTree != null){
				for(Config config:loreTree.getChilds()){
					if(config.getType() != ListItem.TYPE_LISTITEM)
						continue;
					
					warp.addLore(config.getValue());
				}
			}
			
			EssentialsWarpGUI.instance.getContainer().addWarp(warp);
		}
		
		cm.clear();
	}
	
	public static void save(){
		cm.clear();

		cm.addComment("Possible configs:");
		cm.addComment(" - icon");
		cm.addComment(" - prefix");
		cm.addComment(" - suffix");
		cm.addComment(" - lore");
		cm.addComment(" - display-name");
		cm.addComment(" - force-slot");
		
		for(Warp warp:EssentialsWarpGUI.instance.getContainer().getWarps()){
			final Tree warpTree = new Tree(warp.getName(), cm.getRootTree());
			
			warpTree.addChild(new Config("icon", warpTree, ItemStackUtil.toString(warp.getIcon())));
			if(warp.getPrefix() != null) warpTree.addChild(new Config("prefix", warpTree, warp.getPrefix()));
			if(warp.getSuffix() != null) warpTree.addChild(new Config("suffix", warpTree, warp.getSuffix()));
			if(warp.getDisplayName() != null) warpTree.addChild(new Config("display-name", warpTree, warp.getDisplayName()));
			if(warp.getForceSlot() != null) warpTree.addChild(new Config("force-slot", warpTree, warp.getForceSlot()));
			
			if(warp.getLore().size() >= 1){
				final Tree loreTree = new Tree("lore", warpTree);
				
				for(String lore:warp.getLore())
					loreTree.addChild(new ListItem(lore, loreTree));
				
				warpTree.addChild(loreTree);
			}
			
			cm.getRootTree().addChild(warpTree);
			cm.addEmptyLine();
		}
		
		cm.save();
	}
}